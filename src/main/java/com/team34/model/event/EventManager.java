package com.team34.model.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.team34.model.UIDManager;

/**
 * This class manages all events and event order lists.
 * <p>
 * The events are stored in a HashMap, with UIDs (Long) as keys.
 * <p>
 * Event order lists are lists of event UID in a specific order. This allows switching
 * between different event orders and editing the order on a specific order list.
 *
 * @author Kasper S. Skott
 */
public class EventManager {

    private HashMap<Long, Event> events;
    private ArrayList<LinkedList<Long>> eventOrderLists;

    /**
     * Constructs the EventManager and sets optional initial capacities for events and order lists.
     * @param eventCapacity if 0, uses default constructor for events
     * @param orderListCapacity if 0, uses default constructor for order lists
     */
    public EventManager(int eventCapacity, int orderListCapacity) {
        if(eventCapacity > 0)
            events = new HashMap<Long, Event>(eventCapacity);
        else
            events = new HashMap<Long, Event>();

        if(orderListCapacity > 0) {
            eventOrderLists = new ArrayList<>(orderListCapacity);
        }
        else {
            eventOrderLists = new ArrayList<>();
            eventOrderLists.add(new LinkedList<Long>());
        }
    }

    /**
     * Constructs a new event and stores it. The new event UID is generated by the {@link UIDManager},
     * and is thereafter placed at the back of each event order list.
     * @param name the name of the event
     * @param description the description of the event
     * @return the UID of the new event
     */
    public long newEvent(String name, String description) {
        long uid = UIDManager.nextUID();
        addEvent(uid, name, description);
        return uid;
    }

    /**
     * Edits the data inside the event associated with the given UID.
     * @param uid the UID associated with the event to edit
     * @param name the new name
     * @param description the new description
     * @return true if the event was successfully edited; false if the edit failed.
     */
    public boolean editEvent(long uid, String name, String description) {
        if(events.containsKey(uid)) {
            events.replace(uid, new Event(name, description));
            return true;
        }
        return false;
    }

    /**
     * Removes the event associated with the given UID.
     * Also removes the UID from each order list, and the UIDManager.
     * @param uid the UID of the event to remove
     */
    public void removeEvent(long uid) {
        events.remove(uid);
        UIDManager.removeUID(uid);

        for(LinkedList<Long> e : eventOrderLists)
            e.remove(uid);
    }

    public void addEvent(long uid, String name, String description) {
        events.put(uid, new Event(name, description));

        for(LinkedList<Long> e : eventOrderLists)
            e.add(uid);
    }

    /**
     * Returns a structure of data contained within the event, specified with the given UID.
     * The data returned is formatted like this:
     * <ul>
     *  <li>data[0] -- name
     *  <li>data[1] -- description
     * </ul>
     * @param uid the UID of the event to get data from
     * @return an array with a constant size of 2
     */
    public Object[] getEventData(long uid) {
        Object[] data = new Object[2];
        Event event = events.get(uid);
        data[0] = event.getName();
        data[1] = event.getDescription();

        return data;
    }

    /**
     * Returns a structure which contains all data within every event.
     * <p>
     * The data returned is formatted like this:
     * <ul>
     *  <li>data[i] -- array of event data
     *  <ul>
     *      <li>data[i][0] -- UID
     *      <li>data[i][1] -- name
     *      <li>data[i][2] -- description
     *  </ul>
     * </ul>
     * <p>
     *
     * Example of contents:
     * <ul>
     *     <li>data[0][0] -- 1997L
     *     <li>data[0][1] -- "The Beginning"
     *     <li>data[0][2] -- "This is where it all began"
     *     <li>data[1][0] -- 2020L
     *     <li>data[1][1] -- "The End"
     *     <li>data[1][2] -- "This is where it all ended"
     * </ul>
     * <p>
     *
     * Note: The data returned is unordered.
     *
     * @return a 2-dimensional array of event data
     */
    public Object[][] getEvents() {
        if(events.size() < 1)
            return null;

        Long[] uidOrder = events.keySet().toArray(new Long[events.size()]);
        Object[][] eventArray = new Object[uidOrder.length][3];

        for (int i = 0; i < uidOrder.length; i++) {
            long uid = uidOrder[i];
            Event eventRef = events.get(uid);
            eventArray[i][0] = uid;
            eventArray[i][1] = eventRef.getName();
            eventArray[i][2] = eventRef.getDescription();
        }
        return eventArray;
    }

    /**
     * Returns an array of UIDs, specifying the order of events according
     * to the list at the specified index.
     * @param eventOrderList the index to the event order list to use
     * @return the order of event UIDs
     */
    public Long[] getEventOrder(int eventOrderList) {
        return eventOrderLists.get(eventOrderList).toArray(
                new Long[eventOrderLists.get(eventOrderList).size()]
        );
    }

    public void swapEvent(int orderList, int index1, int index2) {
        //TODO swap places between two events in the specified order list.
    }

    public void moveEvent(int orderList, int fromIndex, int toIndex) {
        // TODO move to (insert at) specified location.
    }

    public void addOrderList(LinkedList<Long> orderList) {
        eventOrderLists.add(orderList);
    }

    public void clear() {
        events.clear();
        eventOrderLists.clear();
    }

}

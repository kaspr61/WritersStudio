package com.team34.model.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.team34.model.UIDManager;

/**
 * @author Kasper S. Skott
 */
public class EventManager {

    private HashMap<Long, Event> events;
    private ArrayList<LinkedList<Long>> eventOrderLists;

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

    public long newEvent(String name, String description) {
        long uid = UIDManager.nextUID();
        events.put(uid, new Event(name, description));

        for(LinkedList<Long> e : eventOrderLists)
            e.add(uid);

        return uid;
    }

    public void editEvent(long uid, String name, String description) {
        if(!events.containsKey(uid)) {
            throw new IndexOutOfBoundsException("EventManager::editEvent : event with uid " + uid + " doesn't exist");
        }
        else {
            events.replace(uid, new Event(name, description));
        }
    }

    public void removeEvent(long uid) {
        events.remove(uid);
        UIDManager.removeUID(uid);

        for(LinkedList<Long> e : eventOrderLists)
            e.remove(uid);
    }

    public Object[][] getEvents() {
        if(events.size() < 1)
            throw new IndexOutOfBoundsException("There are no events");

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

    public Long[] getEventOrder(int eventOrderList) {
        return eventOrderLists.get(eventOrderList).toArray(
                new Long[eventOrderLists.get(eventOrderList).size()]
        );
    }

    public void swapEvent(int orderList, int index1, int index2) {
        //TODO swap places between to events in the specified order list.
    }

    public void moveEvent(int orderList, int fromIndex, int toIndex) {
        // TODO move to (insert at) specified location.
    }

    //// Temporary
    public static void main(String[] args) {
        EventManager em = new EventManager(5, 1);
        em.newEvent("Event A", "a");
        em.newEvent("Event B", "b");
        em.newEvent("Event C", "c");
        em.newEvent("Event D", "d");
        em.newEvent("Event E", "e");

        Object[][] events = em.getEvents();

        em.editEvent(1L, "aasddsasda", "eeeeeeee");

        em.removeEvent(3L);

        em.newEvent("NEW", "new desc");

        events = em.getEvents();

    }

}

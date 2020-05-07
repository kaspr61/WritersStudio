package com.team34.model.event;

public class EventListObject {
    private String title;
    private long uid;

    /**
     *
     * @param title Event title.
     * @param uid Event UID.
     */
    public EventListObject(String title, long uid) {
        this.title = title;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public long getUid() {
        return uid;
    }

    /**
     * Overridden toString function so the {@link com.team34.view.event.EventList} is able to display
     * the event's title in the list view.
     * @return
     */
    @Override
    public String toString() {
        return title;
    }
}

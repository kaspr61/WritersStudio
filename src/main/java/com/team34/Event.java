package com.team34;

/**
 * @author Jim Andersson
 */

public class Event {

    private String eventName = "";
    private String eventDescription = "";
    private Character character;

    public Event(String eventName, String eventDescription, Character character) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.character = character;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

}

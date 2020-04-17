package com.team34;

/**
 * Class for event objects that contains the event information.
 * @author Jim Andersson
 */

public class Event {

    private String name = "";
    private String description = "";

    /**
     * Instantiates event object with name and description.
     * @param name Event name
     * @param description Event description
     */
    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

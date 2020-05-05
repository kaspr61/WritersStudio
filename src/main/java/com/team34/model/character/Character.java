package com.team34.model.character;

/**
 * @author Morgan Karlsson
 */

public class Character {

    private String name = "";
    private String description = "";


    public Character(String name, String description){
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

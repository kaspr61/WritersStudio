package com.team34;

/**
 * @author Morgan Karlsson
 */

public class Character {

    private String characterName = "";
    private String characterDescription = "";
    private Event event;

    public Character( String characterName, String characterDescription, Event event){
        this.characterName = characterName;
        this.characterDescription = characterDescription;
        this.event = event;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterDescription() {
        return characterDescription;
    }

    public void setCharacterDescription(String characterDescription) {
        this.characterDescription = characterDescription;
    }
}

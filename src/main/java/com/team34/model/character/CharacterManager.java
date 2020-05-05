package com.team34.model.character;

import com.team34.model.UIDManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This class manages the characters in the application.
 * @author Jim Andersson
 */
public class CharacterManager {

    private HashMap<Long, Character> characters;

    public CharacterManager() {
        characters = new HashMap<>();
    }

    public long newCharacter(String name, String description) {
        long uid = UIDManager.nextUID();
        characters.put(uid, new Character(name, description));

        return uid;
    }

    public boolean editCharacter(long uid, String name, String description) {
        if (characters.containsKey(uid)) {
            characters.replace(uid, new Character(name, description));
            return true;
        }
        return false;
    }

    public void removeCharacter(long uid) {
        characters.remove(uid);
        UIDManager.removeUID(uid);
    }

    public ArrayList<String> getCharacters() {

        ArrayList<String> characterArrayList = new ArrayList<>();

        for (Map.Entry character : characters.entrySet()) {
            Character ch = (Character) character.getValue();
            characterArrayList.add(ch.getName());
        }
        return characterArrayList;
    }


}

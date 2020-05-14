package com.team34.model.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.team34.model.UIDManager;
import com.team34.view.character.CharacterListObject;


/**
 * Manages the characters in the application. All character objects are stored as values in a HashMap and can be
 * accessed through their respective UIDs, used as keys.
 *
 * The character manager class contains methods to create, edit and delete characters, and can return data from
 * individual character objects. It also contains the {@link #getCharacterList()} method, which returns an array of
 * {@link CharacterListObject} objects.
 * @author Jim Andersson
 */
public class CharacterManager {

    private HashMap<Long, Character> characterMap;

    public CharacterManager() {
        characterMap = new HashMap<>();
    }

    /**
     * Creates a new character and stores it in a HashMap.
     * @param name Character name.
     * @param description Character description.
     * @return UID of character.
     */
    public long newCharacter(String name, String description) {
        long uid = UIDManager.nextUID();
        characterMap.put(uid, new Character(name, description));

        return uid;
    }

    /**
     * Edits an existing character.
     * @param uid Existing character UID.
     * @param name Character name.
     * @param description Character description.
     * @return True if character exists, else returns False.
     */
    public boolean editCharacter(long uid, String name, String description) {
        if (characterMap.containsKey(uid)) {
            characterMap.replace(uid, new Character(name, description));
            return true;
        }
        return false;
    }

    /**
     * Deletes an existing character.
     * @param uid Character UID.
     */
    public void deleteCharacter(long uid) {
        characterMap.remove(uid);
        UIDManager.removeUID(uid);
    }

    /**
     * Returns an array list of Object arrays containing character names and UIDs.
     * @return ArrayList of Object[]
     */
    public ArrayList<Object[]> getCharacterList() {

        ArrayList<Object[]> characterArrayList = new ArrayList<>();

        for (Map.Entry character : characterMap.entrySet()) {
            Character ch = (Character) character.getValue();
            Object[] chListObj =  {ch.getName(), character.getKey()};
            characterArrayList.add(chListObj);
        }
        return characterArrayList;
    }

    /**
     * Returns a String array of individual character name and description.
     * @param uid Character UID.
     * @return String[]
     */
    public String[] getCharacterData(long uid) {
        String[] data = new String[2];
        data[0] = characterMap.get(uid).getName();
        data[1] = characterMap.get(uid).getDescription();

        return data;
    }
}

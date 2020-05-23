package com.team34.model.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private HashMap<Long, Association> associationMap;
    private boolean hasChanged;

    public CharacterManager() {
        characterMap = new HashMap<>();
        associationMap = new HashMap<>();
        hasChanged = false;
    }

    /**
     * Creates a new character and stores it in a HashMap.
     * @param name Character name.
     * @param description Character description.
     * @return UID of character.
     */
    public long newCharacter(String name, String description) {
        return newCharacter(name, description, 0.0, 0.0);
    }

    public long newCharacter(String name, String description, double posX, double posY) {
        long uid = UIDManager.nextUID();
        addCharacter(uid, name, description, posX, posY);
        return uid;
    }

    public void addCharacter(long uid, String name, String description, double posX, double posY) {
        characterMap.put(uid, new Character(name, description, posX, posY));
        hasChanged = true;
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
            Character existing = characterMap.get(uid);
            characterMap.replace(uid,
                    new Character(name, description, existing.getChartPositionX(), existing.getChartPositionY())
            );
            hasChanged = true;
            return true;
        }

        return false;
    }

    public boolean editCharacter(long uid, double chartPosX, double chartPosY) {
        if (characterMap.containsKey(uid)) {
            Character existing = characterMap.get(uid);
            characterMap.replace(uid,
                    new Character(existing.getName(), existing.getDescription(), chartPosX, chartPosY)
            );
            hasChanged = true;
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
        hasChanged = true;
    }

    public long newAssociation(long sCharUID, long eCharUID, double sX, double sY, double eX, double eY, String label, double lblX, double lblY) {
        long uid = UIDManager.nextUID();
        addAssociation(uid, sCharUID, eCharUID, sX, sY, eX, eY, label, lblX, lblY);
        return uid;
    }

    public void addAssociation(long uid, long sCharUID, long eCharUID, double sX, double sY, double eX, double eY, String label, double lblX, double lblY) {
        Association assoc = new Association();
        assoc.startCharacterUID = sCharUID;
        assoc.endCharacterUID = eCharUID;
        assoc.startX = sX;
        assoc.startY = sY;
        assoc.endX = eX;
        assoc.endY = eY;
        assoc.label = label;
        assoc.labelX = lblX;
        assoc.labelY = lblY;

        associationMap.put(uid, assoc);
        hasChanged = true;
    }

    public boolean editAssociation(long uid, long sCharUID, long eCharUID, double sX, double sY, double eX, double eY, String label, double lblX, double lblY) {
        if (associationMap.containsKey(uid)) {
            Association assoc = new Association();
            assoc.startCharacterUID = sCharUID;
            assoc.endCharacterUID = eCharUID;
            assoc.startX = sX;
            assoc.startY = sY;
            assoc.endX = eX;
            assoc.endY = eY;
            assoc.label = label;
            assoc.labelX = lblX;
            assoc.labelY = lblY;

            associationMap.replace(uid, assoc);
            hasChanged = true;
            return true;
        }

        return false;
    }

    public void deleteAssociation(long uid) {
        associationMap.remove(uid);
        UIDManager.removeUID(uid);
        hasChanged = true;
    }

    /**
     * Returns an array list of Object arrays containing character names and UIDs.
     * @return ArrayList of Object[]
     *///TODO update javadoc
    public ArrayList<Object[]> getCharacterList() {
        ArrayList<Object[]> characterArrayList = new ArrayList<>();

        for (Map.Entry character : characterMap.entrySet()) {
            Character ch = (Character) character.getValue();
            Object[] chListObj =  new Object[5];
            chListObj[0] = ch.getName();
            chListObj[1] = character.getKey();
            chListObj[2] = ch.getChartPositionX();
            chListObj[3] = ch.getChartPositionY();
            chListObj[4] = ch.getDescription();
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

    /**
     * Returns a String array of individual character name and description.
     * @param uid Character UID.
     * @return String[]
     */
    public Object[] getAssociationData(long uid) {
        Object[] data = new Object[9];
        Association assoc = associationMap.get(uid);
        data[0] = assoc.startCharacterUID;
        data[1] = assoc.endCharacterUID;
        data[2] = assoc.startX;
        data[3] = assoc.startY;
        data[4] = assoc.endX;
        data[5] = assoc.endY;
        data[6] = assoc.label;
        data[7] = assoc.labelX;
        data[8] = assoc.labelY;

        return data;
    }

    public Object[][] getAssociationData() {
        if(associationMap.size() < 1)
            return null;

        Object[][] array = new Object[associationMap.size()][10];
        Iterator<Map.Entry<Long, Association>> it = associationMap.entrySet().iterator();

        int i = 0;
        while(it.hasNext()) {
            Map.Entry<Long, Association> entry = it.next();
            Association assoc = entry.getValue();
            array[i][0] = entry.getKey();
            array[i][1] = assoc.startCharacterUID;
            array[i][2] = assoc.endCharacterUID;
            array[i][3] = assoc.startX;
            array[i][4] = assoc.startY;
            array[i][5] = assoc.endX;
            array[i][6] = assoc.endY;
            array[i][7] = assoc.label;
            array[i][8] = assoc.labelX;
            array[i][9] = assoc.labelY;
            i++;
        }

        return array;
    }

    public void clear() {
        characterMap.clear();
        associationMap.clear();
        hasChanged = false;
    }

    /**
     * Returns whether the data inside this class has changed
     * @return the value of {@link #hasChanged}
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     * Sets {@link #hasChanged} to false.
     */
    public void resetChanges() {
        hasChanged = false;
    }

}

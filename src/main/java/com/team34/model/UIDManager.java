package com.team34.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * The UID Manager generates and stores Unique IDs (UID).
 * The UID Manager class can create new UIDs or remove existing UIDs.
 * @author Jim Andersson
 * @version 1.0
 */
public class UIDManager {
    private static ArrayList<Long> uidList = new ArrayList<>();

    /**
     * Creates a UID and checks it against the UID list to ensure uniqueness. If unique, returns the UID.
     * @return Long UID.
     */
    public static long nextUID() {
        UUID idGenerator;
        long uid;
        do {
            Random rand = new Random();
            long input1 = rand.nextLong();
            long input2 = rand.nextLong();
            idGenerator = new UUID(input1, input2);
            uid = idGenerator.getMostSignificantBits();
        } while (!isUnique(uid));
        uidList.add(uid);
        return uid;
    }

    /**
     * Checks against the UID list to verify that the UID is unique. Returns True if unique, else returns False.
     * @param uid UID
     * @return boolean
     */
    private static boolean isUnique(long uid) {
        for (long listEntry : uidList) {
            if (uid == listEntry) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes UID from UIDList. Returns True if UID is found and successfully removed, else returns False.
     * (The below warning speaks lies, the method does in fact return a boolean).
     * @param uid UID
     * @return boolean
     */
    public static boolean removeUID(long uid) {
        return uidList.remove(uid);
    }

    /**
     * Adds a UID to {@link UIDManager#uidList} manually.
     * This should only be used when loading a project.
     * @param uid the UID to add
     * @author Kasper S. Skott
     */
    public static void addUID(long uid) {
        uidList.add(uid);
    }

    /**
     * Removes all UIDs from {@link UIDManager#uidList}.
     * This should only ever need to be used when creating or loading a project.
     * @author Kasper S. Skott
     */
    public static void clear() {
        uidList.clear();
    }

    /**
     * Returns all UIDs as an array.
     * @return the UIDs contained within {@link UIDManager#uidList}
     * @author Kasper S. Skott
     */
    public static Long[] getUIDs() {
        return uidList.toArray(new Long[uidList.size()]);
    }


    //////////////////
    /// TEST AREA ///
    ////////////////

    /**
     * FOR TEST PURPOSES ONLY
     * Prints the UIDs stored in UIDList.
     */
    private void printUidList() {
        for (Long id : uidList) {
            System.out.println(id + " List size: " + uidList.size() + "\n" );
        }
    }

    /**
     * FOR TEST PURPOSES ONLY
     * Creates and prints UIDs.
     * @param args n/a
     */
    public static void main(String[] args) {

        UIDManager manager = new UIDManager();
        long uid1, uid2, uid3;

        System.out.println("--- Add three UIDs ---");

        uid1 = manager.nextUID();
        uid2 = manager.nextUID();
        uid3 = manager.nextUID();

        System.out.println("UID1: " + uid1);
        System.out.println("UID2: " + uid2);
        System.out.println("UID3: " + uid3);
        manager.printUidList();

        System.out.println("--- Removed third UID entry --- \n");

        manager.removeUID(uid3);
        manager.printUidList();

    }

}

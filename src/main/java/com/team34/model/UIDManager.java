package com.team34.model;

// THIS IS A STUB CLASS

public class UIDManager {

    private static long next = 0L;

    public static long nextUID() {
        long uid = next;
        next++;
        return uid;
    }

    public static boolean removeUID(long uid) {
        return true;
    }

}

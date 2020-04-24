package com.team34;

public class Debug {

    private static final boolean DEBUG = true;

    public static void print(Object o) {
        if(DEBUG)
            System.out.print(o);
    }

    public static void println(Object o) {
        if(DEBUG)
            System.out.println(o);
    }

    public static void printf(String format, Object ... args) {
        if(DEBUG)
            System.out.printf(format, args);
    }

}

package com.example.usi.lanloc;

/**
 * Created by Jennifer Busta on 13.12.17.
 */

public class GlobalVars {
    public static Boolean ALL_USER_MODE = true;
    public static Boolean SPECIFIC_USER_MODE = false;

    public static void switchMode() {
        if (ALL_USER_MODE) {
            ALL_USER_MODE = false;
            SPECIFIC_USER_MODE = true;
        } else if (SPECIFIC_USER_MODE) {
            SPECIFIC_USER_MODE = false;
            ALL_USER_MODE = true;
        }
    }
}

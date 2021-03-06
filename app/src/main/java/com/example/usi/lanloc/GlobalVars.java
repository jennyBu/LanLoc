package com.example.usi.lanloc;

import android.provider.Settings;

/**
 * Created by Jennifer Busta on 13.12.17.
 */

public class GlobalVars {
    public static Boolean ALL_USER_MODE = true;
    public static Boolean SPECIFIC_USER_MODE = false;
    public static CollectionPagerAdapter COLLECTION_PAGER_ADAPTER = null;
    public static String ANDROID_ID = "";

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

package com.example.usi.lanloc.db;

import com.orm.SugarRecord;

/**
 * Created by Jennifer Busta on 19.11.17.
 */

public class User extends SugarRecord<User> {
    String android_id;

    public User (String android_id) {
        this.android_id = android_id;
    }

    public User() {

    }
}

package com.example.usi.lanloc.db;

import com.orm.SugarRecord;

/**
 * Created by Jennifer Busta on 19.11.17.
 */

public class Position extends SugarRecord<Position> {
    Double latitude;
    Double longitude;

    public Position( Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Position() {

    }
}

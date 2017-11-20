package com.example.usi.lanloc.db;

import com.orm.SugarRecord;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Jennifer Busta on 19.11.17.
 */

public class Record extends SugarRecord<Record> {
    Blob audio;
    User user;
    Position position;
    Timestamp timestamp;
    Integer upVotes;
    Integer downVotes;

    public Record(Blob audio, User user, Position position) {
        this.audio = audio;
        this.user = user;
        this.position = position;
        this.timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        this.upVotes = 0;
        this.downVotes = 0;
    }

    public Record() {

    }
}

package com.example.usi.lanloc;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.TableLayout;
import android.support.design.widget.TabLayout;

import com.example.usi.lanloc.db.*;
import com.orm.SugarApp;
import com.orm.SugarDb;

import java.io.File;
import java.sql.Blob;
import java.util.List;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SugarDb db = new SugarDb(this);
        //db.onCreate(db.getWritableDatabase());

        /*SugarDb sugarDb = new SugarDb(getApplicationContext());
        new File(sugarDb.getWritableDatabase().getPath()).delete();
        sugarDb.onCreate(sugarDb.getWritableDatabase());
        User.findById(User.class, (long) 1);
        Position.findById(Position.class, (long) 1);
        Record.findById(Record.class, (long) 1);*/

        //String android_id = Secure.getString(getContext().getContentResolver(), Secure.ANDROID_ID);
        User student = new User(Secure.ANDROID_ID);
        student.save();

        Double latitude = 46.010748;
        Double longitude = 8.958106;
        Position usi = new Position(latitude, longitude);
        usi.save();

        Record record = new Record(null, student, usi);
        record.save();

        List<Position> positions = Position.listAll(Position.class);
        Position position = Position.findById(Position.class, 1L);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}

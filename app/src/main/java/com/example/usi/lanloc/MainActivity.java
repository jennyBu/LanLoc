package com.example.usi.lanloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView recordview;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new DatabaseActivity().execute("getUserByAndroidId", "222333444");
        //new DatabaseActivity().getUserByAndroidId("222333444");
        //new DatabaseActivity().getUserById("1");
        //new DatabaseActivity().getPositionId(123.222, 345.222);
        //new DatabaseActivity().addRecord(1, 1, "apath/to/somewhere/else");
        //new DatabaseActivity().voteRecordDown(4);
        //new DatabaseActivity().voteRecordUp(2);
        //new DatabaseActivity().getAllPositionsInRange(46.010475, 8.957066, 1000);
        //List<Integer> positions = new ArrayList<>();
        //positions.add(1);
        //positions.add(2);
        //positions.add(5);
        //new DatabaseActivity().getRecordsForPositions(positions);
        //new DatabaseActivity().getPositionById(1);

        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(collectionPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);

        recordview = (ImageView) findViewById(R.id.record_icon);

        recordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Toast.makeText(MainActivity.this, "Recording",
                //              Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, audio.class));

            }
        });
    }
}

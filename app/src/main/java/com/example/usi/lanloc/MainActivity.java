package com.example.usi.lanloc;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}

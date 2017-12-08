package com.example.usi.lanloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.usi.lanloc.audio.RecordingActivity;

public class MainActivity extends AppCompatActivity {

    ImageView recordview;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                startActivity(new Intent(MainActivity.this, RecordingActivity.class));

            }
        });

        /*final ImageView bubble = (ImageView) findViewById(R.id.bubble_icon);
        final ImageView user = (ImageView) findViewById(R.id.user_icon);

        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bubble.getDrawable().equals(R.drawable.bubble_blue)) {
                    bubble.setImageResource(R.drawable.bubble_grey);
                    user.setImageResource(R.drawable.user_blue);
                }
                bubble.setImageResource(R.drawable.bubble_blue);
                user.setImageResource(R.drawable.user_grey);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getDrawable().equals(R.drawable.user_grey)) {
                    bubble.setImageResource(R.drawable.bubble_grey);
                    user.setImageResource(R.drawable.user_blue);
                }
                bubble.setImageResource(R.drawable.bubble_blue);
                user.setImageResource(R.drawable.user_grey);
            }
        });*/
    }
}

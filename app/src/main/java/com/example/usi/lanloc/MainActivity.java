package com.example.usi.lanloc;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.example.usi.lanloc.audio.RecordingActivity;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_LOCATION = 0;
    public static final int PERMISSIONS_REQUEST_RECORDING = 1;
    public static final int PERMISSIONS_REQUEST_STORAGE = 2;

    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVars.ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        setUpViewPager();
        setUpFooterActions();
        setUpLocationService();
    }

    private void setUpViewPager() {
        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(collectionPagerAdapter);
        GlobalVars.COLLECTION_PAGER_ADAPTER = collectionPagerAdapter;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    private void setUpFooterActions() {
        final ImageView bubble = (ImageView) findViewById(R.id.bubble_icon);
        final ImageView user = (ImageView) findViewById(R.id.user_icon);
        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);

        bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVars.SPECIFIC_USER_MODE) {
                    GlobalVars.switchMode();
                    bubble.setImageResource(R.drawable.bubble_blue);
                    user.setImageResource(R.drawable.user_grey);
                    updateFragments();
                }
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVars.ALL_USER_MODE) {
                    GlobalVars.switchMode();
                    bubble.setImageResource(R.drawable.bubble_grey);
                    user.setImageResource(R.drawable.user_blue);
                    updateFragments();
                }
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent recordingActivity = new Intent(MainActivity.this, RecordingActivity.class);
                    startActivity(recordingActivity);
                }
            }
        });
    }

    private void setUpLocationService() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        startUpdatingGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdatingGPS();
    }

    private void startUpdatingGPS() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORDING);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_STORAGE);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private void stopUpdatingGPS() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update, menu);

        final MenuItem menuItemUpdate = menu.findItem(R.id.menuItemUpdate);
        menuItemUpdate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                updateFragments();
                return true;
            }
        });

        return true;
    }

    private void updateFragments() {
        collectionPagerAdapter.updateFragments();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                updateFragments();
                break;
            case PERMISSIONS_REQUEST_RECORDING:
                break;
            case PERMISSIONS_REQUEST_STORAGE:
                break;
        }
    }
}
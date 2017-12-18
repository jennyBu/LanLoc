package com.example.usi.lanloc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import com.example.usi.lanloc.audio.RecordingActivity;

import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    ImageView recordview;
    CollectionPagerAdapter collectionPagerAdapter;
    ViewPager viewPager;

    LocationManager locationManager;
    LocationListener locationListener;


    public String AudioSavePathInDevice = null;
    public String AudioSavePathInDevice1 = null;

    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        collectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(collectionPagerAdapter);
        GlobalVars.COLLECTION_PAGER_ADAPTER = collectionPagerAdapter;

        GlobalVars.ANDROID_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);

        final ImageView bubble = (ImageView) findViewById(R.id.bubble_icon);
        final ImageView user = (ImageView) findViewById(R.id.user_icon);

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //System.out.println("Location updated " + location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



        random = new Random();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


               /*     if(checkPermission()) {

                        AudioSavePathInDevice1 = CreateRandomAudioFileName(5) + "AudioRecording.3gp";


                        AudioSavePathInDevice =
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                        AudioSavePathInDevice1;

                        MediaRecorderReady();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, "Start recording",
                                Toast.LENGTH_LONG).show();*/

                        Intent RecordingActivity2 = new Intent(MainActivity.this, RecordingActivity.class);


                        //     startActivity(new Intent(MainActivity.this, RecordingActivity.class));
                        startActivity(RecordingActivity2);

/*                    } else {
                        requestPermission();
                    }



                    // The toggle is enabled
                } else {
              /*      Toast.makeText(MainActivity.this, "Stop recording",
                            Toast.LENGTH_LONG).show();

                    mediaRecorder.stop();

                    Intent RecordingActivity = new Intent(MainActivity.this, RecordingActivity.class);
                    RecordingActivity.putExtra("AudioSavePathInDevice", AudioSavePathInDevice);
                    RecordingActivity.putExtra("AudioSavePathInDevice1", AudioSavePathInDevice1);

                    //     startActivity(new Intent(MainActivity.this, RecordingActivity.class));
                    startActivity(RecordingActivity);
                    // The toggle is disabled*/
                }
            }
        });




      /*  recordview = (ImageView) findViewById(R.id.record_icon);

        recordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Toast.makeText(MainActivity.this, "Recording",
                //              Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, RecordingActivity.class));

            }
        });  */
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME");
        startUpdatingGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdatingGPS();
    }

    private void startUpdatingGPS() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

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


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(MainActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}

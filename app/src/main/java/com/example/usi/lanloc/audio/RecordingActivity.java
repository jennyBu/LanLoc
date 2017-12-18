package com.example.usi.lanloc.audio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.usi.lanloc.GlobalVars;
import com.example.usi.lanloc.MainActivity;
import com.example.usi.lanloc.R;
import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class RecordingActivity extends AppCompatActivity {

    public String AudioSavePathInDevice2 = null;
    public String AudioSavePathInDevice3 = null;
    String AudioSavePathInDevice = null;
    String AudioSavePathInDevice1 = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    ImageView playview;
    ImageView pauseview;
    ImageView uploadview;
    ImageView backview;
    ImageView speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        random = new Random();

        if (getIntent().getExtras() != null) {
            AudioSavePathInDevice = getIntent().getStringExtra("AudioSavePathInDevice");
            AudioSavePathInDevice1 = getIntent().getStringExtra("AudioSavePathInDevice1");
        }

        pauseview = (ImageView) findViewById(R.id.pause_icon);
        playview = (ImageView) findViewById(R.id.play_icon);
        backview = (ImageView) findViewById(R.id.back_icon);
        uploadview = (ImageView) findViewById(R.id.upload_icon);
        speaker = (ImageView) findViewById(R.id.imageView);

        AudioSavePathInDevice3= CreateRandomAudioFileName(5) + "AudioRecording.3gp";
        AudioSavePathInDevice2 =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                        AudioSavePathInDevice3;
        AudioSavePathInDevice = AudioSavePathInDevice2;
        AudioSavePathInDevice1 = AudioSavePathInDevice3;

        MediaRecorderReady();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        backview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordingActivity.this, MainActivity.class));

            }
        });

        playview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        speaker.setImageResource(R.drawable.ic_speaker_orange_big);
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        speaker.setImageResource(R.drawable.ic_speaker_big);
                    }
                });
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(RecordingActivity.this, "Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        pauseview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                speaker.setImageResource(R.drawable.ic_speaker_big);
            }
        });

        uploadview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFileUpload();
                addToDatabase();
                startActivity(new Intent(RecordingActivity.this, MainActivity.class));
                //TODO go here back to last fragment in mainactivity (or is this already happening?)
                //TODO remove from php file not used functions and put php file to server
            }
        });

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);
        toggle.setChecked(true);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    speaker.setImageResource(R.drawable.ic_speaker_orange_big);

                    if (checkPermission()) {

                        AudioSavePathInDevice3= CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                        AudioSavePathInDevice2 =
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                        AudioSavePathInDevice3;
                        AudioSavePathInDevice = AudioSavePathInDevice2;
                        AudioSavePathInDevice1 = AudioSavePathInDevice3;

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
                    } else {
                        requestPermission();
                    }

                    // The toggle is enabled
                } else {
                    speaker.setImageResource(R.drawable.ic_speaker_big);

                    mediaRecorder.stop();

                    AudioSavePathInDevice = AudioSavePathInDevice2;
                    AudioSavePathInDevice1 = AudioSavePathInDevice3;
                }
            }
        });
    }

    private void addToDatabase() {
        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {

            }
        });


        //THIS GETS THE CURRENT GPS LOCATION AND TAGS IT TO THE VOICE RECORDING TO BE UPLOADED TO THE db.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l != null) {
            asyncTask.addRecord(GlobalVars.ANDROID_ID, l.getLatitude(), l.getLongitude(), "uploads/"+ AudioSavePathInDevice);

        } else {
            System.out.println("Can't add recording, no previous location");
        }
    }

    private void doFileUpload() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                //private void doFileUpload(){
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                //DataInputStream inStream = null;
                BufferedReader inStream = null;


                String lineEnd = "\r\n";
              //  String lineEnd = "rn";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String responseFromServer = "";
                Log.d("function", "this is the value of selected path" + AudioSavePathInDevice);
                String urlString = "http://uc-edu.mobile.usilu.net/audio3.php";
                try {
                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(AudioSavePathInDevice));
                    // open a URL connection to the Servlet
                    URL url = new URL(urlString);
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("fileToUpload",AudioSavePathInDevice1);


                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\"" + AudioSavePathInDevice1 + "\"" + lineEnd);

                    //dos.writeBytes("Content-Disposition: form-data; name="uploadedfile";filename="" + selectedPath + """ + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    System.out.println(buffer);
                    System.out.println(bufferSize);
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams
                    Log.e("Debug", "File is written");
                    Log.e("Debug",AudioSavePathInDevice1 );

                    // TO DO  enter record on database with username, position and audio record path (AudiosavepathDevice). AudiosavepathDevice1 returns the audio filename only.


                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage().toString();
                    Log.i("joshtag", "HTTP Response is : "  + serverResponseMessage + ": " +  serverResponseCode);
                    Log.i("joshtag", "HTTP Response is : "  + serverResponseMessage + ": " +  serverResponseCode);
                    Log.i("joshtag", "HTTP Response is : "  + conn.getContent());


                    fileInputStream.close();
                    dos.flush();
                    dos.close();



                } catch (MalformedURLException ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                } catch (IOException ioe) {
                    Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                }
                //------------------ read the SERVER RESPONSE


           /*     try {

                //    inStream = new BufferedReader(conn.getInputStream());
                //    BufferedReader inStream = new BufferedReader(conn.getInputStream());
                 //    inStream = new DataInputStream(conn.getInputStream());
                    String str;

                    while ((str = inStream.readLine()) != null) {
                  //     while ((str = BufferedReader.readLine()) != null) {
                    Log.e("Debug", "Server Response " + str);
                    }
                    inStream.close();

                } catch (IOException ioex) {
                    Log.e("Debug", "error: " + ioex.getMessage(), ioex);
                }*/




                try{
                    String inputLine;
                    inStream = new BufferedReader(new FileReader(AudioSavePathInDevice));
                    while ((inputLine = inStream.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inStream != null) {
                            inStream.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }

                return null;
            }

            protected void onPostExecute(Void feed) {

            }

        }.execute();

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
        ActivityCompat.requestPermissions(RecordingActivity.this, new
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
                        Toast.makeText(RecordingActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordingActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
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
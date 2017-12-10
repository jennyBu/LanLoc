package com.example.usi.lanloc.audio;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.usi.lanloc.MainActivity;
import com.example.usi.lanloc.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.usi.lanloc.audio.RecordingActivity2.RequestPermissionCode;



public class RecordingActivity2 extends AppCompatActivity {

    //  Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
//            buttonStopPlayingRecording ;
    public String AudioSavePathInDevice2 = null;
    public String AudioSavePathInDevice3 = null;
    String AudioSavePathInDevice = null;
    String AudioSavePathInDevice1 = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    ImageView recordview;
    ImageView playview;
    ImageView pauseview;
    ImageView uploadview;
    ImageView backview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio2);

        random = new Random();

        if (getIntent().getExtras() != null) {
            AudioSavePathInDevice = getIntent().getStringExtra("AudioSavePathInDevice");
            AudioSavePathInDevice1 = getIntent().getStringExtra("AudioSavePathInDevice1");
        }

     //   recordview = (ImageView) findViewById(R.id.record_icon);
        pauseview = (ImageView) findViewById(R.id.pause_icon);
        playview = (ImageView) findViewById(R.id.play_icon);
        backview = (ImageView) findViewById(R.id.back_icon);
        uploadview = (ImageView) findViewById(R.id.upload_icon);

        backview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Toast.makeText(MainActivity.this, "Recording",
                //              Toast.LENGTH_LONG).show();
//                startActivity(new Intent(MainActivity.this, RecordingActivity.class));
                startActivity(new Intent(RecordingActivity2.this, MainActivity.class));

            }
        });

        uploadview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Toast.makeText(MainActivity.this, "Recording",
                //              Toast.LENGTH_LONG).show();
//                startActivity(new Intent(MainActivity.this, RecordingActivity.class));
                doFileUpload();

            }
        });




        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


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

                        Toast.makeText(RecordingActivity2.this, "Start recording",
                                Toast.LENGTH_LONG).show();



                    } else {
                        requestPermission();
                    }


                    // The toggle is enabled
                } else {
                    Toast.makeText(RecordingActivity2.this, "Stop recording",
                            Toast.LENGTH_LONG).show();

                    mediaRecorder.stop();

                    AudioSavePathInDevice = AudioSavePathInDevice2;
                    AudioSavePathInDevice1 = AudioSavePathInDevice3;



               /*     Intent RecordingActivity2 = new Intent(RecordingActivity2.this, RecordingActivity2.class);
                    RecordingActivity2.putExtra("AudioSavePathInDevice", AudioSavePathInDevice);
                    RecordingActivity2.putExtra("AudioSavePathInDevice1", AudioSavePathInDevice1);*/

                    //     startActivity(new Intent(MainActivity.this, RecordingActivity2.class));
                    // The toggle is disabled
                }
            }
        });


 /*       ToggleButton toggle = (ToggleButton) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RecordingActivity2.this, "Start recording",
                            Toast.LENGTH_LONG).show();


                    // The toggle is enabled
                } else {
                    Toast.makeText(RecordingActivity2.this, "Stop recording",
                            Toast.LENGTH_LONG).show();

                //    startActivity(new Intent(RecordingActivity2.this, RecordingActivity2.class));
                    // The toggle is disabled
                }
            }
        }); */


        ToggleButton toggle2 = (ToggleButton) findViewById(R.id.toggle2);
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RecordingActivity2.this, "Start playing",
                            Toast.LENGTH_LONG).show();

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(RecordingActivity2.this, "Playing",
                            Toast.LENGTH_LONG).show();



                    // The toggle is enabled
                } else {
                    Toast.makeText(RecordingActivity2.this, "Stop playing",
                            Toast.LENGTH_LONG).show();

                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        MediaRecorderReady();
                    }

                    //     startActivity(new Intent(RecordingActivity2.this, RecordingActivity2.class));
                    // The toggle is disabled
                }
            }
        });






  /*      recordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Toast.makeText(MainActivity.this, "Recording",
                //              Toast.LENGTH_LONG).show();
//                startActivity(new Intent(MainActivity.this, RecordingActivity.class));
                //       startActivity(new Intent(RecordingActivity2.this, RecordingActivity2.class));
                if (checkPermission()) {

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


                    Toast.makeText(RecordingActivity2.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        }); W*/


    }


/*        playview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(RecordingActivity2.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();

            }
        });



    } */






 /*   public class doFileUpload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;
            String existingFileName = AudioSavePathInDevice;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://uc-edu.mobile.usilu.net/RecordingActivity.php";

            try {

                //------------------ CLIENT REQUEST
                FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
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
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
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
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }

            //------------------ read the SERVER RESPONSE
            try {

                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {

                    Log.e("Debug", "Server Response " + str);

                }

                inStream.close();

            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }
            return null;
        }
    }  */

    private void doFileUpload() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                //private void doFileUpload(){
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "rn";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String responseFromServer = "";
                Log.d("function", "this is the value of selected path" + AudioSavePathInDevice);
                String urlString = "http://uc-edu.mobile.usilu.net/audio2.php";
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
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + AudioSavePathInDevice + "" + lineEnd);

                    //dos.writeBytes("Content-Disposition: form-data; name="uploadedfile";filename="" + selectedPath + """ + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
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
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                } catch (MalformedURLException ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                } catch (IOException ioe) {
                    Log.e("Debug", "error: " + ioe.getMessage(), ioe);
                }
                //------------------ read the SERVER RESPONSE
                try {
                    inStream = new DataInputStream(conn.getInputStream());
                    String str;

                    while ((str = inStream.readLine()) != null) {
                        Log.e("Debug", "Server Response " + str);
                    }
                    inStream.close();

                } catch (IOException ioex) {
                    Log.e("Debug", "error: " + ioex.getMessage(), ioex);
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
        ActivityCompat.requestPermissions(RecordingActivity2.this, new
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
                        Toast.makeText(RecordingActivity2.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordingActivity2.this,"Permission Denied",Toast.LENGTH_LONG).show();
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
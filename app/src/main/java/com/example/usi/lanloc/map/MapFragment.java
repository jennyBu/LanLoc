package com.example.usi.lanloc.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usi.lanloc.GlobalVars;
import com.example.usi.lanloc.LanLocArrayAdapter;
import com.example.usi.lanloc.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


//added
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;



/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MapFragment extends Fragment implements Observer, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private static View fragView;
    private GoogleMap googleMapInstance;
    LocationManager locationManager;
    public LanLocArrayAdapter mAdapter;
    MediaPlayer mediaPlayer;


    public MapFragment() {
        super();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            try {
                fragView = inflater.inflate(R.layout.map_page, container, false);
            } catch (InflateException ex) {
                // map is already ready
                System.err.println("------- ERR" + ex);
            }

        com.google.android.gms.maps.MapFragment m = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapFragment);

        m.getMapAsync(this);

        return fragView;


        }

    @Override
    public void update(Observable o, Object arg) {

    }

    //    private Location getLastKnownLocation(){
//        /***
//         * TODO: STEP 1 - GET GPS COORDINATES
//         * /**** TODO: STEP 1 -GET GPS COORDINATES*/
//
//
//        LocationManager locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        String locationProvider= LocationManager.GPS_PROVIDER;
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return locationManager.getLastKnownLocation(locationProvider);
//        }
//        else {
//            return null;
//        }

    //return new Location("");
    //}


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMapInstance = googleMap;
//
//        googleMap.clear();
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//            // Animate to current location
//            LocationManager locationManager= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//            //String locationProvider= LocationManager.GPS_PROVIDER;
//            Location currentLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            float zoomLevel= 17;
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), zoomLevel));
//           // googleMapInstance.addMarker(new MarkerOptions().position(LatLng).title("Marker in Sydney"));
//        }
//
//
//    }

    public void onMapReady(GoogleMap googleMap) {

        googleMapInstance = googleMap;


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMapInstance.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l != null) {
            LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());

            googleMapInstance.addMarker(new MarkerOptions().position(ll));
            googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16f));

            //Custom marker added
            //putMarker(ll);
        } else {
            System.out.println("NO PREVIOUS LOCATION");
        }


        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if (output.getClass().equals(JSONArray.class)) {
                    try {
                        JSONArray jsonArray = (JSONArray) output;
                        //JSONObject records[] = new JSONObject[jsonArray.length()];

                        // final int latitude, longitude = JSONObject("latitude");
                        for (int i = 0; i < jsonArray.length(); ++i) {
                            System.out.println("I AM RIGHT HERE in the loop");

                            JSONObject jsn = jsonArray.getJSONObject(i);
                            System.out.println("!!!!!!!!!!!!THIS IS THE DATA BASE ARRAY!!!!!!!! ");
                            //                        System.out.println(Arrays.toString(jsn));

                            final double latitude = jsn.getDouble("latitude");
                            final double longitude = jsn.getDouble("longitude");

                            LatLng lalo = new LatLng(latitude, longitude);
                            System.out.println(lalo);

                            final String path = jsn.getString("audio");

                            putMarker(lalo, path);


                        }


                        // mAdapter = new LanLocArrayAdapter(getActivity(), R.layout.lanloc_list_item, records);
                        // setListAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if (l != null) {
            if (GlobalVars.ALL_USER_MODE) {
                asyncTask.getRecordsAroundPosition(l.getLatitude(), l.getLongitude(), 1000, "date", GlobalVars.ANDROID_ID, false);
            } else if (GlobalVars.SPECIFIC_USER_MODE) {
                asyncTask.getRecordsAroundPosition(l.getLatitude(), l.getLongitude(), 1000, "date", GlobalVars.ANDROID_ID, true);
            }

        } else {
            System.out.println("Can't add recording, no previous location");
        }
    }


//=============================
//LOAD CUSTOM MARKER FUNCTION

    //Puts custom voice record markets
    //public void putMarker(LatLng loc, String title) {
    public void putMarker(LatLng loc, String path) {
        if (googleMapInstance == null) {
            return;
        }
        //customizes the marker
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker))
                .position(loc);
        googleMapInstance.setOnMarkerClickListener(this);
        googleMapInstance.addMarker(options);
        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16f));
        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLng(loc));
        //


//        googleMapInstance.addMarker(new MarkerOptions().position(loc).title(title));
//        googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16f));
//        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final String path = "uploads/IEMJLAudioRecording.3gp";//value.getString("audio").replace("/storage/emulated/0/","");

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        //marker.setImageResource(R.drawable.ic_speaker_orange);
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //marker.setImageResource(R.drawable.ic_speaker);
                        mediaPlayer.reset();
                    }
                });

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    //marker.setImageResource(R.drawable.ic_speaker);
                } else {
                    try {
                        mediaPlayer.setDataSource("http://uc-edu.mobile.usilu.net/" + path);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                }

        return false;
    }
}

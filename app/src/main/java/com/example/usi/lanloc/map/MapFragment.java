package com.example.usi.lanloc.map;

import android.content.Context;
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
import com.example.usi.lanloc.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MapFragment extends Fragment implements Observer, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static View fragView;
    private GoogleMap googleMapInstance;
    LocationManager locationManager;
    MediaPlayer mediaPlayer;
    private HashMap<LatLng, String> paths = new HashMap<>();


    public MapFragment() {
        super();
        this.mediaPlayer = new MediaPlayer();
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
        if (this.isAdded()) {
            createAllMarkers();
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        googleMapInstance = googleMap;

        createAllMarkers();
    }

    private void createAllMarkers() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Delete everything before creating markers for new data set
        googleMapInstance.clear();
        paths.clear();

        googleMapInstance.setMyLocationEnabled(true);

        // Get location
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (l != null) {
            LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());
            googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16f));
        } else {
            System.out.println("NO PREVIOUS LOCATION");
        }

        // Handle data returned from database
        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if (output.getClass().equals(JSONArray.class)) {
                    try {
                        JSONArray jsonArray = (JSONArray) output;

                        for (int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsn = jsonArray.getJSONObject(i);

                            final double latitude = jsn.getDouble("latitude");
                            final double longitude = jsn.getDouble("longitude");

                            LatLng lalo = new LatLng(latitude, longitude);
                            final String path = jsn.getString("audio");

                            if (paths.get(lalo) == null) {
                                paths.put(lalo,path);
                            }
                        }

                        for (LatLng key : paths.keySet()) {
                            putMarker(key);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Get data from database in ALL_USER_MODE or SPECIFIC_USER_MODE
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

    public void putMarker(LatLng loc) {
        if (googleMapInstance == null) {
            return;
        }

        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker))
                .position(loc);

        googleMapInstance.setOnMarkerClickListener(this);
        googleMapInstance.addMarker(options);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        LatLng position = marker.getPosition();
        final String path = paths.get(position).replace("/storage/emulated/0/","");//value.getString("audio").replace("/storage/emulated/0/","");

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker_orange));
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker));

                mediaPlayer.reset();
            }
        });

        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker));
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

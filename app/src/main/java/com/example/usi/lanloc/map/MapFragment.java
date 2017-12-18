package com.example.usi.lanloc.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usi.lanloc.LanLocArrayAdapter;
import com.example.usi.lanloc.R;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


//added
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LOCATION_SERVICE;



/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MapFragment extends Fragment implements Observer, OnMapReadyCallback {


    private static View fragView;
    private GoogleMap googleMapInstance;
    LocationManager locationManager;
    public LanLocArrayAdapter mAdapter;


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
        System.out.println("I AM RIGHT HERE");

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
            googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,16f));

            //Custom marker added
            putMarker(ll, "Here I am");
        } else {
            System.out.println("NO PREVIOUS LOCATION");
        }

        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//        double latitude = location.getLatitude();
//        double longitude = location.getLatitude();
//
//        LatLng  latLng = new LatLng(latitude, longitude);
//        //Instantiate the class, Geocoder
//        //Geocoder geocoder = new Geocoder(getApplicationContext());
//
//
//        //List<Address> addessList = geocoder.getFromLocation(latitude, longitude, 1);
////        String str = addessList.get(0).getLocality()+",";
////        str += addessList.get(0).getCountryName();
//        googleMap.addMarker(new MarkerOptions().position(latLng));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //putMarker(sydney, "Marker in Sydney");
    }
        //TODO I NEED TO GET THE COORDINATES OF EACH VOICE RECORDS FROM THE ARRAY, HOW?
//    private void createListItems() {
//        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
//            @Override
//            public void processFinish(Object output) {
//                if (output.getClass().equals(JSONArray.class)) {
//                    try {
//                        JSONArray jsonArray = (JSONArray) output;
//                        JSONObject records[] = new JSONObject[jsonArray.length()];
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            records[i] = jsonArray.getJSONObject(i);
//                        }
//
//                        mAdapter = new LanLocArrayAdapter(getActivity(), R.layout.lanloc_list_item, records);
//                        setListAdapter(mAdapter);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });






    public void loadMarkers(LatLng currentLoc) {
        // you call db provider to get you list of locations
    }

    //Puts custom voice record markets
    public void putMarker(LatLng loc, String title) {
        if (googleMapInstance == null) {
            return;
        }
        //customizes the marker
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker))
                .position(loc).title(title);

        googleMapInstance.addMarker(options);
        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16f));
        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLng(loc));
        //


//        googleMapInstance.addMarker(new MarkerOptions().position(loc).title(title));
//        googleMapInstance.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16f));
//        //googleMapInstance.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }



}

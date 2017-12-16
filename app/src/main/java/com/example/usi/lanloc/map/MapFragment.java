package com.example.usi.lanloc.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.usi.lanloc.R;
import java.util.Observable;
import java.util.Observer;


//added
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import static android.content.Context.LOCATION_SERVICE;



/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MapFragment extends Fragment implements Observer, OnMapReadyCallback {


    private static View fragView;
    private GoogleMap googleMapInstance;
    //LocationManager locationManager;


    public MapFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.map_page, container, false);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMapInstance.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMapInstance.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }






}

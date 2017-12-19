package com.example.usi.lanloc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MostPopularFragment extends ListFragment implements Observer {
    public LanLocArrayAdapter mAdapter;

    public MostPopularFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View fragView = inflater.inflate(R.layout.most_popular_page, container, false);

        createListItems();

        return fragView;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (this.isAdded()) {
            createListItems();
        }
    }

    private void createListItems() {
        // Transform data from database to be able to be passed to LanLocArrayAdapter
        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                if (output.getClass().equals(JSONArray.class)) {
                    try {
                        JSONArray jsonArray = (JSONArray) output;
                        JSONObject records[] = new JSONObject[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            records[i] = jsonArray.getJSONObject(i);
                        }

                        mAdapter = new LanLocArrayAdapter(getActivity(), R.layout.lanloc_list_item, records);
                        setListAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Get data from database in ALL_USER_MODE or SPECIFIC_USER_MODE
        if (l != null) {
            if (GlobalVars.ALL_USER_MODE) {
                asyncTask.getRecordsAroundPosition(l.getLatitude(), l.getLongitude(), 1000, "default", GlobalVars.ANDROID_ID, false);
            } else if (GlobalVars.SPECIFIC_USER_MODE) {
                asyncTask.getRecordsAroundPosition(l.getLatitude(), l.getLongitude(), 1000, "default", GlobalVars.ANDROID_ID, true);
            } else {
                System.out.println("Can't add recording, no previous location");
            }
        }
    }
}

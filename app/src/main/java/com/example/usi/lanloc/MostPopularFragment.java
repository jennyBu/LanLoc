package com.example.usi.lanloc;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
        createListItems();
    }

    private void createListItems() {
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

        // TODO pass here real position values
        if (GlobalVars.ALL_USER_MODE) {
            asyncTask.getRecordsAroundPosition(46.010475, 8.957006, 1000, "default", GlobalVars.ANDROID_ID, false);
        } else if (GlobalVars.SPECIFIC_USER_MODE) {
            asyncTask.getRecordsAroundPosition(46.010475, 8.957006, 1000, "default", GlobalVars.ANDROID_ID, true);
        }
    }
}

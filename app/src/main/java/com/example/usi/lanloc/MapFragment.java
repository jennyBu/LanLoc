package com.example.usi.lanloc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MapFragment extends Fragment {

    public MapFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.map_page, container, false);
        return fragView;
    }
}

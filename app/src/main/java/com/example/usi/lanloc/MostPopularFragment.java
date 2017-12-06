package com.example.usi.lanloc;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Jennifer Busta on 06.12.17.
 */

public class MostPopularFragment extends ListFragment {

    String data[] = new String[] { "one", "two", "three", "four" };
    public ArrayAdapter<String> mAdapter;

    public MostPopularFragment() {
        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fragView = inflater.inflate(R.layout.most_popular_page, container, false);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        setListAdapter(mAdapter);

        return fragView;
    }
}

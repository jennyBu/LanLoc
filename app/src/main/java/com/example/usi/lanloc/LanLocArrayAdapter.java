package com.example.usi.lanloc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Jennifer Busta on 07.12.17.
 */

public class LanLocArrayAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final JSONObject[] values;

    public LanLocArrayAdapter(Context context, int lanloc_list_item, JSONObject[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lanloc_list_item, parent, false);

        try {
            String upVotes = values[position].getString("up_votes");
            String downVotes = values[position].getString("down_votes");
            String path = values[position].getString("audio");
            String dateTime = values[position].getString("date_time");

            TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
            dateTextView.setText(dateTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
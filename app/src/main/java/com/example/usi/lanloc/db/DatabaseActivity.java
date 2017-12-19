package com.example.usi.lanloc.db;

import android.os.AsyncTask;

import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * Created by Jennifer Busta on 01.12.17.
 */
public class DatabaseActivity extends AsyncTask<Object, Object, Object> {
    public AsyncResponse delegate = null;

    public DatabaseActivity(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected void onPostExecute(Object result) {
        delegate.processFinish(result);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String link = "http://uc-edu.mobile.usilu.net/lanloc.php";
            String data = (String)objects[0];

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return new JSONArray(line);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param latitude of the current position
     * @param longitude of the current position
     * @param distance to the current position in meters
     * @param order in which the results are sorted (type "default" for sorting by popularity and "date" for sorting by date)
     * @return all records in the range of the distance around the current position
     */
    public Object getRecordsAroundPosition(Double latitude, Double longitude, Integer distance, String order, String android_id, Boolean filterByUser) {
        DecimalFormat df = new DecimalFormat("####.######");
        String lat = df.format(latitude);
        String lon = df.format(longitude);
        String dist = distance.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getRecordsAroundPosition", "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");
            data += "&" + URLEncoder.encode("distance", "UTF-8") + "=" + URLEncoder.encode(dist, "UTF-8");
            data += "&" + URLEncoder.encode("order", "UTF-8") + "=" + URLEncoder.encode(order, "UTF-8");
            data += "&" + URLEncoder.encode("android_id", "UTF-8") + "=" + URLEncoder.encode(android_id, "UTF-8");
            if (filterByUser) {
                //String android_id = Secure.getString(getContext().getContentResolver(),Secure.ANDROID_ID);
                data += "&" + URLEncoder.encode("filterByUser", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
            }

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param androidId of the user uploading a record
     * @param latitude of the users' position
     * @param longitude of the users' position
     * @param path to the audio file on the server
     */
    public void addRecord(String androidId, Double latitude, Double longitude, String path) {
        DecimalFormat df = new DecimalFormat("####.######");
        String lat = df.format(latitude);
        String lon = df.format(longitude);

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("addRecord", "UTF-8");
            data += "&" + URLEncoder.encode("android_id", "UTF-8") + "=" + URLEncoder.encode(androidId, "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");
            data += "&" + URLEncoder.encode("audio", "UTF-8") + "=" + URLEncoder.encode(path, "UTF-8");

            execute(data);
        } catch (Exception e) {
            //return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param recordId id of the record that should get an upvote
     */
    public void voteRecordUp(Integer recordId, String androidId) {
        String record = recordId.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("voteRecordUp", "UTF-8");
            data += "&" + URLEncoder.encode("record", "UTF-8") + "=" + URLEncoder.encode(record, "UTF-8");
            data += "&" + URLEncoder.encode("android_id", "UTF-8") + "=" + URLEncoder.encode(androidId, "UTF-8");

            execute(data);
        } catch (Exception e) {
            //return new String("Exception: " + e.getMessage());
        }
    }

    /**
     * @param recordId id of the record that should get a downvote
     */
    public void voteRecordDown(Integer recordId, String androidId) {
        String record = recordId.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("voteRecordDown", "UTF-8");
            data += "&" + URLEncoder.encode("record", "UTF-8") + "=" + URLEncoder.encode(record, "UTF-8");
            data += "&" + URLEncoder.encode("android_id", "UTF-8") + "=" + URLEncoder.encode(androidId, "UTF-8");

            execute(data);
        } catch (Exception e) {
            //return new String("Exception: " + e.getMessage());
        }
    }
}

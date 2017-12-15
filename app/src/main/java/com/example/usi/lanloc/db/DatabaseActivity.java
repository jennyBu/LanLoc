package com.example.usi.lanloc.db;

import android.os.AsyncTask;
import android.provider.Settings;

import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

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
     * @param androidId the device id of the phone
     * @return the user id. If not already in database it is added and then returned.
     */
    public Object getUserByAndroidId(String androidId) {
        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getUserByAndroidId", "UTF-8");
            data += "&" + URLEncoder.encode("android_id", "UTF-8") + "=" + URLEncoder.encode(androidId, "UTF-8");

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param latitude of the position to get
     * @param longitude of the position to get
     * @return the id of the position. If not already in database it is added and then returned.
     */
    public Object getPositionId(Double latitude, Double longitude) {
        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getPositionId", "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param id of the position
     * @return latitude and longitude of the position
     */
    public Object getPositionById(Integer id) {
        String positionId = id.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getPositionById", "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(positionId, "UTF-8");

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    /**
     *
     * @param latitude of the current position
     * @param longitude of the current position
     * @param distance to the current position in meters
     * @return all positions in the range of the distance around the current position
     */
    public Object getAllPositionsInRange(Double latitude, Double longitude, Integer distance) {
        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);
        String dist = distance.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getAllPositionsInRange", "UTF-8");
            data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8");
            data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8");
            data += "&" + URLEncoder.encode("distance", "UTF-8") + "=" + URLEncoder.encode(dist, "UTF-8");

            return execute(data);
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
        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);
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
     * @param positions that are searched for
     * @return records for all mentioned positions
     */
    public Object getRecordsForPositions(List<Integer> positions) {
        String formattedPositions = formatPositions(positions);

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getRecordsForPositions", "UTF-8");
            data += "&" + URLEncoder.encode("positions", "UTF-8") + "=" + URLEncoder.encode(formattedPositions, "UTF-8");

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    private String formatPositions(List<Integer> positions) {
        String result = positions.toString();
        result = result.replace("[", "(");
        result = result.replace("]", ")");

        return result;
    }

    // TODO for the following function no return (should doInBackground be adapted?)

    /**
     *
     * @param userId foreign key for user (id column in user table)
     * @param positionId foreign key for position (id column in positions table)
     * @param path path to the RecordingActivity file on file server
     */
    public void addRecord(Integer userId, Integer positionId, String path) {
        String user = userId.toString();
        String position = positionId.toString();

        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("addRecord", "UTF-8");
            data += "&" + URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("position", "UTF-8") + "=" + URLEncoder.encode(position, "UTF-8");
            data += "&" + URLEncoder.encode("RecordingActivity", "UTF-8") + "=" + URLEncoder.encode(path, "UTF-8");

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

    /*public Object getUserById(String id) {
        try {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getUserByAndroidId", "UTF-8");
            data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

            return execute(data);
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }*/

    /*

    User posts something:
    - check if android id is already in database and get user id for it         DONE
    - check if position is already in database and get position id for it       DONE
    - create new record using user id and position id                           DONE
    - increase upvotes and downvotes                                            DONE

    User wants to see and play records around him:
    - get all position ids from db in a specific range                          DONE
    - get all records for position ids                                          DONE

     */
}

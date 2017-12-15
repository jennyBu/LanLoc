package com.example.usi.lanloc;

import android.content.Context;
import android.provider.Settings;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usi.lanloc.audio.RecordingActivity2;
import com.example.usi.lanloc.db.AsyncResponse;
import com.example.usi.lanloc.db.DatabaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jennifer Busta on 07.12.17.
 */

public class LanLocArrayAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;
    private final JSONObject[] values;
    // TODO make androidId global
    String androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

    MediaPlayer mediaPlayer ;

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
            JSONObject value = values[position];
            Integer id = value.getInt("id");

            handleUpVotes(value, rowView, id);
            handleDownVotes(value, rowView, id);
            handleDateTime(value, rowView);
            handleAudio(value, rowView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }

    private void handleDownVotes(final JSONObject value, final View rowView, final Integer id) throws JSONException {
        final String downVotes = value.getString("down_votes");
        final int isVotable = value.getInt("isVotable");
        final TextView downVoteTextView = (TextView) rowView.findViewById(R.id.downVotes);
        downVoteTextView.setText(downVotes);

        ImageView downVoteImageView = (ImageView) rowView.findViewById(R.id.iconDownVotes);
        if (isVotable == 1) {
            downVoteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                        }
                    });
                    asyncTask.voteRecordDown(id, androidId);

                    GlobalVars.COLLECTION_PAGER_ADAPTER.updateFragments();

                    disableDownVote(rowView);
                    disableUpVote(rowView);

                    Integer downVotesCount = Integer.parseInt(downVotes);
                    downVotesCount++;
                    downVoteTextView.setText(downVotesCount.toString());
                }
            });
        } else {
            disableDownVote(rowView);
        }
    }

    private void disableDownVote(View rowView) {
        final ImageView downVoteIcon = (ImageView) rowView.findViewById(R.id.iconDownVotes);
        downVoteIcon.setImageResource(R.drawable.ic_down_vote_grey);
    }

    private void handleUpVotes(JSONObject value, final View rowView, final Integer id) throws JSONException {
        final String upVotes = value.getString("up_votes");
        final int isVotable = value.getInt("isVotable");
        final TextView upVoteTextView = (TextView) rowView.findViewById(R.id.upVotes);
        upVoteTextView.setText(upVotes);

        ImageView upVoteImageView = (ImageView) rowView.findViewById(R.id.iconUpVotes);
        if (isVotable == 1) {
            upVoteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
                        @Override
                        public void processFinish(Object output) {
                        }
                    });

                    asyncTask.voteRecordUp(id, androidId);

                    GlobalVars.COLLECTION_PAGER_ADAPTER.updateFragments();

                    disableUpVote(rowView);
                    disableDownVote(rowView);

                    Integer upVotesCount = Integer.parseInt(upVotes);
                    upVotesCount++;
                    upVoteTextView.setText(upVotesCount.toString());
                }
            });
        } else {
            disableUpVote(rowView);
        }
    }

    private void disableUpVote(View rowView) {
        final ImageView downVoteIcon = (ImageView) rowView.findViewById(R.id.iconUpVotes);
        downVoteIcon.setImageResource(R.drawable.ic_up_vote_grey);
    }

    private void handleDateTime(JSONObject value, View rowView) throws JSONException {
        String dateTime = value.getString("date_time");
        TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
        dateTextView.setText(getDateTimeString(dateTime));
    }

    private String getDateTimeString(String dateTime) {
        Integer itemYear = Integer.valueOf(dateTime.substring(0, 4));
        Integer itemMonth = Integer.valueOf(dateTime.substring(5, 7));
        Integer itemDay = Integer.valueOf(dateTime.substring(8, 10));
        Integer itemHour = Integer.valueOf(dateTime.substring(11,13));
        Integer itemMinute = Integer.valueOf(dateTime.substring(14,16));

        String now = new Timestamp(new Date().getTime()).toString();
        Integer nowYear = Integer.valueOf(now.substring(0, 4));
        Integer nowMonth = Integer.valueOf(now.substring(5, 7));
        Integer nowDay = Integer.valueOf(now.substring(8, 10));
        Integer nowHour = Integer.valueOf(now.substring(11,13));
        Integer nowMinute = Integer.valueOf(now.substring(14,16));

        Integer yearDifference = nowYear - itemYear;
        if (yearDifference > 0) {
            if (yearDifference == 1) {
                return "1 year ago";
            } else {
                return yearDifference + " years ago";
            }
        }

        Integer monthDifference = nowMonth - itemMonth;
        if (monthDifference > 0) {
            if (monthDifference == 1) {
                return "1 month ago";
            } else {
                return yearDifference + " months ago";
            }
        }

        Integer dayDifference = nowDay - itemDay;
        if (dayDifference > 0) {
            if (dayDifference == 1) {
                return "1 day ago";
            } else if (dayDifference > 1 && dayDifference < 7) {
                return dayDifference + " days ago";
            } else if (dayDifference > 7 && dayDifference < 14) {
                return "1 week ago";
            } else if (dayDifference > 14 && dayDifference < 21) {
                return "2 weeks ago";
            } else if (dayDifference > 21 && dayDifference < 28) {
                return "3 weeks ago";
            } else if (dayDifference > 28) {
                return "4 weeks ago";
            }
        }

        Integer hourDifference = nowHour - itemHour;
        if (hourDifference > 0) {
            if (hourDifference == 1) {
                return "1 hour ago";
            } else {
                return hourDifference + " hours ago";
            }
        }

        Integer minuteDifference = nowMinute - itemMinute;
        if (minuteDifference > 0) {
            if (minuteDifference == 1) {
                return "1 minute ago";
            } else {
                return minuteDifference + " minutes ago";
            }
        }

        return "now";
    }

    private void handleAudio(JSONObject value, View rowView) throws JSONException {
        final String path = value.getString("audio");
        ImageView speakerView = (ImageView) rowView.findViewById(R.id.icon);

        speakerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource("http://uc-edu.mobile.usilu.net/" + path);
                  //     mediaPlayer.setDataSource("http://uc-edu.mobile.usilu.net/uploads/JBMPEAudioRecording.3gp");

                    // TO MODIFY WITH PROPER PATH
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();


         /*       Integer upVotesCount = Integer.parseInt(upVotes);
                upVotesCount++;
                upVoteTextView.setText(upVotesCount.toString()); */

        /*        DatabaseActivity asyncTask = new DatabaseActivity(new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) { }
                });
                asyncTask.voteRecordUp(id); */
            }
        });


        // TODO on click listener + possibility to play back
    }
}
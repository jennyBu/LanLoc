package com.example.usi.lanloc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jennifer Busta on 20.11.17.
 */
public class WelcomeActivity extends Activity {
    private final int WELCOME_DISPLAY_LENGTH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        }, WELCOME_DISPLAY_LENGTH);
    }
}

package com.parse.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public void getStarted(View view){
        Switch userTypeSwitch = findViewById(R.id.switch1);
        Log.i("Switch value", String.valueOf(userTypeSwitch.isChecked()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() == null){
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null){
                        Log.i("Login", "Success");
                    } else {
                        Log.i("Login", e.getMessage());
                    }
                }
            });
        }

        // Enable Local Datastore.


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

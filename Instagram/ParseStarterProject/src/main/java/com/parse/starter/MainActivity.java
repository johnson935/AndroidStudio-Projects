/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView changeSignUpMode;
  EditText passwordEditText;


  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signUp(view);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.signupTextView) {
      Button signUpbutton = (Button) findViewById(R.id.signIn);

      if (signUpModeActive) {
        signUpModeActive = false;
        signUpbutton.setText("Log In");
        changeSignUpMode.setText("Or, Sign Up");

      } else {
        signUpModeActive = true;
        signUpbutton.setText("Sign Up");
        changeSignUpMode.setText("Or, Log In");
      }
    } else if (view.getId() == R.id.backgroundRelativeLayout){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }
  }

  public void signUp(View view){
    EditText username = (EditText)findViewById(R.id.username);

    if (username.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
      Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();

    } else{

      if (signUpModeActive){
      ParseUser user = new ParseUser();

      user.setUsername(username.getText().toString());
      user.setPassword(passwordEditText.getText().toString());

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null){
            Log.i("Signup", "Successful");
          } else{
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
          }
        }
      });
      } else {
        ParseUser.logInInBackground(username.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null){
              Log.i("Log In", "Successful");
            } else{
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            }
          }
        });
      }
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    changeSignUpMode = (TextView) findViewById(R.id.signupTextView);

    changeSignUpMode.setOnClickListener(this);

    passwordEditText = (EditText) findViewById(R.id.password);

    passwordEditText.setOnKeyListener(this);

    RelativeLayout backgroundRelativeLayout = (RelativeLayout) findViewById(R.id.backgroundRelativeLayout);
    backgroundRelativeLayout.setOnClickListener(this);
    /*ParseUser.logOut();
    if (ParseUser.getCurrentUser() != null){
      Log.i("currentUser", ParseUser.getCurrentUser().getUsername());
    } else {
      Log.i("currentUser", "User not logged in");
    }

    /*ParseUser.logInInBackground("johnson", "password", new LogInCallback() {
      @Override
      public void done(ParseUser parseUser, ParseException e) {
        if (parseUser != null){
          Log.i("Logged in", "Success");
        } else {
          Log.i("Logged in", "Failed" + e.toString());
        }
      }
    });
    /*
    ParseUser user = new ParseUser();
    user.setUsername("johnson");
    user.setPassword("password");

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null){
          Log.i("Signup", "Success");

        } else{
          Log.i("Signup", "Failed");
        }
      }
    });
    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.whereGreaterThan("score", 100);

    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> list, ParseException e) {
        if (e == null){
          Log.i("findInBackground", "Retrived" + list.size() + "objects");

          if (list.size() > 0){
            for (ParseObject object : list){
              object.put("score", object.getInt("score") + 50);
              object.saveInBackground();
              Log.i("findInBackgroundResult", object.getString("username"));
            }
          }
        }
      }
    });
    /*
    ParseObject score = new ParseObject("Score");
    score.put("username", "johnson");
    score.put("score", 86);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null){
          Log.i("Saving", "Success");
        } else{
          Log.i("Saveing", "Failed");
        }
      }
    });*/

    /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

    query.getInBackground("18Fl9CugBd", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        if (e == null && parseObject != null){

          parseObject.put("score", 200);
          parseObject.saveInBackground();
          Log.i("Value", parseObject.getString("username"));
          Log.i("Value", Integer.toString(parseObject.getInt("score")));
        }
      }
    });

    ParseObject tweet = new ParseObject("Tweet");
    tweet.put("username", "tommy");
    tweet.put("tweet", "Hi there");

    tweet.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null){
          Log.i("tweet", "Successful");
        } else {
          Log.i("tweet", "Failed");
        }
      }
    });

    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Tweet");

    query2.getInBackground("GMLJf5mlEH", new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        if (e == null && parseObject != null){
          Log.i("tweet", "successful");
          parseObject.put("tweet", "Bye!");
          parseObject.saveInBackground();
        } else{
          Log.i("tweet", "Failed");
        }
      }
    });*/

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}

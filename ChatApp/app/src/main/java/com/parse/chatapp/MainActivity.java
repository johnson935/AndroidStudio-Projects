package com.parse.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Boolean loginModeActivity = false;

    public void redirectLoggedIn(){
        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(intent);
        }
    }

    public void toggleLoginMode(View view){

        Button loginSignUpButton = findViewById(R.id.loginSignUpButton);
        TextView toggleLoginTextView = findViewById(R.id.toggleLoginModeTextView);

        if (loginModeActivity) {
            loginModeActivity = false;
            loginSignUpButton.setText("Sign Up");
            toggleLoginTextView.setText("Or, Login");
        } else {
            loginModeActivity = true;
            loginSignUpButton.setText("Login");
            toggleLoginTextView.setText("Or, SignUp");
        }
    }

    public void signupLogin(View view) {

        EditText usernameEdit = findViewById(R.id.usernameEditText);

        EditText passwordEdit = findViewById(R.id.passwordEditText);

        if (loginModeActivity) {
            ParseUser.logInInBackground(usernameEdit.getText().toString(), passwordEdit.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null){
                        Log.i("info", "user logged in");
                        redirectLoggedIn();
                    } else {
                        String message = e.getMessage();
                        if (message.toLowerCase().contains("java")){
                            message = e.getMessage().substring(e.getMessage().indexOf(" "));
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            ParseUser user = new ParseUser();
            user.setUsername(usernameEdit.getText().toString());
            user.setPassword(passwordEdit.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Info", "user signed up");
                        redirectLoggedIn();
                    } else {
                        String message = e.getMessage();
                        if (message.toLowerCase().contains("java")){
                            message = e.getMessage().substring(e.getMessage().indexOf(" "));
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("WhatsApp Login");
        redirectLoggedIn();
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

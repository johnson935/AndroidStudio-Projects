package com.example.login_text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void loginClick (View loginButton){
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        Log.i("Info", username.getText().toString() + " " + password.getText().toString());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

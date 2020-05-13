package com.parse.chatapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //ParseUser.enableAutomaticUser();
        //ParseACL defaultACL = new ParseACL();
        //defaultACL.setPublicReadAccess(true);
        //defaultACL.setPublicWriteAccess(true);
        //ParseACL.setDefaultACL(defaultACL, true);

    }
}
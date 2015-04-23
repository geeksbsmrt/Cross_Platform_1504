package com.adamcrawford.ejuicer;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParsePush;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    EJuicer
 * Purpose: TODO Minimum 2 sentence description
 */
public class EJuicer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.initialize(this, getString(R.string.parseAppId), getString(R.string.parseClientKey));
        ParsePush.subscribeInBackground("");
    }
}

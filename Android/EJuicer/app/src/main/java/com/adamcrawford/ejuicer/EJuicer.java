package com.adamcrawford.ejuicer;

import android.app.Application;

import com.parse.Parse;

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
        Parse.initialize(this, getString(R.string.parseAppId), getString(R.string.parseClientKey));
    }
}

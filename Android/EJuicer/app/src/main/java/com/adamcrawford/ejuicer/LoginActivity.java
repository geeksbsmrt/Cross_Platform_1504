package com.adamcrawford.ejuicer;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    LoginActivity
 * Purpose: TODO Minimum 2 sentence description
 */
public class LoginActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<MainActivity> getTargetClass() {
        return MainActivity.class;
    }

}

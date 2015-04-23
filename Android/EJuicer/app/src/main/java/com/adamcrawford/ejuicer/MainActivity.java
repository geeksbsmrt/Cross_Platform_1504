package com.adamcrawford.ejuicer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    MainActivity
 * Purpose: TODO Minimum 2 sentence description
 */
public class MainActivity extends Activity {

    public static ActionBar actionBar;
    public static Context mContext;
    public static Menu mainMenu;
    public static FragmentManager fragMan;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        actionBar = getActionBar();
        mContext = getApplicationContext();
        fragMan = getFragmentManager();

        ParseObject.registerSubclass(JuiceItem.class);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();

        if (savedInstanceState == null) {
            Fragment_JuiceList fjl = new Fragment_JuiceList();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fjl, "JuiceListFrag")
                    .commit();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add: {
                Fragment_NewJuice fnj = new Fragment_NewJuice();
                getFragmentManager().beginTransaction().replace(R.id.container, fnj).addToBackStack(null).commit();
                return false;
            }
            case R.id.action_logout: {
                ParseUser.logOut();
                // FLAG_ACTIVITY_CLEAR_TASK only works on API 11, so if the user
                // logs out on older devices, we'll just exit.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }
                return false;
            }
            case android.R.id.home: {
                getFragmentManager().popBackStack();
                return false;
            }
            default: {
                return false;
            }
        }
    }
}

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    Timer netTimer;
    TimerTask monitorNet;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveEventually();


        if (savedInstanceState == null) {
            Fragment_JuiceList fjl = new Fragment_JuiceList();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fjl, "JuiceListFrag")
                    .commit();
        }
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
                Fragment_JuiceList fjl = (Fragment_JuiceList) MainActivity.fragMan.findFragmentByTag("JuiceListFrag");
                if (fjl != null && fjl.isVisible()) {
                    fnj.setTargetFragment(fjl, 0);
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (netTimer != null) {
            netTimer.cancel();
        }
        netTimer = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        netTimer = new Timer();
        setTimerTask();
        netTimer.schedule(monitorNet, 0, 15000);
    }

    private void setTimerTask() {
        monitorNet = new TimerTask() {
            @Override
            public void run() {
                Log.i("MA", "Running");
                if (isConnected(mContext)) {
                    ParseQuery<JuiceItem> juices;
                    try {
                        juices = JuiceItem.getQuery();
                        juices.fromPin();
                        juices.findInBackground(new FindCallback<JuiceItem>() {
                            @Override
                            public void done(List<JuiceItem> list, ParseException e) {
                                ParseObject.saveAllInBackground(list);
                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}

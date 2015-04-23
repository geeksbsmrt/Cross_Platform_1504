package com.adamcrawford.ejuicer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Author:  Adam Crawford
 * Project: EJuicer
 * Package: com.adamcrawford.ejuicer
 * File:    CustomReceiver
 * Purpose: TODO Minimum 2 sentence description
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.i("CR", "Push Received");
        Fragment_JuiceList fjl = (Fragment_JuiceList) MainActivity.fragMan.findFragmentByTag("JuiceListFrag");
        if (fjl != null && fjl.isVisible()) {
            Log.i("CR", "FJL Visible");
            fjl.juiceAdapter.loadObjects();
        }
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }
}

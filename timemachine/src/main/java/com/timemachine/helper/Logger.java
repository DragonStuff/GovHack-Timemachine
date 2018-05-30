package com.timemachine.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import com.timemachine.R;

/**
 * Created by Jeri on 4/17/2014.
 */
public class Logger{

    public static void showSimpleErrorDialog(Activity context, String title, String errorMessage) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(errorMessage)
                .setTitle(title);
        alertBuilder.setPositiveButton("OK", null);
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public static final boolean isDebug = true;

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if(isDebug)
            Log.d(tag, msg);
    }
}
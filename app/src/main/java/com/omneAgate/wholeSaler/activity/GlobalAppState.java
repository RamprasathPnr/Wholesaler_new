package com.omneAgate.wholeSaler.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
//import android.support.multidex.MultiDex;
import android.util.Log;

import com.omneAgate.wholeSaler.Util.FontsOverride;
import com.omneAgate.wholeSaler.Util.Queue;

import lombok.Data;


/**
 * Used to create application variable all over application
 */
@Data
public class GlobalAppState extends Application {

    //Check logging enabled
    public static boolean isLoggingEnabled = false;

    //language of user
    public static String language;

    public static boolean localLogin = false;

    public static boolean smsAvailable = true;

    private static GlobalAppState sInstance;

    public static String serverUrl;

    //Queue used for log in entire application
    public final Queue queue = new Queue();

    //ReferenceId
    public String refId;

    public synchronized static GlobalAppState getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FontsOverride.setDefaultFont(this, "monospace", "fonts/Bamini.ttf");
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });


    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        Log.e("Error", e.toString(), e);
        Intent intent = new Intent();
        intent.setAction("com.omneAgate.SEND_LOG"); // see step 5.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

        System.exit(1); // kill off the crashed app
    }
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

}

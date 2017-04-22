package com.stiletto.tr.app;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by yana on 22.04.17.
 */

public class TRApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Don't do this! This is just so cold launches take some time
//        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }
}
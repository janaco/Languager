package com.stiletto.tr.app;

import android.app.Application;
import android.os.SystemClock;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.TimeUnit;

/**
 * Created by yana on 22.04.17.
 */

public class TRApp extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "JDZzBo4OULyNXhDt3zIVDazAS";
    private static final String TWITTER_SECRET = "nCU54PWoDbj5rtk9RCAGz2unyS8o0H2fCA8r0chxFvFuyzCTMB";


    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));

        // Don't do this! This is just so cold launches take some time
//        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }
}
package org.supremus.sych.sychnews;

import android.app.Application;
import android.content.Context;

public class SychApp extends Application {

    public static Context SYCHCONTEXT;
    @Override
    public void onCreate() {
        super.onCreate();
        SYCHCONTEXT = getApplicationContext();
    }
}

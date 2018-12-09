package org.supremus.sych.sychnews;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class SychApp extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context SYCHCONTEXT;
    @Override
    public void onCreate() {
        super.onCreate();
        SYCHCONTEXT = getApplicationContext();
        PeriodicWorkRequest.Builder updateNewsBuilder =
                new PeriodicWorkRequest.Builder(UpdateWork.class, 3, TimeUnit.HOURS);
        PeriodicWorkRequest updateNewsReq = updateNewsBuilder.build();
        WorkManager.getInstance().enqueue(updateNewsReq);
    }
}

package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.supremus.sych.sychnews.NewsItemProvider;
import org.supremus.sych.sychnews.UIUpdater;

import java.lang.ref.WeakReference;

public class ShowItemTask extends AsyncTask<Object,Void,Void> {

    private final WeakReference<Activity> wAct;

    public ShowItemTask(Activity activity) {
        wAct = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        fillUI();
        return null;
    }

    private void fillUI() {
        Activity activity = wAct.get();
        if (activity==null) return;
        NewsItemProvider nip = (NewsItemProvider) activity;
        while (nip.getItem()==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fillUI();
        }
        UIUpdater updater = (UIUpdater) activity;
        activity.runOnUiThread(() -> updater.updateUI(nip.getItem()));
    }
}

package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.supremus.sych.sychnews.NewsItemProvider;
import org.supremus.sych.sychnews.UIUpdater;

public class ShowItemTask extends AsyncTask<Object,Void,Void> {

    private NewsItemProvider nip;
    private UIUpdater updater;
    private Activity activity;

    public ShowItemTask(Activity activity) {
        this.activity = activity;
        this.nip = (NewsItemProvider) activity;
        this.updater = (UIUpdater) activity;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        fillUI();
        return null;
    }

    private void fillUI() {
        while (nip.getItem()==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fillUI();
        }
        activity.runOnUiThread(() -> updater.updateUI(nip.getItem()));
    }
}

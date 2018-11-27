package org.supremus.sych.sychnews.tasks;

import android.os.AsyncTask;

import org.supremus.sych.sychnews.interfaces.NewsItemProvider;
import org.supremus.sych.sychnews.interfaces.UIUpdater;
import org.supremus.sych.sychnews.network.NYTApi;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;

public class ShowItemTask extends AsyncTask<Object,Void,Void> {

    private final WeakReference<Fragment> wFrag;

    public ShowItemTask(Fragment fragment) {
        wFrag = new WeakReference<>(fragment);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        fillUI();
        return null;
    }

    private void fillUI() {
        while (NYTApi.getSelectedItem()==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fillUI();
        }
        Fragment fragment = wFrag.get();
        if (fragment==null) return;
        UIUpdater updater = (UIUpdater) fragment;
        fragment.getActivity().runOnUiThread(() -> updater.updateUI(NYTApi.getSelectedItem()));
    }
}

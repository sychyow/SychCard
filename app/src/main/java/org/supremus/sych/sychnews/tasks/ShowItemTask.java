package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.supremus.sych.sychnews.NewsItemProvider;
import org.supremus.sych.sychnews.UIUpdater;

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
        Fragment fragment = wFrag.get();
        if (fragment==null) return;
        NewsItemProvider nip = (NewsItemProvider) fragment;
        while (nip.getItem()==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fillUI();
        }
        UIUpdater updater = (UIUpdater) fragment;
        fragment.getActivity().runOnUiThread(() -> updater.updateUI(nip.getItem()));
    }
}

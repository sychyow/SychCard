package org.supremus.sych.sychnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.supremus.sych.sychnews.MainActivity;
import org.supremus.sych.sychnews.R;
import org.supremus.sych.sychnews.interfaces.ModeSetter;
import org.supremus.sych.sychnews.tasks.ShowItemTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState==null) {
            ModeSetter modeSetter = (ModeSetter) getActivity();
            modeSetter.setMode(MainActivity.MODE_SHOW);
        }
        return inflater.inflate(R.layout.fragment_news_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new ShowItemTask(getParentFragment()).execute();
    }



}


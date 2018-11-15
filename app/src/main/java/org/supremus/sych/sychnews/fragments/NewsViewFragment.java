package org.supremus.sych.sychnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.supremus.sych.sychnews.GetItemTask;
import org.supremus.sych.sychnews.NewsDetailActivity;
import org.supremus.sych.sychnews.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class NewsViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_view, container, false);
               return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentActivity nda = getActivity();
        int newsId = nda.getIntent().getIntExtra(NewsDetailActivity.EXTRA_ID, 0);
        new GetItemTask(nda, newsId).execute();
    }

}


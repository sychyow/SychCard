package org.supremus.sych.sychnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.supremus.sych.sychnews.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class IntroFragments extends Fragment {
    private int sourceId;

    public static IntroFragments newInstance(int resId) {

        Bundle args = new Bundle();
        args.putInt("src", resId);
        IntroFragments fragment = new IntroFragments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourceId = getArguments().getInt("src");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro, container, false);
        ImageView logo = v.findViewById(R.id.image_logo);
        logo.setImageResource(sourceId);
        return v;
    }
}

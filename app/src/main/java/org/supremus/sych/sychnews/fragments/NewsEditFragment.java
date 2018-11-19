package org.supremus.sych.sychnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.supremus.sych.sychnews.NewsItemProvider;
import org.supremus.sych.sychnews.R;
import org.supremus.sych.sychnews.data.NewsItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsEditFragment extends Fragment {

    NewsItemProvider nip;
    NewsItem baseItem;
    EditText etTitle;
    EditText etShortText;
    EditText etFullText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_edit, container, false);
        nip = (NewsItemProvider) getActivity();
        loadComponents(view);
        setupUI();
        return view;
    }

    private void loadComponents(View view) {
        etTitle = view.findViewById(R.id.et_title);
        etShortText = view.findViewById(R.id.et_short_text);
        etFullText = view.findViewById(R.id.et_full_text);
    }

    private void setupUI() {
        baseItem = nip.getItem();
        etTitle.setText(baseItem.getTitle());
        etShortText.setText(baseItem.getPreviewText());
        etFullText.setText(baseItem.getFullText());
    }

    public NewsItem getNews() {
        return  new NewsItem(etTitle.getText().toString(),
                baseItem.getImageUrl(),
                baseItem.getCategory(),
                baseItem.getPublishDate(),
                etShortText.getText().toString(),
                etFullText.getText().toString());
    }
}

package org.supremus.sych.sychnews.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.supremus.sych.sychnews.MainActivity;
import org.supremus.sych.sychnews.interfaces.ModeSetter;
import org.supremus.sych.sychnews.interfaces.NewsItemProvider;
import org.supremus.sych.sychnews.R;
import org.supremus.sych.sychnews.interfaces.UIUpdater;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.tasks.DelItemTask;
import org.supremus.sych.sychnews.tasks.GetItemTask;
import org.supremus.sych.sychnews.tasks.SetItemTask;
import org.supremus.sych.sychnews.util.DataUtils;

public class NewsDetailFragment extends Fragment implements UIUpdater, View.OnClickListener {

    private static final String ARG_ID = "args:newsId";
    private static final int MODE_SHOW = 1;
    private static final int MODE_EDIT = 2;
    private Button btnEdit;
    private View v;

    public NewsDetailFragment() {
    }

    public static NewsDetailFragment newInstance(int newsId) {

        Bundle args = new Bundle();
        args.putInt(ARG_ID, newsId);
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_news_detail, container, false);
        Toolbar tb = v.findViewById(R.id.sych_toolbar);
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.setSupportActionBar(tb);
        btnEdit = v.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);
        ImageButton btnDelete = v.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        updateButton();
        if (savedInstanceState==null) {
            NewsViewFragment nvf = new NewsViewFragment();
            setViewer(nvf);
            int newsId = getArguments().getInt(ARG_ID);
            NYTApi.setSelectedItem(null);
            new GetItemTask(this, newsId).execute();
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private void setViewer(NewsViewFragment nvf) {

        this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.frame_details, nvf, "NEWS_VIEW")
                .addToBackStack("VIEW")
                .commit();
    }

    public void updateUI(NewsItem newsItem) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float ivHeight = displayMetrics.heightPixels / 3; //NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_ITEM);one third of a screen height

        ImageView iv = v.findViewById(R.id.iv_news_image);
        iv.getLayoutParams().height = (int) (ivHeight);
        Glide.with(this).load(newsItem.getImageUrl()).into(iv);
        TextView header = v.findViewById(R.id.tv_news_header);
        header.setText(newsItem.getTitle());
        TextView timestamp = v.findViewById(R.id.tv_news_timestamp);
        DataUtils.setDateString(timestamp, newsItem.getPublishDate());
        TextView fullText = v.findViewById(R.id.tv_news_text);
        fullText.setText(newsItem.getPreviewText());

        if (newsItem.getCategory() != null) {
            getActivity().setTitle(newsItem.getCategory().getName());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_edit) {
            switch (NYTApi.getActivityMode()) {
                case MODE_SHOW:
                    NYTApi.setActivityMode(MODE_EDIT);
                    updateButton();
                    NewsEditFragment nef = new NewsEditFragment();
                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_details, nef, "NEWS_EDIT")
                            .addToBackStack("EDIT")
                            .commit();
                    break;
                case MODE_EDIT:
                    NYTApi.setActivityMode(MODE_SHOW);
                    updateButton();
                    updateData();
            }
        }
        if (v.getId() == R.id.btn_delete) {
            new DelItemTask(this, NYTApi.getSelectedItem()).execute();
        }
    }

    private void updateButton(){
        switch (NYTApi.getActivityMode()) {
            case MODE_SHOW:
                btnEdit.setText(R.string.btn_edit);
                break;
            case MODE_EDIT:
                btnEdit.setText(R.string.btn_save);
                break;
        }
    }

    private void updateData() {
        NewsEditFragment nef =
                (NewsEditFragment) getChildFragmentManager().findFragmentByTag("NEWS_EDIT");
        NewsItem currentItem = nef.getNews();
        NYTApi.setSelectedItem(currentItem);
        new SetItemTask(this, currentItem).execute();
        NewsViewFragment nvf = new NewsViewFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_details, nvf, "NEWS_VIEW")
                .addToBackStack("VIEW")
                .commit();
    }

}

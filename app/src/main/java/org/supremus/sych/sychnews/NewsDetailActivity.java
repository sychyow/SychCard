package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.fragments.NewsEditFragment;
import org.supremus.sych.sychnews.fragments.NewsViewFragment;
import org.supremus.sych.sychnews.util.DataUtils;

interface UIUpdater {
    void updateUI(NewsItem item);
}

public class NewsDetailActivity extends AppCompatActivity implements UIUpdater, NewsItemProvider, View.OnClickListener {


    private static Intent intent = null;
    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    public static final String EXTRA_ID = "EXTRA_ID";
    private static final int MODE_SHOW = 1;
    private static final int MODE_EDIT = 2;
    private int activityMode = MODE_SHOW;
    private NewsItem currentItem;
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_ITEM);
        if (newsItem!=null) {
            setContentView(R.layout.activity_news_detail_web);
            WebView wv = findViewById(R.id.webv_news);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.loadUrl(newsItem.getFullText());
        } else {
            setContentView(R.layout.activity_news_detail);
            Toolbar tb = findViewById(R.id.sych_toolbar);
            setSupportActionBar(tb);
            btnEdit = findViewById(R.id.btn_edit);
            btnEdit.setOnClickListener(this);
            NewsViewFragment nvf = new NewsViewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_details, nvf, "NEWS_VIEW")
                    .commit();
        }

    }

    public static void launch(Context parent, NewsItem item) {
        if (intent == null) intent = new Intent(parent, NewsDetailActivity.class);
        //intent.putExtra(EXTRA_ITEM, item);
        intent.putExtra(EXTRA_ID, item.getId());
        parent.startActivity(intent);
    }

    public void updateUI(NewsItem newsItem) {

        if (newsItem.getCategory()!=null) {
            setTitle(newsItem.getCategory().getName());
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float ivHeight = displayMetrics.heightPixels / 3; //NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_ITEM);one third of a screen height

        ImageView iv = findViewById(R.id.iv_news_image);
        iv.getLayoutParams().height = (int) (ivHeight);
        Glide.with(this).load(newsItem.getImageUrl()).into(iv);
        TextView header = findViewById(R.id.tv_news_header);
        header.setText(newsItem.getTitle());
        TextView timestamp = findViewById(R.id.tv_news_timestamp);
        DataUtils.setDateString(timestamp, newsItem.getPublishDate());
        TextView fullText = findViewById(R.id.tv_news_text);
        fullText.setText(newsItem.getFullText());
    }

    @Override
    public void onClick(View v) {
        switch (activityMode) {
            case MODE_SHOW:
                activityMode = MODE_EDIT;
                btnEdit.setText(R.string.btn_save);
                NewsEditFragment nef = new NewsEditFragment();
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_details, nef, "NEWS_EDIT")
                    .commit();
                break;
            case MODE_EDIT:
                activityMode = MODE_SHOW;
                btnEdit.setText(R.string.btn_edit);
                updateData();
                NewsViewFragment nvf = new NewsViewFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_details, nvf, "NEWS_VIEW")
                        .commit();
        }
    }

    private void updateData() {
        NewsEditFragment nef =
                (NewsEditFragment) getSupportFragmentManager().findFragmentByTag("NEWS_EDIT");
        currentItem = nef.getNews();
    }

    @Override
    public NewsItem getItem() {
        return currentItem;
    }

    @Override
    public void setItem(NewsItem val) {
        currentItem = val;
    }
}

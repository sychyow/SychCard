package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.fragments.NewsViewFragment;
import org.supremus.sych.sychnews.util.DataUtils;

public class NewsDetailActivity extends AppCompatActivity {
    private static Intent intent = null;
    public static final String EXTRA_ITEM = "EXTRA_ITEM";
    public static final String EXTRA_ID = "EXTRA_ID";

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

    public void initUI(NewsItem newsItem) {

        if (newsItem.getCategory()!=null) {
            getSupportActionBar().setTitle(newsItem.getCategory().getName());
            //setTitle(newsItem.getCategory().getName());
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
}

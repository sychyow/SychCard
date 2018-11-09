package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {
    private static Intent intent = null;
    private static final String EXTRA_ITEM = "EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar tb = findViewById(R.id.sych_toolbar);
        setSupportActionBar(tb);
        NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_ITEM);
        if (newsItem.getCategory()!=null)
            setTitle(newsItem.getCategory().getName());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float ivHeight = displayMetrics.heightPixels / 3; //one third of a screen height

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

    public static void launch(Context parent, NewsItem item) {
        if (intent == null) intent = new Intent(parent, NewsDetailActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        parent.startActivity(intent);
    }
}

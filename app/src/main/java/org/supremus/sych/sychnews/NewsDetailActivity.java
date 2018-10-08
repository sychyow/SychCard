package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {
    private static Intent intent = null;
    private static String EXTRA_ITEM = "EXTRA_ITEM";
    private NewsItem newsItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        newsItem = getIntent().<NewsItem>getParcelableExtra(EXTRA_ITEM);
        setTitle(newsItem.getCategory().getName());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float ivHeight = displayMetrics.heightPixels / 3; //one third of a screen height

        ImageView iv = findViewById(R.id.iv_news_image);
        iv.getLayoutParams().height = (int) (ivHeight);
        Glide.with(this).load(newsItem.getImageUrl()).into(iv);
        TextView header = findViewById(R.id.tv_news_header);
        header.setText(newsItem.getTitle());
        TextView timestamp = findViewById(R.id.tv_news_timestamp);
        timestamp.setText(newsItem.getPublishDate().toString());
        TextView fullText = findViewById(R.id.tv_news_text);
        fullText.setText(newsItem.getFullText());


    }

    public static void launch(Context parent, NewsItem item) {
        if (intent == null) intent = new Intent(parent, NewsDetailActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        parent.startActivity(intent);
    }
}

package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import org.supremus.sych.sychnews.data.NewsItem;

public class NewsDetailActivity extends AppCompatActivity {
    private static Intent intent = null;
    private static final String EXTRA_ITEM = "EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_web);
        NewsItem newsItem = getIntent().getParcelableExtra(EXTRA_ITEM);
        WebView wv = findViewById(R.id.webv_news);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl(newsItem.getFullText());
        /*Toolbar tb = findViewById(R.id.sych_toolbar);
        setSupportActionBar(tb);

        if (newsItem.getCategory()!=null)
            setTitle(newsItem.getCategory().getName());

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
        fullText.setText(newsItem.getFullText());*/
    }

    public static void launch(Context parent, NewsItem item) {
        if (intent == null) intent = new Intent(parent, NewsDetailActivity.class);
        intent.putExtra(EXTRA_ITEM, item);
        parent.startActivity(intent);
    }
}

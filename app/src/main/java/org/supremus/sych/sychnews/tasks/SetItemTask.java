package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.lang.ref.WeakReference;

public class SetItemTask extends AsyncTask<Object, Void, Void> {
    private final WeakReference<Activity> nda;
    private final NewsDB db;
    private NewsItem newsItem;

    public SetItemTask(Activity nda, NewsItem newsItem) {
        this.nda = new WeakReference<>(nda);
        this.newsItem = newsItem;
        db = NewsDB.getAppDatabase(nda);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NewsEntity dbNews = NewsExtractor.makeEntity(newsItem);
        db.newsDAO().update(dbNews);
        NYTApi.setChangedId(dbNews.id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

package org.supremus.sych.sychnews.tasks;

import android.os.AsyncTask;

import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.util.NewsExtractor;

import androidx.fragment.app.Fragment;

public class SetItemTask extends AsyncTask<Object, Void, Void> {
    private final NewsDB db;
    private final NewsItem newsItem;

    public SetItemTask(Fragment fragment, NewsItem newsItem) {
        this.newsItem = newsItem;
        db = NewsDB.getAppDatabase(fragment.getActivity());
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

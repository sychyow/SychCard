package org.supremus.sych.sychnews.tasks;

import android.os.AsyncTask;

import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.util.NewsExtractor;

import androidx.fragment.app.Fragment;

public class GetItemTask extends AsyncTask<Object, Void, Void> {
    private final NewsDB db;
    private final int newsId;

    public GetItemTask(Fragment fragment, int newsId) {
        db = NewsDB.getAppDatabase(fragment.getActivity());
        this.newsId = newsId;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NewsEntity dbNews = db.newsDAO().getNewsById(newsId);
        NewsItem newsItem = NewsExtractor.makeItem(dbNews);
        NYTApi.setSelectedItem(newsItem);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

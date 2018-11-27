package org.supremus.sych.sychnews.tasks;

import android.os.AsyncTask;

import org.supremus.sych.sychnews.interfaces.NewsItemProvider;
import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;

public class GetItemTask extends AsyncTask<Object, Void, Void> {
    private final WeakReference<Fragment> nda;
    private final NewsDB db;
    private final int newsId;
    private NewsItem newsItem;

    public GetItemTask(Fragment fragment, int newsId) {
        this.nda = new WeakReference<>(fragment);
        db = NewsDB.getAppDatabase(fragment.getActivity());
        this.newsId = newsId;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NewsEntity dbNews = db.newsDAO().getNewsById(newsId);
        newsItem = NewsExtractor.makeItem(dbNews);
        NYTApi.setSelectedItem(newsItem);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

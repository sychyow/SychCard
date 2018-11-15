package org.supremus.sych.sychnews;

import android.app.Activity;
import android.os.AsyncTask;

import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.lang.ref.WeakReference;

public class GetItemTask extends AsyncTask<Object, Void, Void> {
    private final WeakReference<Activity> nda;
    private final NewsDB db;
    private final int newsId;
    private NewsItem newsItem;

    public GetItemTask(Activity activity, int newsId) {
        this.nda = new WeakReference<>(activity);
        db = NewsDB.getAppDatabase(activity);
        this.newsId = newsId;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NewsEntity dbNews = db.newsDAO().getNewsById(newsId);
        newsItem = NewsExtractor.makeItem(dbNews);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Activity activity = nda.get();
        if (activity!=null) {
            UIUpdater updater = (UIUpdater) activity;
            activity.runOnUiThread(() -> updater.updateUI(newsItem));
        }
    }
}

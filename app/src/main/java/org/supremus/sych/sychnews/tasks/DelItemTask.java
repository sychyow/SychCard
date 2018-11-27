package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DelItemTask extends AsyncTask<Object, Void, Void> {
    private final NewsDB db;
    private final NewsItem newsItem;
    private final WeakReference<Fragment> wFrag;

    public DelItemTask(Fragment fragment, NewsItem newsItem) {
        this.newsItem = newsItem;
        db = NewsDB.getAppDatabase(fragment.getActivity());
        wFrag = new WeakReference<>(fragment);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        NewsEntity dbNews = NewsExtractor.makeEntity(newsItem);
        db.newsDAO().delete(dbNews);
        NYTApi.setRemovedId(dbNews.id);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Fragment fragment = wFrag.get();
        if (fragment!=null) {
            fragment.getActivity().getSupportFragmentManager().popBackStack("LIST", 0);
        }
    }
}

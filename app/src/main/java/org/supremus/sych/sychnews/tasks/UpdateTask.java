package org.supremus.sych.sychnews.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.supremus.sych.sychnews.R;
import org.supremus.sych.sychnews.SychApp;
import org.supremus.sych.sychnews.data.FeedDTO;
import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.interfaces.CompleteListener;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.network.TopStoriesService;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class UpdateTask extends AsyncTask<Object, Void, Void> {
    private static final String LAST_UPDATE_TIME_PREF = "LAST_UPDATE_TIME_PREF";
    static final int MIN_UPD_INTERVAL = 2 * 60 * 60 * 1000;  //two hours
    private final TopStoriesService svc;
    private NewsDB db;
    private CompleteListener listener;
    private boolean wasSkipped;


    public UpdateTask(CompleteListener lsnr) {
        db = NewsDB.getAppDatabase(SychApp.SYCHCONTEXT);
        svc = NYTApi.getInstance().getTopStoriesService();
        listener = lsnr;
        wasSkipped = false;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SychApp.SYCHCONTEXT);

        Long lastTime = sharedPref.getLong(LAST_UPDATE_TIME_PREF, 0);
        Long nowTime = new Date().getTime();
        if (nowTime-lastTime< MIN_UPD_INTERVAL) {
            wasSkipped = true;
            return null;
        }

        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putLong("LAST_UPDATE_TIME_PREF", nowTime);
        ed.apply();

        try {
            for (String sec : NYTApi.SECTIONS) {
                List<NewsItem> data = getNetworkData(sec);
                saveToDB(sec, data);
                Thread.sleep(NYTApi.TIMEOUT); //because NYT allows only one request in 5 seconds
            }
        }catch (Exception ex) {
            return null;
        }
        return null;
    }

    private void saveToDB(String section, List<NewsItem> data) {
        if (data==null) return;
        NewsEntity news[] = new NewsEntity[data.size()];
        for (int i=0; i<data.size(); i++)
            news[i] = NewsExtractor.makeEntity(data.get(i), section);
        db.newsDAO().deleteSection(section);
        db.newsDAO().insertAll(news);
    }

    private List<NewsItem> getNetworkData(String section) throws Exception {
        List<NewsItem> data;
        try {
            Response<FeedDTO> response = svc.getStories(section).execute();
            if (response.code()==200) {
                assert response.body() != null;
                data = NewsExtractor.extract(response.body());
            } else {
                showError(SychApp.SYCHCONTEXT.getString(R.string.error_server) + response.toString());
                throw new Exception();
            }
        } catch (IOException e) {
            showError(SychApp.SYCHCONTEXT.getString(R.string.error_network) + e.getLocalizedMessage());
            throw new Exception();
        }
        return data;
    }

    private void showError(String s) {
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!wasSkipped)
            listener.taskComplete();
    }
}

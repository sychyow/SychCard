package org.supremus.sych.sychnews.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import org.supremus.sych.sychnews.NewsAdapter;
import org.supremus.sych.sychnews.NewsListActivity;
import org.supremus.sych.sychnews.R;
import org.supremus.sych.sychnews.SychApp;
import org.supremus.sych.sychnews.UITooler;
import org.supremus.sych.sychnews.data.FeedDTO;
import org.supremus.sych.sychnews.data.NewsDB;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.network.TopStoriesService;
import org.supremus.sych.sychnews.util.NewsExtractor;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Response;

public class LoadDataTask extends AsyncTask<Object, Void, Void> {
    public static final int TASK_DB = 1;
    public static int TASK_NETWORK = 2;
    private WeakReference<UITooler> wTooler;
    private boolean isUpdate;
    private int dataMode;
    private final NewsDB db;

    public LoadDataTask(Activity activity, int mode, boolean update) {
        wTooler = new WeakReference<>((UITooler) activity);
        db = NewsDB.getAppDatabase(activity);
        isUpdate = update;
        dataMode = mode;
    }

    @Override
    protected Void doInBackground(Object[] objects) {

        List<NewsItem> data;
        if (dataMode == TASK_NETWORK) {
            List<NewsItem> dataIn = getNetworkData();
            saveToDB(dataIn);
        }
        
        data = loadFromDB();

        setData(data);

        return null;
    }

    private List<NewsItem> loadFromDB() {
        List<NewsEntity> rawData = db.newsDAO().getNewsBySection(NYTApi.getInstance().getCurrentSection());
        return NewsExtractor.extract(rawData);
    }

    private void saveToDB(List<NewsItem> data) {
        if (data==null) return;
        NewsEntity news[] = new NewsEntity[data.size()];
        for (int i=0; i<data.size(); i++)
            news[i] = NewsExtractor.makeEntity(data.get(i));
        db.newsDAO().insertAll(news);
    }

    private List<NewsItem> getNetworkData() {
        TopStoriesService svc = NYTApi.getInstance().getTopStoriesService();
        List<NewsItem> data = null;
        try {
            Response<FeedDTO> response = svc.getStories(NYTApi.getCurrentSection()).execute();
            if (response.code()==200) {
                assert response.body() != null;
                data = NewsExtractor.extract(response.body());
            } else {
                showError(SychApp.SYCHCONTEXT.getString(R.string.error_server) + response.toString());
            }
        } catch (IOException e) {
            showError(SychApp.SYCHCONTEXT.getString(R.string.error_network) + e.getLocalizedMessage());
            return null;
        }
        return data;
    }



    private void setData(List<NewsItem> data) {

        UITooler tooler = wTooler.get();
        if (tooler!=null) {
            NewsAdapter na = new NewsAdapter(data);
            tooler.getUITool(NewsListActivity.MODE_RV)
                    .setNewsAdapter(na)
                    .apply();
            if (isUpdate) {
                tooler.getUITool(NewsListActivity.MODE_UPD)
                        .apply();
            }
        }
    }

    private void showError(String msg) {
        UITooler tooler = wTooler.get();
        if (tooler!=null) {
            tooler.getUITool(NewsListActivity.MODE_ERR)
                    .setErrorText(msg)
                    .apply();
        }
    }

    @Override
    protected void onPostExecute(Void res) {
        super.onPostExecute(res);
        UITooler tooler = wTooler.get();
        if (tooler!=null) {
            tooler.getUITool(NewsListActivity.MODE_PB).apply();
        }
    }
}

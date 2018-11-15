package org.supremus.sych.sychnews;

import android.os.AsyncTask;

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

class LoadDataTask extends AsyncTask<Object, Void, Void> {
    public static int TASK_DB = 1;
    public static int TASK_NETWORK = 2;
    private WeakReference<NewsListActivity> nla;
    boolean isUpdate;
    int dataMode;
    private final NewsDB db;

    LoadDataTask(NewsListActivity activity, int mode, boolean update) {
        this.nla = new WeakReference<>(activity);
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

        if (isUpdate) {
            updateData(data);
        } else {
            setData(data);
        }
        return null;
    }

    private List<NewsItem> loadFromDB() {
        List<NewsEntity> rawData = db.newsDAO().getNewsBySection(NYTApi.getCurrentSection());
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

    private void updateData(List<NewsItem> data) {
        NewsListActivity activity = nla.get();
        if (activity!=null) {
            NewsAdapter na = new NewsAdapter(data);
            NewsListActivity.UITool runner = activity.getUITool(NewsListActivity.UITool.MODE_RV)
                    .setNewsAdapter(na);
            activity.runOnUiThread(runner);
            runner = activity.getUITool(NewsListActivity.UITool.MODE_UPD);
            activity.runOnUiThread(runner);
        }
     }

    private void setData(List<NewsItem> data) {
        NewsAdapter na = new NewsAdapter(data);
        NewsListActivity activity = nla.get();
        if (activity!=null) {
            NewsListActivity.UITool runner = activity.getUITool(NewsListActivity.UITool.MODE_RV)
                            .setNewsAdapter(na);
            activity.runOnUiThread(runner);
        }
    }

    private void showError(String msg) {
        NewsListActivity activity = nla.get();
        if (activity!=null) {
            NewsListActivity.UITool runner = activity.getUITool(NewsListActivity.UITool.MODE_ERR)
                            .setErrorText(msg);
            activity.runOnUiThread(runner);
        }
    }

    @Override
    protected void onPostExecute(Void res) {
        super.onPostExecute(res);
        NewsListActivity activity = nla.get();
        if (activity!=null) {
            activity.runOnUiThread(activity.getUITool(NewsListActivity.UITool.MODE_PB));
        }
    }
}

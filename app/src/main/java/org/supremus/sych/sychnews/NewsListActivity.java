package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;


public class NewsListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Toolbar tb = findViewById(R.id.sych_toolbar);
        setSupportActionBar(tb);
        rv = findViewById(R.id.rv_root);
        pb = findViewById(R.id.progressbar);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            getRv().setLayoutManager(llm);
        } else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dhWidth = displayMetrics.widthPixels / displayMetrics.density;
            int colNum = (int) Math.floor(dhWidth / 300.0);
            GridLayoutManager glm = new GridLayoutManager(this, colNum);
            getRv().setLayoutManager(glm);
        }
        new LoadDataTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutActivity.launch(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public RecyclerView getRv() {
        return rv;
    }

    public PBOff getPBOff(int mode) {
        return new PBOff(mode);
    }

    private class PBOff implements Runnable {
        private final static int MODE_PB = 0;
        private final static int MODE_RV = 1;
        private int mode;

        private NewsAdapter na;

        PBOff(int m) {
            mode = m;
        }

        void setNewsAdapter(NewsAdapter n) {
            na = n;
        }

        @Override
        public void run() {
            switch(mode) {
                case MODE_PB:
                    pb.setVisibility(View.GONE);
                    break;
                case MODE_RV:
                    getRv().setAdapter(na);
                    getRv().setHasFixedSize(true);
                    break;
            }

        }
    }

    private static class LoadDataTask extends AsyncTask<Object, Void, Void> {
        private WeakReference<NewsListActivity> nla;

         LoadDataTask(NewsListActivity activity) {
            this.nla = new WeakReference<>(activity);
        }
        @Override
        protected Void doInBackground(Object[] objects) {
            NewsAdapter na = new NewsAdapter();
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                PBOff runner = activity.getPBOff(PBOff.MODE_RV);
                runner.setNewsAdapter(na);
                activity.runOnUiThread(runner);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                activity.runOnUiThread(activity.getPBOff(PBOff.MODE_PB));
            }
        }
    }

}

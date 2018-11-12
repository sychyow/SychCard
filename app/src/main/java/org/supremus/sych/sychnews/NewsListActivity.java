package org.supremus.sych.sychnews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.supremus.sych.sychnews.data.FeedDTO;
import org.supremus.sych.sychnews.data.NewsItem;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class NewsListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView rv;
    private ProgressBar pb;
    private LinearLayout errorPanel;
    private TextView errorText;
    private Button btnRetry;
    private Button btnSection;
    private Toolbar tb;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        findViews();
        btnRetry.setOnClickListener(this);
        btnSection.setOnClickListener(this);
        btnSection.setText(NYTApi.getCurrentSection());
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initSpinner();
        setOrientation();
        new LoadDataTask(this, false).execute();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> aa = new ArrayAdapter<CharSequence>(
                this,
                R.layout.sections_spinner,
                NYTApi.getSectionNames());
        sp.setAdapter(aa);
        sp.setOnItemSelectedListener(this);
        sp.setSelection(NYTApi.getSelectedIndex());
    }

    private void setOrientation() {
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
    }

    void showSectionDlg() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setTitle(getString(R.string.select_theme));
        adBuilder.setSingleChoiceItems(NYTApi.getSectionNames(), NYTApi.getSelectedIndex(), new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                NYTApi.setCurrentSection(item);
                pb.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                new LoadDataTask(NewsListActivity.this, true).execute();
                btnSection.setText(NYTApi.getCurrentSection());
                dialog.dismiss();
            }
        });
        AlertDialog alert = adBuilder.create();
        alert.show();
    }

    private void findViews() {
        tb = findViewById(R.id.sych_toolbar);
        rv = findViewById(R.id.rv_root);
        pb = findViewById(R.id.progressbar);
        errorPanel = findViewById(R.id.errorPanel);
        errorText = findViewById(R.id.tv_error);
        btnRetry = findViewById(R.id.btnRetry);
        btnSection = findViewById(R.id.btn_section);
        sp = findViewById(R.id.spin_newslist);
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

    public UITool getUITool(int mode) {
        return new UITool(mode);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            errorPanel.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            new LoadDataTask(this, false).execute();
        }

        if (v.getId() == R.id.btn_section) {
            showSectionDlg();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NYTApi.setCurrentSection(position);
        pb.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        new LoadDataTask(NewsListActivity.this, true).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class UITool implements Runnable {
        private final static int MODE_PB = 0;
        private final static int MODE_RV = 1;
        private final static int MODE_ERR = 2;
        private final static int MODE_UPD = 3;
        private int mode;

        private NewsAdapter na;
        private String et;

        UITool(int m) {
            mode = m;
        }

        UITool setNewsAdapter(NewsAdapter n) {
            na = n; return this;
        }

        UITool setErrorText(String s) {
            et = s;
            return this;
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
                case MODE_ERR:
                    pb.setVisibility(View.GONE);
                    errorPanel.setVisibility(View.VISIBLE);
                    errorText.setText(et);
                    break;
                case MODE_UPD:
                    getRv().setVisibility(View.VISIBLE);
                    getRv().getAdapter().notifyDataSetChanged();
                    break;
            }

        }
    }

    private static class LoadDataTask extends AsyncTask<Object, Void, Void> {
        private WeakReference<NewsListActivity> nla;
        boolean isUpdate;

         LoadDataTask(NewsListActivity activity, boolean update) {
            this.nla = new WeakReference<>(activity);
            isUpdate = update;
        }
        @Override
        protected Void doInBackground(Object[] objects) {
            TopStoriesService svc = NYTApi.getInstance().getTopStoriesService();
            List<NewsItem> data = new ArrayList<>();
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
            if (isUpdate) {
                updateData(data);
            } else {
                setData(data);
            }
            return null;
        }

        private void updateData(List<NewsItem> data) {
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                NewsAdapter na = new NewsAdapter(data);
                UITool runner = activity.getUITool(UITool.MODE_RV)
                        .setNewsAdapter(na);
                activity.runOnUiThread(runner);
                runner = activity.getUITool(UITool.MODE_UPD);
                activity.runOnUiThread(runner);
            }
         }

        private void setData(List<NewsItem> data) {
            NewsAdapter na = new NewsAdapter(data);
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                UITool runner = activity.getUITool(UITool.MODE_RV)
                                .setNewsAdapter(na);
                activity.runOnUiThread(runner);
            }
        }

        private void showError(String msg) {
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                UITool runner = activity.getUITool(UITool.MODE_ERR)
                                .setErrorText(msg);
                activity.runOnUiThread(runner);
            }
        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            NewsListActivity activity = nla.get();
            if (activity!=null) {
                activity.runOnUiThread(activity.getUITool(UITool.MODE_PB));
            }
        }
    }

}

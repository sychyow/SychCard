package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
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


public class NewsListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private RecyclerView rv;
    private ProgressBar pb;
    private LinearLayout errorPanel;
    private TextView errorText;
    private Button btnRetry;
    private Toolbar tb;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        findViews();
        btnRetry.setOnClickListener(this);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initSpinner();
        setOrientation();
        new LoadDataTask(this, LoadDataTask.TASK_DB, false).execute();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> aa = new ArrayAdapter<>(
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

    private void findViews() {
        tb = findViewById(R.id.sych_toolbar);
        rv = findViewById(R.id.rv_root);
        pb = findViewById(R.id.progressbar);
        errorPanel = findViewById(R.id.errorPanel);
        errorText = findViewById(R.id.tv_error);
        btnRetry = findViewById(R.id.btnRetry);
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
            case R.id.menu_update: {
                pb.setVisibility(View.VISIBLE);
                new LoadDataTask(this, LoadDataTask.TASK_NETWORK, true).execute();
            }
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
            new LoadDataTask(this, LoadDataTask.TASK_NETWORK, false).execute();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NYTApi.setCurrentSection(position);
        pb.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        int mode = NYTApi.isEnabled()?LoadDataTask.TASK_NETWORK:LoadDataTask.TASK_DB;
        new LoadDataTask(NewsListActivity.this, mode,true).execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

     class UITool implements Runnable {
        final static int MODE_PB = 0;
        final static int MODE_RV = 1;
        final static int MODE_ERR = 2;
        final static int MODE_UPD = 3;
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

}

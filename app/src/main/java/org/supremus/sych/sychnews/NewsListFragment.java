package org.supremus.sych.sychnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.tasks.GetItemTask;

import java.util.List;


public class NewsListFragment extends Fragment implements NewsItemProvider,View.OnClickListener, AdapterView.OnItemSelectedListener, UITooler {

    private RecyclerView rv;
    private ProgressBar pb;
    private LinearLayout errorPanel;
    private TextView errorText;
    private Button btnRetry;
    private Toolbar tb;
    private Spinner sp;


    public final static int MODE_PB = 0;
    public final static int MODE_RV = 1;
    public final static int MODE_ERR = 2;
    public final static int MODE_UPD = 3;

    private UITool tool;
    private View v;

    public NewsListFragment() {
        tool = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.activity_news_list, container, false);
        findViews();
        btnRetry.setOnClickListener(this);
        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.setSupportActionBar(tb);
        aca.getSupportActionBar().setDisplayShowTitleEnabled(false);
        initSpinner();
        setOrientation();
        NewsLoader.load(this);
        return v;
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> aa = new ArrayAdapter<>(
                getActivity(),
                R.layout.sections_spinner,
                NYTApi.getInstance().getSectionNames());
        sp.setAdapter(aa);
        sp.setOnItemSelectedListener(this);
        sp.setSelection(NYTApi.getSelectedIndex());
    }

    private void setOrientation() {
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            getRv().setLayoutManager(llm);
        } else {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float dhWidth = displayMetrics.widthPixels / displayMetrics.density;
            int colNum = (int) Math.floor(dhWidth / 300.0);
            GridLayoutManager glm = new GridLayoutManager(getContext(), colNum);
            getRv().setLayoutManager(glm);
        }
    }

    private void findViews() {
        tb = v.findViewById(R.id.sych_toolbar);
        rv = v.findViewById(R.id.rv_root);
        pb = v.findViewById(R.id.progressbar);
        errorPanel = v.findViewById(R.id.errorPanel);
        errorText = v.findViewById(R.id.tv_error);
        btnRetry = v.findViewById(R.id.btnRetry);
        sp = v.findViewById(R.id.spin_newslist);
    }

    @Override
    public void onStart() {
        super.onStart();
        int id = NYTApi.getChangedId();
        if (id > 0) {
            new GetItemTask(getActivity(), id).execute();
            NYTApi.setChangedId(-1);
        }
        id = NYTApi.getRemovedId();
        if (id>0) {
            removeItem(id);
            NYTApi.setRemovedId(-1);
        }
    }

    private void removeItem(int id) {
        NewsAdapter newsAdapter = (NewsAdapter)getRv().getAdapter();
        if (newsAdapter==null) return;
        List<NewsItem> itemList  = (newsAdapter).getData();
        for (int i=0; i<itemList.size(); i++) {
            if (itemList.get(i).getId()==id) {
                itemList.remove(i);
                newsAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }





    public RecyclerView getRv() {
        return rv;
    }

    public ProgressBar getPb() {
        return pb;
    }

    @Override
    public UITooler getUITool(int mode) {
         tool = new UITool(mode);
         return this;
    }

    @Override
    public UITooler setNewsAdapter(NewsAdapter na) {
        tool.setNewsAdapter(na);
        return this;
    }

    @Override
    public UITooler setErrorText(String msg) {
        tool.setErrorText(msg);
        return this;
    }

    @Override
    public void apply() {
        getActivity().runOnUiThread(tool);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            errorPanel.setVisibility(View.GONE);
            NewsLoader.forceNetwork();
            NewsLoader.load(this);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        NYTApi.setCurrentSection(position);
        NewsLoader.load(NewsListFragment.this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public NewsItem getItem() {
        return null;
    }

    @Override
    public void setItem(NewsItem val) {
        NewsAdapter na = (NewsAdapter)getRv().getAdapter();
        List<NewsItem> ln  = (na).getData();
        for (int i=0; i<ln.size(); i++) {
            if (ln.get(i).getId()==val.getId()) {
                ln.set(i, val);
                na.notifyItemChanged(i);
                break;
            }
        }
    }

    class UITool implements Runnable {
        private final int mode;

        private NewsAdapter na;
        private String et;

        UITool(int m) {
            mode = m;
        }

        void setNewsAdapter(NewsAdapter n) {
            na = n;
        }

        void setErrorText(String s) {
            et = s;
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
                    getRv().setVisibility(View.VISIBLE);
                    break;
                case MODE_ERR:
                    pb.setVisibility(View.GONE);
                    errorPanel.setVisibility(View.VISIBLE);
                    errorText.setText(et);
                    break;
                case MODE_UPD:
                    getRv().setVisibility(View.VISIBLE);
                    NewsAdapter newsAdapter = (NewsAdapter) getRv().getAdapter();
                    newsAdapter.setData(na.getData());
                    newsAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

}

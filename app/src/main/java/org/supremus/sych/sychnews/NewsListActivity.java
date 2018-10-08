package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class NewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        RecyclerView rv = findViewById(R.id.rv_root);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv.setLayoutManager(llm);
        } else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dhWidth = displayMetrics.widthPixels / displayMetrics.density;
            int colNum = (int) Math.floor(dhWidth / 300.0);
            GridLayoutManager glm = new GridLayoutManager(this, colNum);
            rv.setLayoutManager(glm);
        }
        NewsAdapter na = new NewsAdapter();
        rv.setAdapter(na);
        rv.setHasFixedSize(true);
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
}

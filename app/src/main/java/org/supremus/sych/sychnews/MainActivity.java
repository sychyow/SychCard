package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NewsListFragment nlf = new NewsListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_list, nlf, "NEWS_LIST")
                .addToBackStack("LIST")
                .commit();
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
                NewsLoader.setUpdate(true);
                NewsLoader.forceNetwork();
                NewsLoader.load((NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_list));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            return;
        }
    }
}

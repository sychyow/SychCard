package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int MODE_LIST = 0;
    private static final int MODE_SHOW = 1;
    private static final int MODE_EDIT = 2;
    private int activityMode = MODE_LIST;

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
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment==null||fragment.getChildFragmentManager().getBackStackEntryCount()<=1)
            getSupportFragmentManager().popBackStack();
    }
}

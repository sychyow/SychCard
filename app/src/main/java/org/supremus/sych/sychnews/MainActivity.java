package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import org.supremus.sych.sychnews.fragments.IntroPanelFragment;
import org.supremus.sych.sychnews.fragments.NewsListFragment;
import org.supremus.sych.sychnews.interfaces.ModeSetter;
import org.supremus.sych.sychnews.network.NYTApi;

public class MainActivity extends AppCompatActivity implements ModeSetter {

    public static final int MODE_LIST = 0;
    public static final int MODE_SHOW = 1;
    public static final int MODE_EDIT = 2;
    private int activityMode = MODE_LIST;

    private static final String INTRO_PREF = "INTRO";
    private static final Boolean INTRO_DEF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null) {
            if (needIntro()) {
                IntroPanelFragment ipf = new IntroPanelFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_list, ipf, "NEWS_INTRO")
                        .commit();
            } else {
                NewsListFragment nlf = new NewsListFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frame_list, nlf, "NEWS_LIST")
                        .addToBackStack("LIST")
                        .commit();
            }
            updatePref();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        if(activityMode==MODE_SHOW){
            menu.findItem(R.id.menu_update).setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutActivity.launch(this);
                return true;
            case R.id.menu_update: {
                NewsLoader.get()
                        .setUpdate()
                        .forceNetwork()
                        .load((NewsListFragment)getSupportFragmentManager().findFragmentById(R.id.frame_list));
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

    @Override
    public void setMode(int mode) {

        activityMode = mode;
        NYTApi.setActivityMode(mode);
        invalidateOptionsMenu();
    }

    private boolean needIntro() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPref.getBoolean(INTRO_PREF, INTRO_DEF);
    }

    private void updatePref() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPref.edit();
        Boolean introState = sharedPref.getBoolean(INTRO_PREF, INTRO_DEF);
        ed.putBoolean(INTRO_PREF, !introState);
        ed.apply();
    }


}

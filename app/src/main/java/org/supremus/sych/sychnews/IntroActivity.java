package org.supremus.sych.sychnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.disposables.CompositeDisposable;
import me.relex.circleindicator.CircleIndicator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import org.supremus.sych.sychnews.fragments.IntroFragments;

public class IntroActivity extends AppCompatActivity {

    private static final String INTRO_PREF = "INTRO";
    private static final Boolean INTRO_DEF = true;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static class IntroPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 3;

        public IntroPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return IntroFragments.newInstance(R.drawable.screenshot);
                case 1:
                    return IntroFragments.newInstance(R.drawable.screen_details);
                case 2:
                    return IntroFragments.newInstance(R.drawable.screen_about);
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (needIntro()) {
            setContentView(R.layout.activity_intro);
            ViewPager pager = findViewById(R.id.intro_pager);
            IntroPagerAdapter pagerAdapter = new IntroPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
            CircleIndicator ci = findViewById(R.id.cindicator);
            ci.setViewPager(pager);

            TextView welcome = findViewById(R.id.text_welcome);
            welcome.setOnClickListener(this::startSecondActivity);
        } else {
            startSecondActivity();
        }
        updatePref();
    }

    private void startSecondActivity(View view) {
        startSecondActivity();
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

    void startSecondActivity() {
        startActivity(new Intent(this, NewsListFragment.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

}

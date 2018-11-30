package org.supremus.sych.sychnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.supremus.sych.sychnews.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class IntroPanelFragment extends Fragment {

    public static class IntroPagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 3;

        IntroPagerAdapter(FragmentManager fm) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_intro, container, false);

        ViewPager pager = v.findViewById(R.id.intro_pager);
        IntroPagerAdapter pagerAdapter = new IntroPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        CircleIndicator ci = v.findViewById(R.id.cindicator);
        ci.setViewPager(pager);

        TextView welcome = v.findViewById(R.id.text_welcome);
        welcome.setOnClickListener(this::startSecondActivity);

        return v;
    }

    private void startSecondActivity(View view) {
        NewsListFragment ndf = new NewsListFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, ndf, "NEWS_LIST")
                .addToBackStack("LIST")
                .commit();
    }




}

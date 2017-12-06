package com.example.usi.lanloc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.usi.lanloc.map.MapFragment;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private static int NUMBER_OF_FRAGMENTS = 3;
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment =  new MostPopularFragment();
                return fragment;
            case 1:
                fragment =  new NewestFragment();
                return fragment;
            case 2:
                fragment = new MapFragment();
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return ""; //"Most Popular";
            case 1:
                return ""; //"Newest";
            case 2:
                return ""; //"Map";
            default:
                return "EMPTY TAB";
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_FRAGMENTS;
    }
}

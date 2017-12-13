package com.example.usi.lanloc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.usi.lanloc.map.MapFragment;

import java.util.Observable;
import java.util.Observer;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    private Observable observers = new FragmentObserver();

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
                break;
            case 1:
                fragment =  new NewestFragment();
                break;
            case 2:
                fragment = new MapFragment();
                break;
            default:
                fragment = null;
                break;
        }

        if (fragment instanceof Observer) {
            observers.addObserver((Observer) fragment);
        }

        return fragment;
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

    public void updateFragments() {
        observers.notifyObservers();
    }
}

package com.example.usi.lanloc;

/**
 * Created by Jennifer Busta on 27.11.17.
 */

public enum CustomPagerEnum {

    MOST_POPULAR_PAGE(R.string.most_popular_name, R.layout.most_popular_page),
    NEWEST_PAGE(R.string.newest_name, R.layout.newest_page),
    MAP_PAGE(R.string.map_name, R.layout.map_page);

    private int titleResId;
    private int layoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        this.titleResId = titleResId;
        this.layoutResId = layoutResId;
    }

    public int getTitleResId() {
        return this.titleResId;
    }

    public int getLayoutResId() {
        return this.layoutResId;
    }

}
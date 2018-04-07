package com.example.baohuynh.mymovieapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.baohuynh.mymovieapp.R;
import com.example.baohuynh.mymovieapp.fragment.MovieFragment;
import java.lang.annotation.Retention;

import static com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter.TabItem.NOW_PLAYING;
import static com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter.TabItem.POPULAR;
import static com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter.TabItem.TOP_RATE;
import static com.example.baohuynh.mymovieapp.adapter.ViewPagerAdapter.TabItem.UPCOMING;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by baohuynh on 19/03/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final int TAB_ITEM = 4;
    private Context mContext;
    public static final String FRAGMENT_KEY = "fragment_key";

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        MovieFragment movieFragment = new MovieFragment();
        Bundle bundle = new Bundle();
        switch (position) {
            case POPULAR:
                bundle.putInt(FRAGMENT_KEY, POPULAR);
                movieFragment.setArguments(bundle);
                return movieFragment;
            case NOW_PLAYING:
                bundle.putInt(FRAGMENT_KEY, NOW_PLAYING);
                movieFragment.setArguments(bundle);
                return movieFragment;
            case UPCOMING:
                bundle.putInt(FRAGMENT_KEY, UPCOMING);
                movieFragment.setArguments(bundle);
                return movieFragment;
            case TOP_RATE:
                bundle.putInt(FRAGMENT_KEY, TOP_RATE);
                movieFragment.setArguments(bundle);
                return movieFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_ITEM;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POPULAR:
                return mContext.getResources().getString(R.string.popular);
            case NOW_PLAYING:
                return mContext.getResources().getString(R.string.now_playing);
            case UPCOMING:
                return mContext.getResources().getString(R.string.upcoming);
            case TOP_RATE:
                return mContext.getResources().getString(R.string.top_rate);
            default:
                return null;
        }
    }

    @Retention(SOURCE)
    @IntDef({POPULAR, NOW_PLAYING, UPCOMING, TOP_RATE})
    public @interface TabItem {
        int POPULAR = 0;
        int NOW_PLAYING = 1;
        int UPCOMING = 2;
        int TOP_RATE = 3;
    }
}

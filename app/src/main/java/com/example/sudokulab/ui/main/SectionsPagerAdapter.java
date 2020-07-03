package com.example.sudokulab.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sudokulab.Advanced;
import com.example.sudokulab.Basic;
import com.example.sudokulab.Tricks;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position) {
            case 0:
                Basic tab1=new Basic();
                return tab1;
            case 1:
                Tricks tab2=new Tricks();
                return tab2;
            case 2:
                Advanced tab3=new Advanced();
                return tab3;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Basic Rules";
            case 1:
                return "Tricks";
            case 2:
                return "Advanced";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}
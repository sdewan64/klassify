package com.shaheed.klassify.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shaheed.klassify.fragments.EditInformationFragment;
import com.shaheed.klassify.fragments.MyAdsFragment;
import com.shaheed.klassify.fragments.WishListFragment;

/**
 * Created by Shaheed on 1/30/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new MyAdsFragment();
            case 1:

                return new WishListFragment();
            case 2:
                return new EditInformationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
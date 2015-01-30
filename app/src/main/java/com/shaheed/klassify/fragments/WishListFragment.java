package com.shaheed.klassify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shaheed.klassify.R;

/**
 * Created by Shaheed on 1/30/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class WishListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);

        return rootView;
    }
}

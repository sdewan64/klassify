package com.shaheed.klassify.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shaheed.klassify.Constants;
import com.shaheed.klassify.NewAdActivity;
import com.shaheed.klassify.R;
import com.shaheed.klassify.SdHorizontalListView;
import com.shaheed.klassify.StartingActivity;
import com.shaheed.klassify.ViewAdActivity;
import com.shaheed.klassify.VolleyController;
import com.shaheed.klassify.models.Ads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Shaheed on 1/30/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class MyAdsFragment extends Fragment {

    private static final String VOLLEYTAG = "Account";

    private HorizontalScrollView myads_horizontalScrollView;
    private SdHorizontalListView sdHorizontalListView;

    private HashMap<Integer,Object> listMap;

    private View.OnClickListener onClickListener;

    private ImageButton myads_postButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_myads, container, false);

        myads_horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.myads_horizontalscrollview);
        myads_postButton = (ImageButton) rootView.findViewById(R.id.myads_imagebutton_postad);
        myads_postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewAdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        initiateAddScroller();

        return rootView;
    }

    private void initiateAddScroller() {

        if(!Constants.isConnected(getActivity())){
            return;
        }

        listMap = new HashMap<>();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(v.getId());
            }
        };
        sdHorizontalListView = new SdHorizontalListView(getActivity().getApplicationContext(), myads_horizontalScrollView, listMap);

        JsonArrayRequest editorsChoiceAdRequest = new JsonArrayRequest(Constants.URL_MY_AD, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray adArray) {
                for(int i=0;i<adArray.length();i++){
                    try{
                        JSONObject adObject = adArray.getJSONObject(i);
                        Ads ad = new Ads(adObject.getString("ad_id"),adObject.getString("ad_title"),adObject.getString("ad_description"),adObject.getString("ad_owner"),adObject.getString("ad_type"),adObject.getString("ad_category"),adObject.getString("ad_sub_category"),adObject.getString("ad_thumb_image"),adObject.getString("ad_price"),adObject.getString("ad_phone_number"),adObject.getString("ad_email"));
                        ad.setAdView(adObject.getString("ad_view_count"));
                        sdHorizontalListView.addNewImageTextItem(ad.getAdThumbImageLink(), VolleyController.getInstance().getImageLoader(), "View "+ad.getAdView(),ad,onClickListener);
                    }catch (JSONException e){
                        Log.e("JSONERROR", e.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", "Error: " + error.getMessage());
            }
        });

        VolleyController.getInstance().addNewToRequestQueue(editorsChoiceAdRequest,VOLLEYTAG);
    }

    public void itemClicked(Integer id){
        Constants.ad = (Ads) listMap.get(id);

        Intent intent = new Intent(getActivity(),ViewAdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

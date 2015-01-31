package com.shaheed.klassify.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shaheed.klassify.Constants;
import com.shaheed.klassify.R;
import com.shaheed.klassify.SdHorizontalListView;
import com.shaheed.klassify.SessionManager;
import com.shaheed.klassify.StartingActivity;
import com.shaheed.klassify.ViewAdActivity;
import com.shaheed.klassify.VolleyController;
import com.shaheed.klassify.models.Ads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Shaheed on 1/31/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class SubCategoryFragment extends Fragment {

    private static final String VOLLEYTAG = "Search";

    private ArrayAdapter adapter;

    private ListView listView;

    private String [] catagories;

    private HorizontalScrollView myads_horizontalScrollView;
    private SdHorizontalListView sdHorizontalListView;

    private HashMap<Integer,Object> listMap;

    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);

        listView = (ListView) view.findViewById(R.id.subcategory_list);
        String category = getArguments().getString("category");
        if(category == "Mobiles and Tablets"){
            catagories = getActivity().getResources().getStringArray(R.array.MobTabSubCat);
        }else if(category == "Electronics and Computer"){
            catagories = getActivity().getResources().getStringArray(R.array.ElecAndComAc);
        }else {
            catagories = getActivity().getResources().getStringArray(R.array.VehiclesSubCat);
        }
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, catagories);
        listView.setAdapter(adapter);

        myads_horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.subcategory_horizontalScrollView);
        listMap = new HashMap<>();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(v.getId());
            }
        };
        sdHorizontalListView = new SdHorizontalListView(getActivity().getApplicationContext(), myads_horizontalScrollView, listMap);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonArrayRequest editorsChoiceAdRequest = new JsonArrayRequest(Constants.URL_SEARCH_CATEGORY+"?search="+catagories[position], new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray adArray) {
                        for(int i=0;i<adArray.length();i++){
                            try{
                                JSONObject adObject = adArray.getJSONObject(i);
                                Ads ad = new Ads(adObject.getString("ad_id"),adObject.getString("ad_title"),adObject.getString("ad_description"),adObject.getString("ad_owner"),adObject.getString("ad_type"),adObject.getString("ad_category"),adObject.getString("ad_sub_category"),adObject.getString("ad_thumb_image"),adObject.getString("ad_price"),adObject.getString("ad_phone_number"),adObject.getString("ad_email"));
                                sdHorizontalListView.addNewImageTextItem(ad.getAdThumbImageLink(), VolleyController.getInstance().getImageLoader(), "BDT "+ad.getAdPrice(),ad,onClickListener);
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
        });

        return view;
    }

    public void itemClicked(Integer id){
        Constants.ad = (Ads) listMap.get(id);

        Intent intent = new Intent(getActivity(),ViewAdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

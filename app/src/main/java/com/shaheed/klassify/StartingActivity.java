package com.shaheed.klassify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shaheed.klassify.fragments.LoginFragment;
import com.shaheed.klassify.fragments.RegistrationFragment;
import com.shaheed.klassify.fragments.SubCategoryFragment;
import com.shaheed.klassify.models.Ads;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class StartingActivity extends ActionBarActivity {

    private static final String VOLLEYTAG = "Menu";

    private final String MENU_LOGIN = "Login";
    private final String MENU_SIGNUP = "Sign Up";

    private final String MENU_CAT1 = "Mobiles and Tablets";
    private final String MENU_CAT2 = "Electronics and Computer";
    private final String MENU_CAT3 = "Vehicles";

    private Activity activity;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private String[] mMenu  = new String[]{MENU_LOGIN, MENU_SIGNUP,"Categories",MENU_CAT1,MENU_CAT2,MENU_CAT3};
    private ArrayAdapter adapter;

    private SessionManager sessionManager;

    private ListView mDrawerList;
    private TextView textView;
    private ImageButton starting_postNewAdView;
    private HorizontalScrollView starting_horizontalScrollView;
    private SdHorizontalListView sdHorizontalListView;

    private HashMap<Integer,Object> listMap;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_starting);

        activity = this;
        sessionManager = new SessionManager(getApplicationContext());

        if(sessionManager.userLoggedIn()){
            textView = (TextView) findViewById(R.id.starting_textview_helloText);
            textView.setText("Hello "+ sessionManager.getUserName());
        }

        initiateMenuDrawer();
        findViewsById();
        implementButtons();

        initiateAddScroller();

        if(!Constants.isConnected(this)){
            Constants.makeToast(this,getString(R.string.network_not_connected),true);
        }

    }

    private void initiateAddScroller() {

        if(!Constants.isConnected(this)){
            return;
        }

        listMap = new HashMap<>();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClicked(v.getId());
            }
        };
        sdHorizontalListView = new SdHorizontalListView(getApplicationContext(), starting_horizontalScrollView, listMap);

        JsonArrayRequest editorsChoiceAdRequest = new JsonArrayRequest(Constants.URL_EDITORS_CHOICE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray adArray) {
                for(int i=0;i<adArray.length();i++){
                    try{
                        JSONObject adObject = adArray.getJSONObject(i);
                        Ads ad = new Ads(adObject.getString("ad_id"),adObject.getString("ad_title"),adObject.getString("ad_description"),adObject.getString("ad_owner"),adObject.getString("ad_type"),adObject.getString("ad_category"),adObject.getString("ad_sub_category"),adObject.getString("ad_thumb_image"),adObject.getString("ad_price"),adObject.getString("ad_phone_number"),adObject.getString("ad_email"));
                        sdHorizontalListView.addNewImageTextItem(ad.getAdThumbImageLink(),VolleyController.getInstance().getImageLoader(), "BDT "+ad.getAdPrice(),ad,onClickListener);
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

        Intent intent = new Intent(activity,ViewAdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("VOLLEY", "Volley cancel all called");
        VolleyController.getInstance().cancelAllRequest(VOLLEYTAG);
    }

    private void implementButtons() {
        starting_postNewAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sessionManager.userLoggedIn()){
                    Constants.makeToast(activity,"You need to register first!", true);
                    RegistrationFragment menuFragment = new RegistrationFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, menuFragment).commit();

                }else{
                    Intent intent = new Intent(StartingActivity.this, NewAdActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void findViewsById() {
        starting_postNewAdView = (ImageButton) findViewById(R.id.starting_imagebutton_postad);

        starting_horizontalScrollView = (HorizontalScrollView) findViewById(R.id.starting_horizontalscrollview);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initiateMenuDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, mMenu);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawers();
                Fragment menuFragment = null;

                if(mMenu[position].equals(MENU_LOGIN)){
                    menuFragment = new LoginFragment();
                }else if(mMenu[position].equals(MENU_SIGNUP)){
                    menuFragment = new RegistrationFragment();
                }else if(mMenu[position].equals(MENU_CAT1)){
                    Bundle bundle = new Bundle();
                    bundle.putString("category",MENU_CAT1);
                    menuFragment = new SubCategoryFragment();
                    menuFragment.setArguments(bundle);
                }else if(mMenu[position].equals(MENU_CAT2)){
                    Bundle bundle = new Bundle();
                    bundle.putString("category",MENU_CAT2);
                    menuFragment = new SubCategoryFragment();
                    menuFragment.setArguments(bundle);
                }else if(mMenu[position].equals(MENU_CAT3)){
                    Bundle bundle = new Bundle();
                    bundle.putString("category",MENU_CAT3);
                    menuFragment = new SubCategoryFragment();
                    menuFragment.setArguments(bundle);
                }else if(mMenu[position].equals("Categories")){
                    return;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, menuFragment).commit();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_starting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

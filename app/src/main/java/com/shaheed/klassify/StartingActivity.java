package com.shaheed.klassify;

import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shaheed.klassify.fragments.LoginFragment;
import com.shaheed.klassify.fragments.RegistrationFragment;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class StartingActivity extends ActionBarActivity {

    private final String MENU_LOGIN = "Login";
    private final String MENU_SIGNUP = "Sign Up";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mDrawerList;

    private String[] mMenu  = new String[]{MENU_LOGIN, MENU_SIGNUP};
    private ArrayAdapter adapter;

    private SessionManager sessionManager;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_starting);

        sessionManager = new SessionManager(getApplicationContext());

        if(sessionManager.userLoggedIn()){
            textView = (TextView) findViewById(R.id.starting_textview_helloText);
            textView.setText("Hello "+ sessionManager.getUserName()+"!");
        }

        initiateMenuDrawer();

        if(!Constants.isConnected(this)){
            Constants.makeToast(this,getString(R.string.network_not_connected),true);
        }

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

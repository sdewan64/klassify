package com.shaheed.klassify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private final int PRIVATE_MODE = 0;

    private static final String PREFERENCE_NAME = "Klassify";
    private static final String IS_USER_LOGGEDIN = "IsUserLoggedIn";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_NAME = "username";

    public static final String NO_ID_AVAILABLE = "no id available";
    public static final String NO_NAME_AVAILABLE = "Guest";

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createNewLoginSession(String id,String name){
        editor.putBoolean(IS_USER_LOGGEDIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);

        editor.commit();
    }

    public boolean userLoggedIn(){
        return sharedPreferences.getBoolean(IS_USER_LOGGEDIN, false);
    }

    public boolean checkLogin(){
        if(!this.userLoggedIn()){
            Intent in = new Intent(context, StartingActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            return true;
        }
        return false;
    }

    public String getUserId(){
        return sharedPreferences.getString(KEY_USER_ID, NO_ID_AVAILABLE);
    }

    public String getUserName(){
        return sharedPreferences.getString(KEY_USER_NAME, NO_NAME_AVAILABLE);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Constants.userId = "";
        Constants.userName = "";

        Intent in = new Intent(context, StartingActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }
}

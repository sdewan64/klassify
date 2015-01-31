package com.shaheed.klassify;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.shaheed.klassify.models.Ads;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class Constants {

    public static final String URL_BASE = "http://www.mocky.io/v2/";
    public static String URL_LOGIN = URL_BASE + "54cb9aed96d6b2c50e431f6b";
    public static String URL_REGISTRATION = URL_BASE + "54cb6b8396d6b26f09431f43";
    public static String URL_UPDATE = URL_BASE + "54cb6b8396d6b26f09431f43";
    public static String URL_FETCH = URL_BASE + "54cb86d896d6b2850c431f5d";
    public static String URL_EDITORS_CHOICE = URL_BASE + "54cc979d22f5cf2e0607e112";
    public static String URL_POST_AD = URL_BASE + "54cb9aed96d6b2c50e431f6b";
    public static String URL_MY_AD = URL_BASE + "54cc87d622f5cf3a0507e108";
    public static String URL_SEARCH_CATEGORY = URL_BASE + "54cc9a2c22f5cf600607e114";

    public static String userId = "";
    public static String userName = "";

    public static Ads ad;

    public static void makeToast(Activity activity,String message, boolean isError){
        if(!isError){
            Toast toast = Toast.makeText(activity,message,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toast.getView().setBackgroundColor(Color.parseColor(activity.getString(R.string.primary_color_string)));
            TextView toastView = (TextView) toast.getView().findViewById(android.R.id.message);
            toastView.setTextColor(Color.parseColor(activity.getString(R.string.secondary_color_string)));
            toastView.setGravity(Gravity.CENTER);
            toast.show();

        }else{
            Toast toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,20);
            toast.getView().setBackgroundColor(Color.parseColor(activity.getString(R.string.primary_color_string)));
            TextView toastView = (TextView) toast.getView().findViewById(android.R.id.message);
            toastView.setTextColor(Color.RED);
            toastView.setGravity(Gravity.CENTER);
            toast.show();
        }
    }

    public static void showProgressDialogue(ProgressDialog progressDialog,String title,String msg){
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    public static void closeProgressDialogue(ProgressDialog progressDialog){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    public static boolean isConnected(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        else return false;
    }
}

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
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shaheed.klassify.AccountActivity;
import com.shaheed.klassify.Constants;
import com.shaheed.klassify.R;
import com.shaheed.klassify.SessionManager;
import com.shaheed.klassify.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class LoginFragment extends Fragment{

    private static final String VOLLEYTAG = "Login";

    private Button login_loginButton;
    private EditText login_email,login_password;

    private ProgressDialog progressDialog;

    private Fragment currentFragment;

    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findViewsById(view);
        addClickListeners();
        progressDialog = new ProgressDialog(getActivity());
        currentFragment = this;

        handleSession();

        return view;
    }

    private void handleSession() {
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if(sessionManager.userLoggedIn()){
            Constants.makeToast(getActivity(),"Login Information Found",false);
            Intent in = new Intent(getActivity(), AccountActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            getActivity().finish();
        }
    }

    private void addClickListeners() {
        login_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_loginButtonClicked();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("VOLLEY", "Volley cancel all called");
        VolleyController.getInstance().cancelAllRequest(VOLLEYTAG);
    }

    private void findViewsById(View view) {

        login_loginButton = (Button) view.findViewById(R.id.login_button_login);

        login_email = (EditText) view.findViewById(R.id.login_edittext_email);
        login_password = (EditText) view.findViewById(R.id.login_edittext_password);
    }

    private void login_loginButtonClicked() {
        if(!Constants.isConnected(getActivity())){
            Constants.makeToast(getActivity(),getString(R.string.network_not_connected),true);
            return;
        }
        if(login_email.getText().toString().equals("") || login_password.getText().toString().equals("")){
            Constants.makeToast(this.getActivity(), "All fields are required!", true);
        }else {
            Constants.showProgressDialogue(progressDialog,"Logging In","Please wait while we check...");
            progressDialog.setCancelable(false);

            String email = login_email.getText().toString();
            String password = login_password.getText().toString();

            String passwordHash = null;

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(password.getBytes());
                byte[] bytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte aByte : bytes) {
                    sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                }
                passwordHash = sb.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                Log.e("HASH_ERROR","HASHING FAILED!");
            }

            HashMap<String, String> loginInfo = new HashMap<>();
            loginInfo.put("email", email);
            loginInfo.put("password", passwordHash);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.URL_LOGIN, new JSONObject(loginInfo), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Constants.closeProgressDialogue(progressDialog);
                    String reply;
                    String replyMsg = "";
                    Boolean isDone = false;
                    try{
                        reply = jsonObject.getString("reply");

                        if(reply.equals("done")){
                            isDone = true;
                            Constants.userId = jsonObject.getString("userid");
                            Constants.userName = jsonObject.getString("username");
                        }else{
                            replyMsg = reply;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    gotLoginResponse(isDone, replyMsg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Constants.closeProgressDialogue(progressDialog);
                    Constants.makeToast(currentFragment.getActivity(),"Network Error",true);
                    Log.e("Volley Error", volleyError.toString());
                }
            });
            VolleyController.getInstance().addNewToRequestQueue(jsonObjectRequest,VOLLEYTAG);
        }
    }

    private void gotLoginResponse(Boolean isDone, String replyMsg){
        if(isDone){
            //user found making session and redirecting to account menu

            SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
            sessionManager.createNewLoginSession(Constants.userId,Constants.userName);

            Constants.makeToast(currentFragment.getActivity(),"Login Successful.\nRedirecting to Account Page...",false);
            Intent in = new Intent(currentFragment.getActivity(), AccountActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            getActivity().finish();
        }else{
            //setting the statusText as the error replied from server
            Constants.makeToast(currentFragment.getActivity(), replyMsg, true);
        }
    }



}

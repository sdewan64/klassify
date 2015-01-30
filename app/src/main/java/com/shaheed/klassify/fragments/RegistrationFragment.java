package com.shaheed.klassify.fragments;

import android.app.ProgressDialog;
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
import com.shaheed.klassify.Constants;
import com.shaheed.klassify.R;
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

public class RegistrationFragment extends Fragment {

    private static final String VOLLEYTAG = "Registration";

    private Button registration_signUpButton,registration_socialSignupButton;
    EditText registration_fullName,registration_password,registration_confirmPassword,registration_email,registration_address,registration_phone;
    Fragment currentFragment;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        findViewsById(view);
        addClickListeners();

        if(getArguments()!=null){
            if(getArguments().getString("isFb")!=null){
                registration_fullName.setText(getArguments().getString("fbName"));
                registration_email.setText(getArguments().getString("fbEmail"));
                registration_address.setText(getArguments().getString("fbAddress"));
            }
        }


        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("VOLLEY", "Volley cancel all called");
        VolleyController.getInstance().cancelAllRequest(VOLLEYTAG);
    }

    private void findViewsById(View view) {
        registration_signUpButton = (Button) view.findViewById(R.id.registration_button_signup);
        registration_socialSignupButton = (Button) view.findViewById(R.id.registration_button_socialSignup);

        registration_fullName = (EditText) view.findViewById(R.id.registration_edittext_fullName);
        registration_password = (EditText) view.findViewById(R.id.registration_edittext_password);
        registration_confirmPassword = (EditText) view.findViewById(R.id.registration_edittext_confirmPassword);
        registration_email = (EditText) view.findViewById(R.id.registration_edittext_email);
        registration_address = (EditText) view.findViewById(R.id.registration_edittext_address);
        registration_phone = (EditText) view.findViewById(R.id.registration_edittext_phone);
    }

    private void addClickListeners() {
        registration_signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration_signUpButtonClicked();
            }
        });

        //TODO registration social media implementation
    }

    private void registration_signUpButtonClicked(){
        if(registration_fullName.getText().toString().equals("") || registration_password.getText().toString().equals("") || registration_confirmPassword.getText().toString().equals("") || registration_email.getText().toString().equals("") || registration_phone.getText().toString().equals("")){
            Constants.makeToast(this.getActivity(), "All fields are required!", true);
        }else{
            if(registration_password.getText().toString().equals(registration_confirmPassword.getText().toString())){
                Constants.showProgressDialogue(progressDialog,"Registering User","Please wait while we register your information");
                progressDialog.setCancelable(false);

                String fullName = registration_fullName.getText().toString();
                String email = registration_email.getText().toString();
                String password = registration_password.getText().toString();
                String address = registration_address.getText().toString();
                String phone = registration_phone.getText().toString();

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

                HashMap<String, String> registrationInfo = new HashMap<>();
                registrationInfo.put("fullName", fullName);
                registrationInfo.put("email", email);
                registrationInfo.put("password", passwordHash);
                registrationInfo.put("address", address);
                registrationInfo.put("phone", phone);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_REGISTRATION, new JSONObject(registrationInfo), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Constants.closeProgressDialogue(progressDialog);
                        Boolean isDone = false;
                        String reply;

                        try{
                            reply = jsonObject.getString("reply");

                            if(reply.equals("done")){
                                isDone = true;
                            }
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR","Could not retrieve return data");
                        }
                        gotRegistrationResponse(isDone);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Constants.closeProgressDialogue(progressDialog);
                        Constants.makeToast(currentFragment.getActivity(),"Network Error",true);
                        Log.e("Volley Error", volleyError.toString());
                    }
                });

                VolleyController.getInstance().addNewToRequestQueue(jsonObjectRequest, VOLLEYTAG);

            }else{
                Constants.makeToast(this.getActivity(), "Password and Confirm Password did not match!", true);
            }
        }
    }

    private void gotRegistrationResponse(Boolean isDone){
        if(isDone) {
            Constants.makeToast(currentFragment.getActivity(), "Registration was successful.\nYou can login now", false);
        }
        else{
            Constants.makeToast(currentFragment.getActivity(), "Registration was unsuccessful!", true);
        }
    }

}

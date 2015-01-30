package com.shaheed.klassify.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shaheed.klassify.AccountActivity;
import com.shaheed.klassify.Constants;
import com.shaheed.klassify.R;
import com.shaheed.klassify.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Shaheed on 1/30/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class EditInformationFragment extends Fragment {

    private static final String VOLLEYTAG = "Login";

    private EditText account_name,account_email,account_address,account_phone,account_oldPassword,account_newPassword,account_newConfirmPassword;
    private Button account_editButton;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_editinformation, container, false);

        progressDialog = new ProgressDialog(getActivity());

        findViewsById(rootView);
        implementButtons();

        fetchUserData();

        return rootView;
    }

    private void findViewsById(View view){
        account_name = (EditText) view.findViewById(R.id.account_editText_name);
        account_email = (EditText) view.findViewById(R.id.account_editText_email);
        account_address = (EditText) view.findViewById(R.id.account_editText_address);
        account_phone = (EditText) view.findViewById(R.id.account_editText_phonenumber);
        account_oldPassword = (EditText) view.findViewById(R.id.account_editText_oldPassword);
        account_newPassword = (EditText) view.findViewById(R.id.account_editText_newPassword);
        account_newConfirmPassword = (EditText) view.findViewById(R.id.account_editText_newConfirmPassword);

        account_editButton = (Button) view.findViewById(R.id.account_button_edit);

    }

    private void implementButtons() {
        account_editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Constants.isConnected(getActivity())){
                    Constants.makeToast(getActivity(),getString(R.string.network_not_connected),true);
                    return;
                }

                if(account_name.getText().toString().equals("") || account_email.getText().toString().equals("") || account_address.getText().toString().equals("") || account_phone.getText().toString().equals("") || account_oldPassword.getText().toString().equals("") || account_newPassword.getText().toString().equals("")){
                    Constants.makeToast(getActivity(), "All fields required!", true);
                }else{
                    if(!account_newPassword.getText().toString().equals(account_newConfirmPassword.getText().toString())){
                        Constants.makeToast(getActivity(), "New password and confirm password didn't match!", true);
                    }else {
                        Constants.showProgressDialogue(progressDialog,"Updating User","Please wait while we update your information");
                        progressDialog.setCancelable(false);

                        String fullName = account_name.getText().toString();
                        String email = account_email.getText().toString();
                        String oldPassword = account_oldPassword.getText().toString();
                        String newPassword = account_newPassword.getText().toString();
                        String address = account_address.getText().toString();
                        String phone = account_phone.getText().toString();

                        String oldPasswordHash = null;

                        try {
                            MessageDigest md = MessageDigest.getInstance("SHA-1");
                            md.update(oldPassword.getBytes());
                            byte[] bytes = md.digest();
                            StringBuilder sb = new StringBuilder();
                            for (byte aByte : bytes) {
                                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                            }
                            oldPasswordHash = sb.toString();
                        }
                        catch (NoSuchAlgorithmException e)
                        {
                            Log.e("HASH_ERROR", "HASHING FAILED!");
                        }

                        String newPasswordHash = null;

                        try {
                            MessageDigest md = MessageDigest.getInstance("SHA-1");
                            md.update(newPassword.getBytes());
                            byte[] bytes = md.digest();
                            StringBuilder sb = new StringBuilder();
                            for (byte aByte : bytes) {
                                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                            }
                            newPasswordHash = sb.toString();
                        }
                        catch (NoSuchAlgorithmException e)
                        {
                            Log.e("HASH_ERROR","HASHING FAILED!");
                        }

                        HashMap<String, String> updateInfo = new HashMap<>();
                        updateInfo.put("userid", Constants.userId);
                        updateInfo.put("fullName", fullName);
                        updateInfo.put("email", email);
                        updateInfo.put("oldpassword", oldPasswordHash);
                        updateInfo.put("newpassword", newPasswordHash);
                        updateInfo.put("address", address);
                        updateInfo.put("phone", phone);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_UPDATE, new JSONObject(updateInfo), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Constants.closeProgressDialogue(progressDialog);
                                Boolean isDone = false;
                                String reply = null;

                                try{
                                    reply = jsonObject.getString("reply");

                                    isDone = reply.equals("done");
                                } catch (JSONException e) {
                                    Log.e("JSON_ERROR","Could not retrieve return data");
                                }
                                gotUpdateResponse(isDone,reply);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Constants.closeProgressDialogue(progressDialog);
                                Constants.makeToast(getActivity(),"Network Error",true);
                                Log.e("Volley Error", volleyError.toString());
                            }
                        });

                        VolleyController.getInstance().addNewToRequestQueue(jsonObjectRequest, VOLLEYTAG);
                    }
                }

            }
        });
    }

    private void fetchUserData() {

        if(!Constants.isConnected(getActivity())){
            Constants.makeToast(getActivity(),getString(R.string.network_not_connected),true);
            return;
        }

        Constants.showProgressDialogue(progressDialog, "Fetching Data", "Please wait while we fetch...");
        progressDialog.setCancelable(false);

        String id = Constants.userId;

        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userid", id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.URL_FETCH, new JSONObject(userInfo), new Response.Listener<JSONObject>() {
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
                    }else{
                        replyMsg = reply;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gotUserFetchResponse(isDone, replyMsg, jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Constants.closeProgressDialogue(progressDialog);
                Constants.makeToast(getActivity(), "Network Error\nCould not Retrieve user information", true);
                Log.e("Volley Error", volleyError.toString());
            }
        });
        VolleyController.getInstance().addNewToRequestQueue(jsonObjectRequest,VOLLEYTAG);

    }

    private void gotUserFetchResponse(Boolean isDone, String replyMsg,JSONObject fetchedData) {
        if(isDone){
            try{
                account_name.setText(fetchedData.getString("username"));
                account_email.setText(fetchedData.getString("useremail"));
                account_address.setText(fetchedData.getString("useraddress"));
                account_phone.setText(fetchedData.getString("userphone"));
            }catch (JSONException e){
                Log.e("JSON Exception", e.getMessage());
                Constants.makeToast(getActivity(),"Error Occurred in fetching data", true);
            }
        }else {
            Constants.makeToast(getActivity(),replyMsg, true);
        }
    }

    private void gotUpdateResponse(Boolean isDone, String replyMsg) {
        if(isDone){
            Constants.makeToast(getActivity(),"Profile update was successful", false);
            Intent in = new Intent(getActivity(), AccountActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            getActivity().finish();
        }else {
            Constants.makeToast(getActivity(),replyMsg, true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("VOLLEY", "Volley cancel all called");
        VolleyController.getInstance().cancelAllRequest(VOLLEYTAG);
    }

}

package com.shaheed.klassify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class NewAdActivity extends ActionBarActivity{

    private static final String VOLLEYTAG = "NewAd";

    private ImageButton newad_getImageButton;
    private Button newad_postButton;

    private EditText newad_adTitle,newad_adDescpription, newad_adPrice;

    private Uri currImageURI;

    private Activity activity;

    private Spinner spinner_type, spinner_category, spinner_sub_category;
    private ProgressDialog progressDialog;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        activity = this;
        progressDialog = new ProgressDialog(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        if(sessionManager.checkLogin()) {
            finish();
        }

        findViewsById();
        implementButtons();

        String[] typeArray = getResources().getStringArray(R.array.type);
        String[] categoriesArray = getResources().getStringArray(R.array.categories);
        String[] subcategoriesArray = getResources().getStringArray(R.array.subCategories);

        spinner_type = (Spinner) findViewById(R.id.Type);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, typeArray);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter_state);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_type.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_category = (Spinner) findViewById(R.id.Categories);
        adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoriesArray);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_state);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_category.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sub_category = (Spinner) findViewById(R.id.SubCategories);

        adapter_state = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subcategoriesArray);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sub_category.setAdapter(adapter_state);
        spinner_sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_sub_category.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void implementButtons() {
        newad_getImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
                alertBuilder.setMessage("Select image from : ").setCancelable(true).setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 0);
                    }
                }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });

        newad_postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postButtonClicked();
            }
        });

    }

    private void postButtonClicked() {
        if(!Constants.isConnected(this)){
            Constants.makeToast(this,getString(R.string.network_not_connected),true);
            return;
        }
        if(spinner_type.getSelectedItem().toString().equals("Choose a Type") || spinner_category.getSelectedItem().toString().equals("Choose a Category") || spinner_sub_category.getSelectedItem().toString().equals("Choose a Sub Category") || newad_adTitle.getText().toString().equals("") || newad_adPrice.getText().toString().equals("")){
            Constants.makeToast(this, "All fields are required!", true);
        }else {
            Constants.showProgressDialogue(progressDialog,"Posting your ad","Please wait...");
            progressDialog.setCancelable(false);

            String userid = sessionManager.getUserId();
            String adType = spinner_type.getSelectedItem().toString();
            String adCategory = spinner_category.getSelectedItem().toString();
            String adSubcategory = spinner_sub_category.getSelectedItem().toString();
            String adTitle = newad_adTitle.getText().toString();
            String adDescription = newad_adDescpription.getText().toString();
            String adPrice = newad_adPrice.getText().toString();

            if(adDescription==null){
                adDescription = "";
            }

            HashMap<String, String> adInfo = new HashMap<>();
            adInfo.put("userid", userid);
            adInfo.put("adType", adType);
            adInfo.put("adCategory", adCategory);
            adInfo.put("adSubcategory", adSubcategory);
            adInfo.put("adTitle", adTitle);
            adInfo.put("adDescription", adDescription);
            adInfo.put("adPrice", adPrice);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.URL_POST_AD, new JSONObject(adInfo), new Response.Listener<JSONObject>() {
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
                    gotResponse(isDone, replyMsg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Constants.closeProgressDialogue(progressDialog);
                    Constants.makeToast(activity,"Network Error",true);
                    Log.e("Volley Error", volleyError.toString());
                }
            });
            VolleyController.getInstance().addNewToRequestQueue(jsonObjectRequest,VOLLEYTAG);
        }
    }

    private void gotResponse(Boolean isDone, String replyMsg) {

        if(isDone){
            Constants.makeToast(this,"Post Successful.\nRedirecting to Account Page...",false);
            Intent in = new Intent(this, AccountActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        }else{
            //setting the statusText as the error replied from server
            Constants.makeToast(this, replyMsg, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int outWidth = point.x;

        if(requestCode==0 && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if(bitmap != null){
                newad_getImageButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap,outWidth,400,true));
            }else {
                Constants.makeToast(this,"Error fetching picture from camera!",true);
            }
        }

        if(requestCode==1 && resultCode==RESULT_OK){
            currImageURI = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(getRealPathFromURI(currImageURI));
            if(bitmap != null){
                newad_getImageButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap, outWidth, 400, true));
            }else{
                Constants.makeToast(this,"Error fetching picture from gallery!",true);
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj={MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = managedQuery( contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void findViewsById() {
        newad_getImageButton = (ImageButton) findViewById(R.id.newad_imagebutton_getimage);
        newad_postButton = (Button) findViewById(R.id.newad_button_post);

        newad_adTitle = (EditText) findViewById(R.id.newad_edittext_adtitle);
        newad_adDescpription = (EditText) findViewById(R.id.newad_edittext_description);
        newad_adPrice = (EditText) findViewById(R.id.newad_edittext_price);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("VOLLEY", "Volley cancel all called");
        VolleyController.getInstance().cancelAllRequest(VOLLEYTAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

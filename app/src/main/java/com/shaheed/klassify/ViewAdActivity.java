package com.shaheed.klassify;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


public class ViewAdActivity extends ActionBarActivity {

    private TextView viewad_adTitle,viewad_description,viewad_category,viewad_subCategory,viewad_bdtPrice,viewad_posted,viewad_type;

    private NetworkImageView viewad_networkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);

        if(Constants.ad == null){
            Constants.makeToast(this,"No ad found!", true);
            Intent intent = new Intent(this,StartingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;

        }

        findViewsById();

        applyView();
    }

    private void applyView() {
        viewad_adTitle.setText(Constants.ad.getAdTitle());
        viewad_description.setText("Description : "+Constants.ad.getAdDescription());
        viewad_category.setText("Category : " + Constants.ad.getAdCategory());
        viewad_subCategory.setText("Sub Category : "+Constants.ad.getAdSubCategory());
        viewad_bdtPrice.setText("BDT "+Constants.ad.getAdPrice());
        viewad_posted.setText("This ad was posted by "+Constants.ad.getAdOwner());
        viewad_type.setText("Type : "+Constants.ad.getAdType());

        viewad_networkImageView.setImageUrl(Constants.ad.getAdThumbImageLink(), VolleyController.getInstance().getImageLoader());

    }

    private void findViewsById() {
        viewad_adTitle = (TextView) findViewById(R.id.viewad_textview_adtitle);
        viewad_bdtPrice = (TextView) findViewById(R.id.viewad_textview_price);
        viewad_description = (TextView) findViewById(R.id.viewad_textview_description);
        viewad_category = (TextView) findViewById(R.id.viewad_textview_category);
        viewad_subCategory = (TextView) findViewById(R.id.viewad_textview_subcategory);
        viewad_posted = (TextView) findViewById(R.id.viewad_textview_posted);
        viewad_type = (TextView) findViewById(R.id.viewad_textview_type);

        viewad_networkImageView = (NetworkImageView) findViewById(R.id.viewad_networkimageview_loading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_share) {
            String shareMsg = "";
            shareMsg = viewad_adTitle.getText().toString() +
                    " " + viewad_type.getText().toString() +
                    " at " + viewad_bdtPrice.getText().toString() +
                    " \n "+ Constants.ad.getAdThumbImageLink();
            handleShare(shareMsg);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleShare(String msg) {
        if(msg == null || msg.equals("")){
            msg = getString(R.string.share_default_message);
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(shareIntent, "Share with..."));
    }
}

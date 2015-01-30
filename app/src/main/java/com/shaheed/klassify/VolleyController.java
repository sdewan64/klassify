package com.shaheed.klassify;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Shaheed on 1/20/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */

public class VolleyController extends Application {

    private String Tag = "Default Tag";

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private static VolleyController instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }

    public static synchronized VolleyController getInstance(){
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue ==null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addNewToRequestQueue(Request<T> request, String tag){
        this.Tag = tag;
        request.setTag(Tag);
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if(imageLoader == null){
            imageLoader = new ImageLoader(this.requestQueue, new VolleyBitmapCacher());
        }
        return this.imageLoader;
    }

    public void cancelAllRequest(Object tag){
        if(requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

}

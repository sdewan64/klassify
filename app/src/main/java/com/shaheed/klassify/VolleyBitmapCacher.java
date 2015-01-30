package com.shaheed.klassify;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Shaheed on 1/31/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class VolleyBitmapCacher extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{
    public static int getDefaultLruCacheSize(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;

        return cacheSize;
    }

    public VolleyBitmapCacher(){
        this(getDefaultLruCacheSize());
    }

    public VolleyBitmapCacher(int sizeInKiloBytes){
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value){
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url){
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap){
        put(url,bitmap);
    }

}

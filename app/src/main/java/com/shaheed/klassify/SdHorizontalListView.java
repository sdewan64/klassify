package com.shaheed.klassify;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created by Shaheed on 1/27/2015.
 * Shaheed Ahmed Dewan Sagar
 * Ahsanullah University of Science & Technology
 * Email : sdewan64@gmail.com
 */
public class SdHorizontalListView {

    private HashMap<Integer, Object> listData;

    protected Context context;
    protected HorizontalScrollView horizontalScrollView;

    protected LinearLayout outerLinearLayout;

    /**
     * Initializing the scrollable list view
     * @param context
     * @param horizontalScrollView
     */
    public SdHorizontalListView(Context context, HorizontalScrollView horizontalScrollView, HashMap<Integer, Object> listData){
        this.horizontalScrollView = horizontalScrollView;
        this.context = context;
        this.listData =  listData;

        outerLinearLayout = new LinearLayout(context);
        horizontalScrollView.addView(outerLinearLayout);
    }

    /**
     * It will generate a view of a drawbale and text(aligned center) and add it to the list and also will update the list.
     * @param text
     * @param bindValue
     * @param onClickListener
     * @throws NullPointerException
     */
    public void addNewImageTextItem(String imageLocation,ImageLoader imageLoader,String text,Object bindValue,View.OnClickListener onClickListener) throws NullPointerException {
        if(context==null || horizontalScrollView==null) {
            throw new NullPointerException("Class has not been initialized yet");
        }

        LinearLayout innerLinearLayout = new LinearLayout(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        innerLinearLayout.setLayoutParams(layoutParams);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);

        NetworkImageView imageView = new NetworkImageView(context);
        imageView.setImageUrl(imageLocation,imageLoader);

        Integer newId = generateNewId();
        imageView.setId(newId);

        imageView.setOnClickListener(onClickListener);

        listData.put(newId,bindValue);

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setId(generateNewId());
        textView.setGravity(Gravity.CENTER);
        textView.setTextAppearance(context,R.style.Base_TextAppearance_AppCompat_Small);

        innerLinearLayout.addView(imageView);
        innerLinearLayout.addView(textView);

        outerLinearLayout.addView(innerLinearLayout);
    }

    /**
     * It will generate a unique Id depending on free id's for the image view which will help identifying clicks.
     * @return Integer
     */
    private Integer generateNewId(){
        final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

        if (Build.VERSION.SDK_INT < 17) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

}

package com.timemachine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.timemachine.database.DBEventRepository;
import com.timemachine.helper.Constants;
import com.timemachine.helper.Logger;
import com.timemachine.model.DBEvent;

/**
 * Created by Jeri on 13/07/2014.
 */
public class ImageViewerActivity extends BaseTabActivity {
    private final String TAG = "ImageViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_viewer);

        // load the information
        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            // get the message
            int id = extras.getInt(Constants.ARGS_EVENT_ID);

            if(id > 0){
                DBEventRepository eventRepo = new DBEventRepository(this);
                DBEvent event = eventRepo.get(id);
                ImageView iv = (ImageView)findViewById(R.id.imageView);

                imageLoader.displayImage(event.getHighreslink(), iv, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    }
                });
            }else{
                exitActivity();
            }
        }

        // reset as GB always has this on
        setLoading(false, TAG);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        exitActivity();
    }

    private void exitActivity(){

        finish();
        overridePendingTransition  (R.anim.in_from_right, R.anim.out_to_left);
    }
}

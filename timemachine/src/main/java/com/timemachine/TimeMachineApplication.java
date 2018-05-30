package com.timemachine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Jeri on 4/17/2014.
 */
public class TimeMachineApplication extends Application {
    private Activity mCurrentActivity;
    private boolean mIsApplicationActive = false;

    public TimeMachineApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                        //.showImageOnLoading(R.drawable.ic_darkgray)
                        //.displayer(new RoundedBitmapDisplayer(12))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)

                .build();
        ImageLoader.getInstance().init(config);

        //.writeDebugLogs()
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setIsApplicationActive(boolean isApplicationActive) {
        mIsApplicationActive = isApplicationActive;
    }

    public boolean getIsApplicationActive() { return mIsApplicationActive; }
}

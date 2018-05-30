package com.timemachine;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timemachine.helper.Logger;

/**
 * Created by Jeri on 16/05/2014.
 */
public class BaseTabActivity extends SherlockFragmentActivity {
    protected BaseTabActivity mActivity;
    private final String TAG = "BaseActivity";
    private TimeMachineApplication mApplication;
    public ImageLoader imageLoader;

    public int width = 0;
    public int height = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_INDETERMINATE_PROGRESS);

        mActivity = this;
        this.setTitle("");
        //LatershotLogger.i(TAG, this.toString());
        mApplication = (TimeMachineApplication) getApplication();

        // get the size
        Display display = getWindowManager().getDefaultDisplay();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB){
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }
        else{
            try {
                width = display.getWidth();  // deprecated
                height = display.getHeight(); // deprecated
            }catch(Exception ex){
                Logger.e(TAG, "Get Width no longer supported : " + ex.getMessage());
            }
        }

        imageLoader = ImageLoader.getInstance();
    }

    String tabLoading = "";

    protected void setLoading(final boolean isLoading, String tab)
    {
        try {
/*            if (tabLoading.length() > 0 && !tabLoading.equalsIgnoreCase(tab)) {
                return;
            }*/

            this.runOnUiThread(new Runnable() {
                public void run() {
                    // show loading progress bar
                    setSupportProgressBarIndeterminateVisibility(isLoading);

                    // hide the rest of the menu
                    //setMenuVisibility(!isLoading);

                    if (!isLoading)
                        tabLoading = "";
                }
            });
        }
        catch(Exception ex)
        {
            //LatershotLogger.e("FragmentTabBase", ex.getMessage());
        }
    }

    public void setTabTitle(String title){
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
        getSupportActionBar().setTitle(title);
    }
}

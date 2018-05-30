package com.timemachine.fragment;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jeri on 5/17/2014.
 */
public class FragmentTabBase extends SherlockFragment {
    String tabLoading = "";
    boolean focused = false;
    protected ImageLoader imageLoader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageLoader = ImageLoader.getInstance();
    }

    protected void setLoading(final boolean isLoading, String tab)
    {
        try {
            if (tabLoading.length() > 0 && !tabLoading.equalsIgnoreCase(tab)) {
                return;
            }

            final SherlockFragmentActivity sherlockActivity = getSherlockActivity();
            sherlockActivity.runOnUiThread(new Runnable() {
                public void run() {
                    // show loading progress bar
                    sherlockActivity
                            .setSupportProgressBarIndeterminateVisibility(isLoading);

                    // hide the rest of the menu
                    setMenuVisibility(!isLoading);

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

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}

package com.timemachine.adapter;

/**
 * Created by Jeri on 12/07/2014.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.timemachine.R;
import com.timemachine.TimeMachineMain;
import com.timemachine.database.DBEventRepository;
import com.timemachine.helper.Logger;
import com.timemachine.model.DBEvent;

public class PopupAdapter implements InfoWindowAdapter {
    private TimeMachineMain mContext;
    private View popup=null;
    private LayoutInflater inflater=null;
    private Marker markerShowingInfoWindow;

    public PopupAdapter(LayoutInflater inflater, TimeMachineMain context) {
        this.inflater=inflater;
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        markerShowingInfoWindow = marker;
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }

        DBEventRepository eventRepo = new DBEventRepository(mContext);

        int id = 0;

        try{
            id = Integer.parseInt(marker.getSnippet());
        }
        catch(Exception ex){

        }


        if(id > 0) {
            DBEvent event = eventRepo.get(id);
            mContext.currentEvent = id;

            TextView tv = (TextView) popup.findViewById(R.id.title);
            tv.setText(event.getTitle());
            tv = (TextView) popup.findViewById(R.id.snippet);
            tv.setText(event.getCaption());

            final ImageView iv = (ImageView) popup.findViewById(R.id.image);
            //final ProgressBar progressBar = (ProgressBar)popup.findViewById(R.id.progressBar);

            //progressBar.setVisibility(View.VISIBLE);
            iv.setVisibility(View.VISIBLE);

            mContext.imageLoader.displayImage(event.getHighreslink(), iv, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    Logger.d("POPUP", "Loaded Image : " +bitmap.getByteCount() + " bytes");
                    //progressBar.setVisibility(View.INVISIBLE);
                    //iv.setVisibility(View.VISIBLE);

                    if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {

                        markerShowingInfoWindow.hideInfoWindow();
                        markerShowingInfoWindow.showInfoWindow();
                    }
                }
            });
        }
        else {
            final ImageView iv = (ImageView) popup.findViewById(R.id.image);
            iv.setVisibility(View.GONE);
            TextView tv = (TextView) popup.findViewById(R.id.title);
            tv.setText(marker.getTitle());
            tv = (TextView) popup.findViewById(R.id.snippet);
            tv.setText(marker.getSnippet());
        }

        float zoom_lvl = mContext.mMap.getCameraPosition().zoom;
        double dpPerdegree = 256.0*Math.pow(2, zoom_lvl)/170.0;
        double screen_height = (double) mContext.height;
        double screen_height_30p = 30.0*screen_height/100.0;
        double degree_30p = screen_height_30p/dpPerdegree;
        LatLng centerlatlng = new LatLng( marker.getPosition().latitude+ degree_30p, marker.getPosition().longitude );
        mContext.mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(centerlatlng, mContext.mMap.getCameraPosition().zoom), 1000, null);

        return(popup);
    }

    /**
     * This method is called after the bitmap has been loaded. It checks if the currently displayed
     * info window is the same info window which has been saved. If it is, then refresh the window
     * to display the newly loaded image.
     */
    private Callback onImageLoaded = new Callback() {

        @Override
        public void execute(String result) {

        }
    };


    public interface Callback {

        public void execute(String result);

    }
}
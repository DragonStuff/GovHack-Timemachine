package com.timemachine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.timemachine.adapter.PopupAdapter;
import com.timemachine.database.DBEventRepository;
import com.timemachine.helper.Constants;
import com.timemachine.helper.Logger;
import com.timemachine.helper.MultiDrawable;
import com.timemachine.helper.MyUtils;
import com.timemachine.model.DBEvent;
import com.timemachine.model.Event;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jeri on 12/07/2014.
 */
public class TimeMachineMain extends BaseTabActivity implements LocationListener, ClusterManager.OnClusterClickListener<Event>, ClusterManager.OnClusterInfoWindowClickListener<Event>, ClusterManager.OnClusterItemClickListener<Event>, ClusterManager.OnClusterItemInfoWindowClickListener<Event>, GoogleMap.OnInfoWindowClickListener {
    private final String TAG = "TimeMachineMain";
    Context mContext;
    private ActionBar actionBar;


    public GoogleMap mMap;
    private LocationManager locationManager;
    private LatLng latLng;
    private LatLng myPos;
    private Marker myMarker;
    SeekBar seekBar;
    TextView lblSelectRange;
    TextView lblLoadedData;
    RelativeLayout progressLayout;
    Animation slideIn;
    Animation slideOut;
    boolean isSeeking = false;
    SlidingMenu menu;
    int start = 1860;
    PopupAdapter popup;
    List<DBEvent> events;
    boolean buildingOn = true;
    boolean landscapesOn = true;
    boolean newsOn = true;
    boolean crimeOn = true;
    public int currentEvent = 0;

    // Declare a variable for the cluster manager.
    private ClusterManager<Event> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar();
        setSupportProgressBarIndeterminateVisibility(false);

        setContentView(R.layout.activity_time_machine);

        slideIn = AnimationUtils.loadAnimation(this, R.anim.in_from_right);
        slideOut = AnimationUtils.loadAnimation(this, R.anim.out_to_left);


        // get controls
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        lblSelectRange = (TextView)findViewById(R.id.lblSelectRange);
        lblLoadedData = (TextView)findViewById(R.id.lblLoadedData);
        progressLayout = (RelativeLayout)findViewById(R.id.progressLayout);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                start = 1860 + i * 5;
                Calendar cal = Calendar.getInstance();

                lblSelectRange.setText(String.format("%s - %s", start, (cal.get(Calendar.YEAR) < start+9)?"current":start+9));

                if(lblSelectRange.getVisibility() == View.INVISIBLE && isSeeking) {
                    lblSelectRange.startAnimation(slideIn);
                    lblSelectRange.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
                Logger.d(TAG, "Seeking : " + isSeeking);
                //Toast.makeText(mContext, "Progress : " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
                Logger.d(TAG, "Seeking : " + isSeeking);
                //lblSelectRange.setAnimation(fadeOutLblSelectRange);
                lblSelectRange.startAnimation(slideOut);
                lblSelectRange.setVisibility(View.INVISIBLE);

                //lblSelectRange.setVisibility(View.INVISIBLE);
                //lblSelectRange.setText("");
                loadMapData();
            }
        });

        // reset as GB always has this on
        setLoading(false, TAG);
        setTabTitle(getString(R.string.app_name));

        //startTracking();
        setDefaultLocation();

        configureSideBar();

        showHelp();
    }

    private void showHelp(){
        SharedPreferences prefs = this.getSharedPreferences(
                "com.timemachine", Context.MODE_PRIVATE);


        String ft = "com.latershot.firsttime";

        // has this been used before
        boolean firsttime = prefs.getBoolean(ft, false);

        if(!firsttime){
            prefs.edit().putBoolean(ft, true).apply();
            StringBuilder str = new StringBuilder();
            str.append("1. Data is only available between 1860 and 1930\r\n");
            str.append("2. Use the SLIDER at the bottom of the Map to change your Time Line\r\n");
            str.append("3. Data with the Building Icons has been taken from State Library\r\n");
            str.append("4. Data with the Mountain Icons has been taken from State Archives\r\n");
            str.append("5. Data with the Newspaper Icons is just dummy data but it will be retrieving Newspaper articles from Trove\r\n");
            str.append("6. Data with Criminal Icons is just dummy data but it will be retrieving Heatmap data from NSW Crime Tool\r\n");

            // show help dialog
            Logger.showSimpleErrorDialog(this, "Hints",  str.toString());

            str = new StringBuilder();
            str.append("Use the SLIDER at the bottom of the Map to change your Time Line\r\n");

            // show help dialog
            Logger.showSimpleErrorDialog(this, "Hints",  str.toString());

            // load the data
            loadData();

        }

    }

    private void configureSideBar(){
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void toggleEventType(int type, boolean toggle)
    {
        //menu.toggle(true);

        switch(type)
        {
            case 0:
                buildingOn = toggle;
                break;
            case 1:
                landscapesOn = toggle;
                break;
            case 2:
                newsOn = toggle;
                break;
            case 3:
                crimeOn = toggle;
                break;
        }

        loadMapData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        com.actionbarsherlock.view.MenuInflater i = getSupportMenuInflater();
        i.inflate(R.menu.home_menu, (com.actionbarsherlock.view.Menu) menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_load_database) {
            loadData();
        } else
        if(item.getItemId() == android.R.id.home) {
            menu.toggle(true);
        }
        return true;
    }

    private void loadData(){
        // start loading
        progressLayout.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Integer, List<DBEvent>>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                lblLoadedData.setText(String.format("Loaded %s Records", values[0]));
            }

            @Override
            protected List<DBEvent> doInBackground(Void ... params) {
                List<DBEvent> result = new ArrayList<DBEvent>();
                int totalLoaded = 0;

                try {

                    // load the data gov dataset
                    AssetManager assetManager = getAssets();
                    InputStream ims = assetManager.open("datagov.txt");

                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(ims);

                    DBEvent[] events = gson.fromJson(reader, DBEvent[].class);
                    List<DBEvent> eventList = Arrays.asList(events);

                    DBEventRepository eventRepo = new DBEventRepository(mContext);
                    eventRepo.deleteAll();
                    for(DBEvent ev:eventList)
                    {

                        ev.setIcon(0);
                        eventRepo.create(ev);
                        publishProgress(totalLoaded++);
                    }

                    // load the state record dataset
                    ims = assetManager.open("staterecord.txt");

                    reader = new InputStreamReader(ims);

                    events = gson.fromJson(reader, DBEvent[].class);
                    eventList = Arrays.asList(events);

                    for(DBEvent ev:eventList)
                    {
                        ev.setIcon(1);
                        eventRepo.create(ev);
                        publishProgress(totalLoaded++);
                    }

                    // load the trove dataset

                }catch(IOException e) {
                    e.printStackTrace();
                }



                return result;
            }

            @Override
            protected void onPostExecute(List<DBEvent> result) {
                progressLayout.setVisibility(View.INVISIBLE);

                loadMapData();
            };

            @Override
            protected void onCancelled() {
                setLoading(false, TAG);
            }
        }.execute();

    }

    private void loadBuildingsData(){
        DBEventRepository eventRepo = new DBEventRepository(this);
        events = eventRepo.getAll(start, 0);

        for(DBEvent event:events)
        {
            if(event.getLatitude() != 0){
                int mainImage = R.drawable.ic_buildings;
                String type = event.getTitle();
                String desc = ""  + event.getId();
                mClusterManager.addItem(new Event(new LatLng(event.getLatitude(), event.getLongitude()), type, desc, mainImage, event.getId() ));
            }
        }
    }

    private void loadLandscapesData(){
        DBEventRepository eventRepo = new DBEventRepository(this);
        events = eventRepo.getAll(start, 1);

        for(DBEvent event:events)
        {
            if(event.getLatitude() != 0){
                int mainImage = R.drawable.ic_landscapes;
                String type = event.getTitle();
                String desc = ""  + event.getId();
                mClusterManager.addItem(new Event(new LatLng(event.getLatitude(), event.getLongitude()), type, desc, mainImage, event.getId() ));
            }
        }
    }

    private void loadMapData(){
        //mMap.clear();
        mClusterManager.clearItems();

        // load buildings item
        if(buildingOn)
            loadBuildingsData();

        // load landscapes
        if(landscapesOn)
            loadLandscapesData();

        // need to generate many markers for the current map

        // Set some lat/lng coordinates to start with.
        double lat = -33.866247;
        double lng = 151.210054;
        int image = 0;


        // Add ten cluster items in close proximity, for purposes of this example.
        Logger.d(TAG, "Started Loading Random Points");
        for (int i = 0; i < MyUtils.randInt(20, 40); i++) {
            double offset = i / 100000d;
            boolean latPlus = ((Math.random() * 100) > 50)?true:false;
            boolean lngPlus = ((Math.random() * 100) > 50)?true:false;

            if(latPlus)
                lat = lat + (offset * (Math.random() * 100));
            else
                lat = lat - (offset * (Math.random() * 100));

            if(lngPlus)
                lng = lng + (offset * (Math.random() * 100));
            else
                lng = lng - (offset * (Math.random() * 100));

            //MyItem offsetItem = new MyItem(lat, lng);
            //mClusterManager.addItem(offsetItem);

            int mainImage = 0;
            String type = "";
            String desc = "";

            switch(image % 4)
            {
/*                case 0:
                    mainImage = R.drawable.ic_buildings;
                    type = "Establishment";
                    desc = String.format("New establishment built between %s-%s", start, start + 9);
                    break;*/
                case 1:
                    if(crimeOn) {
                        mainImage = R.drawable.ic_criminal;
                        type = "Crime";
                        desc = String.format("Crime commited between between %s-%s", start, start + 9);
                        mClusterManager.addItem(new Event(new LatLng(lat, lng), type, desc, mainImage, 0));
                    }

                    break;
                case 2:
/*                    if(landscapesOn) {
                        mainImage = R.drawable.ic_landscapes;
                        type = "Roads";
                        desc = String.format("New roads built between %s-%s", start, start + 9);
                        mClusterManager.addItem(new Event(new LatLng(lat, lng), type, desc, mainImage, 0));
                    }*/
                    break;
                case 3:
                    if(newsOn) {
                        mainImage = R.drawable.ic_news;
                        type = "News";
                        desc = String.format("News published between %s-%s", start, start + 9);
                        mClusterManager.addItem(new Event(new LatLng(lat, lng), type, desc, mainImage, 0));
                    }
                    break;
            }

            image++;

        }

        mClusterManager.cluster();
        Logger.d(TAG, "Finished Loading Random Points");
    }

    private void setDefaultLocation(){
        setLocation(new LatLng(-33.866247, 151.210054));
    }

    private void startTracking(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.MIN_TIME, Constants.MIN_DISTANCE, this);
        Logger.d(TAG, "Enabled Message Tracking via GPS");
    }

    public void setLocation(LatLng latLng){
        if(mMap == null){
            FragmentManager fmanager = getSupportFragmentManager();
            Fragment fragment = fmanager.findFragmentById(R.id.mapView);
            SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
            mMap = supportmapfragment.getMap();
            // Enabling MyLocation Layer of Google Map
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);


            // Initialize the manager with the context and the map.
            // (Activity extends context, so we can pass 'this' in the constructor.)
            mClusterManager = new ClusterManager<Event>(this, mMap);


            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            mClusterManager.setRenderer(new EventRenderer(mMap));
            mMap.setOnCameraChangeListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);
            mMap.setOnInfoWindowClickListener(mClusterManager);
/*            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    if(popup != null)
                        popup.closeInfoWindow();

                }
            });*/
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
            popup = new PopupAdapter(getLayoutInflater(), this);
            mMap.setInfoWindowAdapter(popup);
            mMap.setOnInfoWindowClickListener(this);

            // Add cluster items (markers) to the cluster manager.
            loadMapData();

        }

        myPos = latLng;

        if(myPos != null)
        {
            // always clear the map
            mMap.clear();
/*            if(myMarker == null) {
                String username = "You";
                myMarker = mMap.addMarker(new MarkerOptions().title(username).position(myPos).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            else
            {
                myMarker.setPosition(latLng);
            }

            myMarker.showInfoWindow();*/
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos, 8));

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        boolean refresh = true;

        if(latLng != null){
            Location loc = new Location(location.getProvider());
            loc.setLatitude(latLng.latitude);
            loc.setLongitude(latLng.longitude);

            refresh = MyUtils.isBetterLocation(location, loc);
        }

        if(refresh){
            // update list only if lat long is better
            //aa.notifyDataSetInvalidated();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());

            setLocation(latLng);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, ImageViewerActivity.class);
        intent.putExtra(Constants.ARGS_EVENT_ID, currentEvent);
        //intent.putExtra(Constants.ARGS_MESSAGE_COMMENT_COUNT, msg.getComments());
        startActivity(intent);
        overridePendingTransition  (R.anim.in_from_right, R.anim.out_to_left);
        marker.hideInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(locationManager != null) {
            Logger.d(TAG, "Stopping GPS Tracking");
            locationManager.removeUpdates(this);
        }
    }

    public void closeInfoWindow(View view){
        Logger.d(TAG, "Closing Info Window");
    }

    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Event> cluster) {

        if((mMap.getCameraPosition().zoom + 1) < mMap.getMaxZoomLevel())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), mMap.getCameraPosition().zoom + 1));

        /*// Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();*/
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Event> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Event item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Event item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    private class EventRenderer extends DefaultClusterRenderer<Event> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public EventRenderer(GoogleMap mMap) {
            super(getApplicationContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(final Event Event, final MarkerOptions markerOptions) {
            // Draw a single Event.
            // Set the info window to show their name.
            mImageView.setImageResource(Event.profilePhoto);
            if(Event.id > 0)
            {
                // get the event
                DBEventRepository eventRepo = new DBEventRepository(mContext);
                DBEvent event = eventRepo.get(Event.id);

                // Load image, decode it to Bitmap and return Bitmap to callback
                Bitmap icon = mIconGenerator.makeIcon();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(Event.name).snippet(Event.description);


                ImageSize targetSize = new ImageSize(36, 36); // result Bitmap will be fit to this size
                imageLoader.loadImage(event.getThumbnaillink(), targetSize, null, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Logger.d(TAG, "Loaded Icon : " +loadedImage.getByteCount() + " bytes");
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(loadedImage)).title(Event.name).snippet(Event.description);
                        mClusterManager.cluster();
                    }
                });
                //Bitmap bmp = imageLoader.loadImageSync(Event.pictureThumb);
                //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp)).title(Event.name).snippet(Event.description);

                Logger.d(TAG, "Event : " + event.getId());
            }
            else {
                Bitmap icon = mIconGenerator.makeIcon();
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(Event.name).snippet(Event.description);
            }
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Event> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Event p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(p.profilePhoto);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }
}

package com.timemachine.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Event implements ClusterItem {
    public final String name;
    public final String description;
    public final int profilePhoto;
    public final int  id;
    private final LatLng mPosition;

    public Event(LatLng position, String name, String description, int pictureResource, int id) {
        this.name = name;
        this.description = description;
        profilePhoto = pictureResource;
        this.id = id;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

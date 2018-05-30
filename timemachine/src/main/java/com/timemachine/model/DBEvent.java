package com.timemachine.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Jeri on 12/07/2014.
 */
@DatabaseTable(tableName = "event")
public class DBEvent {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateofwork() {
        return dateofwork;
    }

    public void setDateofwork(String dateofwork) {
        this.dateofwork = dateofwork;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHighreslink() {
        return highreslink;
    }

    public void setHighreslink(String highreslink) {
        this.highreslink = highreslink;
    }

    public String getThumbnaillink() {
        return thumbnaillink;
    }

    public void setThumbnaillink(String thumbnaillink) {
        this.thumbnaillink = thumbnaillink;
    }

    public int getDecade() {
        return decade;
    }

    public void setDecade(int decade) {
        this.decade = decade;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    /*

        "itemid":412069,
    "title":"Sydney - photographs of streets, public buildings, views in the Harbour, suburbs etc., chiefly pre 1885",
    "caption":"The City Bank [corner of King & George Streets, Sydney, ca. 1870s]",
    "creator":"NULL",
    "albumnumber":412050,
    "dateofwork":"[ca. 1870s]",
    "collectionitemlink":"http://acms.sl.nsw.gov.au/item/itemdetailpaged.aspx?itemid=412050",
    "digitalitemlink":"http://acms.sl.nsw.gov.au/item/itemdetailpaged.aspx?itemid=412069",
    "digitalordernumber":"a089002",
    "thumbnaillink":"http://acms.sl.nsw.gov.au/_DAMt/image/18/122/a089002t.jpg",
    "highreslink":"http://acms.sl.nsw.gov.au/_DAMx/image/18/122/a089002h.jpg",
    "zoomablelink":"NULL",
    "albumorder":"1",
    "Address":"",
    "Filtered_Address":"",
    "Lat":null,
    "Lon":null,
    "Decade":1870

     */

    @com.google.gson.annotations.SerializedName("itemid")
    @DatabaseField(generatedId = true)
    private int id;

    @com.google.gson.annotations.SerializedName("id")
    @DatabaseField
    private String key;

    @com.google.gson.annotations.SerializedName("title")
    @DatabaseField
    private String title;

    @com.google.gson.annotations.SerializedName("caption")
    @DatabaseField
    private String caption;

    @com.google.gson.annotations.SerializedName("dateofwork")
    @DatabaseField
    private String dateofwork;

    @com.google.gson.annotations.SerializedName("Lat")
    @DatabaseField
    private double latitude;

    @com.google.gson.annotations.SerializedName("Lon")
    @DatabaseField
    private double longitude;

    @com.google.gson.annotations.SerializedName("highreslink")
    @DatabaseField
    private String highreslink;

    @com.google.gson.annotations.SerializedName("thumbnaillink")
    @DatabaseField
    private String thumbnaillink;

    @com.google.gson.annotations.SerializedName("Decade")
    @DatabaseField
    private int decade;

    @com.google.gson.annotations.SerializedName("icon")
    @DatabaseField
    private int icon;

    public DBEvent() {

    }
}

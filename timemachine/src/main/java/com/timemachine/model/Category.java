package com.timemachine.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Created by Jeri on 12/07/2014.
 */
public class Category {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @com.google.gson.annotations.SerializedName("categoryId")
    @DatabaseField(generatedId = true)
    private int id;

    @com.google.gson.annotations.SerializedName("id")
    @DatabaseField
    private String key;

    @com.google.gson.annotations.SerializedName("name")
    @DatabaseField
    private String name;

    transient int total;

    public Category() {
        // ORMLite needs a no-arg constructor
        total = 0;
    }
}

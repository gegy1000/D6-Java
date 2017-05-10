package net.gegy1000.communicator.model;

import com.google.gson.annotations.SerializedName;

public class FeedModel {
    private long id;
    private String name;
    private String token;
    private String logo;
    @SerializedName("private")
    private int isPrivate;
    private String country;
    private String provice;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getToken() {
        return this.token;
    }

    public String getLogo() {
        return this.logo;
    }

    public int isPrivate() {
        return this.isPrivate;
    }

    public String getCountry() {
        return this.country;
    }

    public String getProvice() {
        return this.provice;
    }
}

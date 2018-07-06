package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 3/9/2018.
 */

public class UserDetailResultValue {
    public double getCreateDate() {
        return createDate;
    }

    public double getModifiedDate() {
        return modifiedDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getDevice() {
        return device;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isActive() {
        return active;
    }

    @SerializedName("createDate")
    private double createDate;

    @SerializedName("modifiedDate")
    private double modifiedDate;

    @SerializedName("userId")
    private int userId;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("device")
    private String device;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("platform")
    private String platform;

    @SerializedName("active")
    private boolean active;
}

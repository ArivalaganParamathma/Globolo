package com.idl.globolo.POJO;

/**
 * Created by arivalagan on 3/9/2018.
 */

public class UserIdInput {
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    private String createDate;
    private String device;
    private String deviceId;
    private String modifiedDate;
    private String platform;
    private int userId;
    private String userToken;
    private boolean active;

    public void setActive(boolean active) {
        this.active = active;
    }
}

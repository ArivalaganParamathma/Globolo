package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 3/9/2018.
 */

public class UserDetailResult {
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public UserDetailResultValue getResultValue() {
        return resultValue;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("resultValue")
    private UserDetailResultValue resultValue;
}

package com.idl.globolo.pojoFavourite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 4/7/2018.
 */

public class FavouriteResponse {

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public FavouriteResult getResultValue() {
        return resultValue;
    }

    @SerializedName("resultValue")
    private FavouriteResult resultValue;

}

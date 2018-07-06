package com.idl.globolo.pojoFavourite;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 4/7/2018.
 */

public class FavouriteResult {
    public int getFavId() {
        return favId;
    }

    @SerializedName("favId")
    private int favId;

    public String getNumberId() {
        return numberId;
    }

    @SerializedName("numberId")
    private String numberId;
}

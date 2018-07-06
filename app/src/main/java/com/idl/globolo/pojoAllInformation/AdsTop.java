package com.idl.globolo.pojoAllInformation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class AdsTop {
    public int getPrDetailId() {
        return prDetailId;
    }

    public int getPromoId() {
        return promoId;
    }

    public String getPromoImage() {
        return promoImage;
    }

    public int getPrOrderBy() {
        return prOrderBy;
    }

    public boolean isActive() {
        return active;
    }

    public String getUrl() {
        return url;
    }

    @SerializedName("prDetailId")
    private int prDetailId;

    @SerializedName("promoId")
    private int promoId;

    @SerializedName("promoImage")
    private String promoImage;

    @SerializedName("prOrderBy")
    private int prOrderBy;

    @SerializedName("active")
    private boolean active;

    @SerializedName("url")
    private String url;
}

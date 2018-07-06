package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 3/8/2018.
 */

public class MainAdResultValue {
    public int getPrmId() {
        return prmId;
    }

    public String getPrmImage() {
        return prmImage;
    }

    public String getUrl() {
        return url;
    }

    @SerializedName("prmId")
    private int prmId;

    @SerializedName("prmImage")
    private String prmImage;

    @SerializedName("url")
    private String url;
}

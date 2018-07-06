package com.idl.globolo.pojoLanguage;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class LanguageResultValue {
    public double getCreateDate() {
        return createDate;
    }

    public double getModifiedDate() {
        return modifiedDate;
    }

    public int getLangId() {
        return langId;
    }

    public String getLangName() {
        return langName;
    }

    public boolean isActive() {
        return active;
    }

    @SerializedName("createDate")
    private double createDate;

    @SerializedName("modifiedDate")
    private double modifiedDate;

    @SerializedName("langId")
    private int langId;

    @SerializedName("langName")
    private String langName;

    @SerializedName("active")
    private boolean active;
}

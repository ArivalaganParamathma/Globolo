package com.idl.globolo.pojoAllInformation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class Category {
    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public int getLanguageFrom() {
        return languageFrom;
    }

    public int getLanguageTo() {
        return languageTo;
    }


    @SerializedName("catId")
    private int catId;

    @SerializedName("catName")
    private String catName;

    @SerializedName("catImage")
    private String catImage;

    @SerializedName("languageFrom")
    private int languageFrom;

    @SerializedName("languageTo")
    private int languageTo;
}

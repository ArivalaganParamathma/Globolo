package com.idl.globolo.pojoAllInformation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class CategoryList {
    public int getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(int languageFrom) {
        this.languageFrom = languageFrom;
    }

    public int getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(int languageTo) {
        this.languageTo = languageTo;
    }

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public ArrayList<AdsTop> getAdsTop() {
        return adsTop;
    }

    public void setAdsTop(ArrayList<AdsTop> adsTop) {
        this.adsTop = adsTop;
    }

    public ArrayList<AdsBottom> getAdsBottom() {
        return adsBottom;
    }

    public void setAdsBottom(ArrayList<AdsBottom> adsBottom) {
        this.adsBottom = adsBottom;
    }

    @SerializedName("languageFrom")
    private int languageFrom;

    @SerializedName("languageTo")
    private int languageTo;

    @SerializedName("category")
    private ArrayList<Category> category;

    @SerializedName("adsTop")
    private ArrayList<AdsTop> adsTop;

    @SerializedName("adsBottom")
    private ArrayList<AdsBottom> adsBottom;
}

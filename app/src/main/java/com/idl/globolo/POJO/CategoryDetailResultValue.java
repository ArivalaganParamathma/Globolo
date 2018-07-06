package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;
import com.idl.globolo.pojoAllInformation.AdsBottom;
import com.idl.globolo.pojoAllInformation.AdsTop;

import java.util.ArrayList;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class CategoryDetailResultValue {
    public ArrayList<WordsList> getWordsList() {
        return wordsList;
    }

    public ArrayList<AdsTop> getAdsTop() {
        return adsTop;
    }

    public ArrayList<AdsBottom> getAdsBottom() {
        return adsBottom;
    }

    @SerializedName("wordsList")
    private ArrayList<WordsList> wordsList;

    @SerializedName("adsTop")
    private ArrayList<AdsTop> adsTop;

    @SerializedName("adsBottom")
    private ArrayList<AdsBottom> adsBottom;
}

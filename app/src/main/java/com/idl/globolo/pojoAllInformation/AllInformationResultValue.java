package com.idl.globolo.pojoAllInformation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class AllInformationResultValue {
    public ArrayList<CategoryList> getCategoriesList() {
        return categoriesList;
    }

    @SerializedName("categoriesList")
    private ArrayList<CategoryList> categoriesList;
}

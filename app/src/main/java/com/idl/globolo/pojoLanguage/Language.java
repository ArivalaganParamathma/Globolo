package com.idl.globolo.pojoLanguage;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class Language {
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<LanguageResultValue> getResultValue() {
        return resultValue;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("resultValue")
    private ArrayList<LanguageResultValue> resultValue;
}

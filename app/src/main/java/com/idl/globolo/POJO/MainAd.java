package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by arivalagan on 3/8/2018.
 */

public class MainAd {
    public String getStatus() {
        return status;
    }

    public MainAdResultValue getResultValue() {
        return resultValue;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("resultValue")
    private MainAdResultValue resultValue;
}

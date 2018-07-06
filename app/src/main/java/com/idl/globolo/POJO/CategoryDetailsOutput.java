package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class CategoryDetailsOutput {
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public CategoryDetailResultValue getResultValue() {
        return resultValue;
    }

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("resultValue")
    private CategoryDetailResultValue resultValue;
}

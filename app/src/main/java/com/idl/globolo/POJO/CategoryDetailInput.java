package com.idl.globolo.POJO;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class CategoryDetailInput {
    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int catId;
    private int languageFrom;
    private int languageTo;
    private int userId;
}

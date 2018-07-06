package com.idl.globolo.pojoFavourite;

/**
 * Created by arivalagan on 4/7/2018.
 */

public class CreateFavouriteInput {
    private boolean active;
    private int catId;
    private String createDate;
    private int favId;
    private int langFromId;
    private int langToId;
    private String modifiedDate;
    private int numberId;
    private int userId;

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public void setLangFromId(int langFromId) {
        this.langFromId = langFromId;
    }

    public void setLangToId(int langToId) {
        this.langToId = langToId;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

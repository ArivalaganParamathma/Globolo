package com.idl.globolo.POJO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class WordsList {

    @SerializedName("languageFrom")
    private int languageFrom;

    @SerializedName("languageTo")
    private int languageTo;

    @SerializedName("catId")
    private int catId;

    @SerializedName("catName")
    private String catName;

    @SerializedName("catImage")
    private String catImage;

    @SerializedName("wordImage")
    private String wordImage;

    @SerializedName("wordFromName")
    private String wordFromName;

    @SerializedName("wordFromSound")
    private String wordFromSound;

    @SerializedName("wordToName")
    private String wordToName;

    @SerializedName("wordToPronounciation")
    private String wordToPronounciation;

    @SerializedName("wordToSound")
    private String wordToSound;

    @SerializedName("favouriteFlag")
    private String favouriteFlag;

    @SerializedName("favId")
    private String favId;

    @SerializedName("numberId")
    private int numberId;


    public int getLanguageFrom() {
        return languageFrom;
    }
    public int getLanguageTo() {
        return languageTo;
    }

    public int getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public String getWordImage() {
        return wordImage;
    }

    public String getWordFromName() {
        return wordFromName;
    }

    public String getWordFromSound() {
        return wordFromSound;
    }

    public String getWordToName() {
        return wordToName;
    }

    public String getWordToPronounciation() {
        return wordToPronounciation;
    }

    public String getWordToSound() {
        return wordToSound;
    }

    public String getFavouriteFlag() {
        return favouriteFlag;
    }

    public String getFavId() {
        return favId;
    }

    public int getNumberId() {
        return numberId;
    }

    public void setLanguageFrom(int languageFrom) {
        this.languageFrom = languageFrom;
    }
    public void setLanguageTo(int languageTo) {
        this.languageTo = languageTo;
    }
    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    public void setWordImage(String wordImage) {
        this.wordImage = wordImage;
    }

    public void setWordFromName(String wordFromName) {
        this.wordFromName = wordFromName;
    }

    public void setWordFromSound(String wordFromSound) {
        this.wordFromSound = wordFromSound;
    }

    public void setWordToName(String wordToName) {
        this.wordToName = wordToName;
    }

    public void setWordToPronounciation(String wordToPronounciation) {
        this.wordToPronounciation = wordToPronounciation;
    }

    public void setWordToSound(String wordToSound) {
        this.wordToSound = wordToSound;
    }

    public void setFavouriteFlag(String favouriteFlag) {
        this.favouriteFlag = favouriteFlag;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public void setNumberId(int numberId) {
        this.numberId = numberId;
    }
}

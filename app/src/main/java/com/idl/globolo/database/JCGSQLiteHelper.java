package com.idl.globolo.database;

/**
 * Created by arivalagan on 7/7/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.commonPojos.LanguageList;
import com.idl.globolo.pojoAllInformation.AdsBottom;
import com.idl.globolo.pojoAllInformation.AdsTop;
import com.idl.globolo.pojoAllInformation.Category;
import com.idl.globolo.pojoLanguage.Language;
import com.idl.globolo.pojoLanguage.LanguageResultValue;
import com.idl.globolo.pojoViewAdapter.pojoAds;
import com.idl.globolo.pojoViewAdapter.pojoCategory;

import java.util.ArrayList;


public class JCGSQLiteHelper extends SQLiteOpenHelper {

    // database version
    private static final int database_VERSION = 1;
    // database name
    private static final String database_NAME = "Globol";
    private static final String table_name_language = "Language";
    private static final String table_name_category = "category";
    private static final String table_name_words = "words";
    private static final String table_name_adTop = "adTop";
    private static final String table_name_adBottom = "adBottom";


    public JCGSQLiteHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table

        String CREATE_LANGUAGE_TABLE = "CREATE TABLE Language ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "langId INTEGER, "
                + "langName TEXT, "
                + "active INTEGER )";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE category ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "catId INTEGER, "
                + "catName TEXT, "
                + "catImage TEXT, "
                + "languageFrom INTEGER, "
                + "languageTo INTEGER )";

        String CREATE_WORDS_TABLE = "CREATE TABLE words ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "catId INTEGER, "
                + "catName TEXT, "
                + "catImage TEXT, "
                + "wordImage TEXT, "
                + "wordFromName TEXT, "
                + "wordFromSound TEXT, "
                + "wordToName TEXT, "
                + "wordToPronounciation TEXT, "
                + "wordToSound TEXT, "
                + "favouriteFlag TEXT, "
                + "favId TEXT, "
                + "numberId INTEGER, "
                + "languageFrom INTEGER, "
                + "languageTo INTEGER )";

        String CREATE_AD_TOP_TABLE = "CREATE TABLE adTop ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "languageFrom INTEGER, "
                + "languageTo INTEGER, "
                + "prDetailId INTEGER, "
                + "promoId TEXT, "
                + "promoImage TEXT, "
                + "prOrderBy INTEGER, "
                + "active INTEGER, "
                + "url TEXT, "
                + "screen TEXT )";

        String CREATE_AD_BOTTOM_TABLE = "CREATE TABLE adBottom ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "languageFrom INTEGER, "
                + "languageTo INTEGER, "
                + "prDetailId INTEGER, "
                + "promoId TEXT, "
                + "promoImage TEXT, "
                + "prOrderBy INTEGER, "
                + "active INTEGER, "
                + "url TEXT, "
                + "screen TEXT )";

        db.execSQL(CREATE_LANGUAGE_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_WORDS_TABLE);
        db.execSQL(CREATE_AD_TOP_TABLE);
        db.execSQL(CREATE_AD_BOTTOM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop radioFm table if already exists
        db.execSQL("DROP TABLE IF EXISTS Language");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS words");
        db.execSQL("DROP TABLE IF EXISTS adTop");
        db.execSQL("DROP TABLE IF EXISTS adBottom");

        this.onCreate(db);
    }

    public void addLanguage(Language language) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<LanguageResultValue> arrayList = language.getResultValue();
        for(int i=0;i<arrayList.size();i++ ){
            int languageId = arrayList.get(i).getLangId();
            String languageName = arrayList.get(i).getLangName();
            boolean isActive = arrayList.get(i).isActive();
            int active = (isActive?1:0);

            ContentValues values = new ContentValues();
            values.put("langId", languageId);
            values.put("langName", languageName);
            values.put("active", active);

            db.insert(table_name_language, null, values);
        }
        db.close();
    }

    public void addCategories(ArrayList<Category> categories, int languageFrom, int languageTo) {
        SQLiteDatabase db = this.getWritableDatabase();

        int categoriesSize = categories.size();
        for(int j=0;j<categoriesSize;j++){
            int catId = categories.get(j).getCatId();
            String catName = categories.get(j).getCatName();
            String catImage = categories.get(j).getCatImage();
//            int languageFrom = categories.get(j).getLanguageFrom();
//            int languageTo = categories.get(j).getLanguageTo();


            ContentValues values = new ContentValues();
            values.put("catId", catId);
            values.put("catName", catName);
            values.put("catImage", catImage);
            values.put("languageFrom", languageFrom);
            values.put("languageTo", languageTo);


            db.insert(table_name_category, null, values);
        }
        db.close();
    }

    public void addTopAds(ArrayList<AdsTop> adsTops, int languageFrom, int languageTo, String screen) {
        SQLiteDatabase db = this.getWritableDatabase();

        int adsTopsSize = adsTops.size();
        for(int j=0;j<adsTopsSize;j++){
            int prDetailId = adsTops.get(j).getPrDetailId();
            int promoId = adsTops.get(j).getPromoId();
            String promoImage = adsTops.get(j).getPromoImage();
            int prOrderBy = adsTops.get(j).getPrOrderBy();
            boolean active = adsTops.get(j).isActive();
            String url = adsTops.get(j).getUrl();

            int isActive = (active?1:0);

            ContentValues values = new ContentValues();
            values.put("languageFrom", languageFrom);
            values.put("languageTo", languageTo);
            values.put("prDetailId", prDetailId);
            values.put("promoId", promoId);
            values.put("promoImage", promoImage);
            values.put("prOrderBy", prOrderBy);
            values.put("active", isActive);
            values.put("url", url);
            values.put("screen", screen);

            db.insert(table_name_adTop, null, values);
        }
        db.close();
    }
    public void addBottomAds(ArrayList<AdsBottom> adsBottoms, int languageFrom, int languageTo, String screen) {
        SQLiteDatabase db = this.getWritableDatabase();

        int adsBottomsSize = adsBottoms.size();
        for(int j=0;j<adsBottomsSize;j++){
            int prDetailId = adsBottoms.get(j).getPrDetailId();
            int promoId = adsBottoms.get(j).getPromoId();
            String promoImage = adsBottoms.get(j).getPromoImage();
            int prOrderBy = adsBottoms.get(j).getPrOrderBy();
            boolean active = adsBottoms.get(j).isActive();
            String url = adsBottoms.get(j).getUrl();

            int isActive = (active?1:0);

            ContentValues values = new ContentValues();
            values.put("languageFrom", languageFrom);
            values.put("languageTo", languageTo);
            values.put("prDetailId", prDetailId);
            values.put("promoId", promoId);
            values.put("promoImage", promoImage);
            values.put("prOrderBy", prOrderBy);
            values.put("active", isActive);
            values.put("url", url);
            values.put("screen", screen);


            db.insert(table_name_adBottom, null, values);
        }

        db.close();
    }
    public ArrayList<LanguageList> getLanguageList() {
        ArrayList<LanguageList> languages = new ArrayList<>();

        // select book query
        String query = "SELECT * FROM " + table_name_language + " WHERE active = '" + 1 + "' ORDER BY  langId";

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int langId = cursor.getInt(1);
                String langName = cursor.getString(2);

                LanguageList languageList = new LanguageList();
                languageList.setLanguageId(langId);
                languageList.setLanguageName(langName);

                languages.add(languageList);
            } while (cursor.moveToNext());
        }
        db.close();
        return languages;
    }
    public String[] getLanguageNames() {

        // select book query
        String query = "SELECT * FROM " + table_name_language + " WHERE active = '" + 1 + "' ORDER BY  langId";

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String[] languageNames = new String[cursor.getCount()];
        int i=0;
        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int langId = cursor.getInt(1);
                String langName = cursor.getString(2);
                languageNames[i] = langName;
                i++;

            } while (cursor.moveToNext());
        }
        db.close();
        return languageNames;
    }
    public String[] getTranslateLanguageNames(int selectedLanguageId) {
        // select book query

        String query = "SELECT * FROM " + table_name_language + " WHERE active = '" + 1 + "'"+"AND langId !='"+selectedLanguageId+"'ORDER BY  langId";

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        String[] languageNames = new String[cursor.getCount()];
        int i=0;
        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int langId = cursor.getInt(1);
                String langName = cursor.getString(2);
                languageNames[i] = langName;
                i++;

            } while (cursor.moveToNext());
        }
        db.close();
        return languageNames;
    }
    public int getLanguageId(String selectedLanguage) {
        String query = "SELECT langId FROM " + table_name_language + " WHERE langName = '" + selectedLanguage+"'" ;
        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int langId = 0;
        if (cursor.moveToFirst()) {
            langId = cursor.getInt(0);
        }
        return langId;
    }

    public ArrayList<pojoCategory> getCategoryList(int selectedNativeLanguageId, int selectedTranslateLanguageId) {
        ArrayList<pojoCategory> categories = new ArrayList<>();

        String query = "SELECT * FROM " + table_name_category + " WHERE languageFrom = '" + selectedNativeLanguageId+"' AND languageTo= '"+ selectedTranslateLanguageId +"'" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int catId = cursor.getInt(1);
                String catName = cursor.getString(2);
                String catImage = cursor.getString(3);

                pojoCategory category = new pojoCategory();
                category.setCatId(catId);
                category.setCatName(catName);
                category.setCatImage(catImage);
                categories.add(category);

            } while (cursor.moveToNext());
        }
        return categories;
    }
    public ArrayList<pojoAds> getTopAds(int selectedNativeLanguageId, int selectedTranslateLanguageId, String screen) {
        ArrayList<pojoAds> pojoAds = new ArrayList<>();

        String query = "SELECT promoImage,url FROM " + table_name_adTop + " WHERE languageFrom = '" + selectedNativeLanguageId+"' AND languageTo= '"+ selectedTranslateLanguageId +"' AND screen= '"+ screen +"' ORDER BY prOrderBy" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String promoImage = cursor.getString(0);
                String url = cursor.getString(1);

                com.idl.globolo.pojoViewAdapter.pojoAds pojoAds1 = new pojoAds();
                pojoAds1.setPromoImage(promoImage);
                pojoAds1.setUrl(url);

                pojoAds.add(pojoAds1);

            } while (cursor.moveToNext());
        }

        return pojoAds;
    }
    public ArrayList<pojoAds> getBottomAds(int selectedNativeLanguageId, int selectedTranslateLanguageId, String screen) {
        ArrayList<pojoAds> pojoAds = new ArrayList<>();

        String query = "SELECT promoImage,url FROM " + table_name_adBottom + " WHERE languageFrom = '" + selectedNativeLanguageId+"' AND languageTo= '"+ selectedTranslateLanguageId+"' AND screen= '"+ screen +"' ORDER BY prOrderBy" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String promoImage = cursor.getString(0);
                String url = cursor.getString(1);

                com.idl.globolo.pojoViewAdapter.pojoAds pojoAds1 = new pojoAds();
                pojoAds1.setPromoImage(promoImage);
                pojoAds1.setUrl(url);

                pojoAds.add(pojoAds1);

            } while (cursor.moveToNext());
        }

        return pojoAds;
    }


    public void addWords(ArrayList<WordsList> wordsLists, int languageFrom, int languageTo) {
        SQLiteDatabase db = this.getWritableDatabase();

        int wordlistSize = wordsLists.size();
        for(int j=0;j<wordlistSize;j++){
            int catId = wordsLists.get(j).getCatId();
            String catName = wordsLists.get(j).getCatName();
            String catImage = wordsLists.get(j).getCatImage();
            String wordImage = wordsLists.get(j).getWordImage();
            String wordFromName = wordsLists.get(j).getWordFromName();
            String wordFromSound = wordsLists.get(j).getWordFromSound();
            String wordToName = wordsLists.get(j).getWordToName();
            String wordToPronounciation = wordsLists.get(j).getWordToPronounciation();
            String wordToSound = wordsLists.get(j).getWordToSound();
            String favouriteFlag = wordsLists.get(j).getFavouriteFlag();
            String favId = wordsLists.get(j).getFavId();
            int numberId = wordsLists.get(j).getNumberId();

            ContentValues values = new ContentValues();
            values.put("catId", catId);
            values.put("catName", catName);
            values.put("catImage", catImage);
            values.put("wordImage", wordImage);
            values.put("wordFromName", wordFromName);
            values.put("wordFromSound", wordFromSound);
            values.put("wordToName", wordToName);
            values.put("wordToPronounciation", wordToPronounciation);
            values.put("wordToSound", wordToSound);
            values.put("favouriteFlag", favouriteFlag);
            values.put("favId", favId);
            values.put("numberId", numberId);
            values.put("languageFrom", languageFrom);
            values.put("languageTo", languageTo);


            db.insert(table_name_words, null, values);
        }
        db.close();
    }

    public ArrayList<WordsList> getWordsList(int languageFrom, int languageTo, int categoryId){
        ArrayList<WordsList>  wordsLists = new ArrayList<>();

        // select book query
        String query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId= "+ categoryId ;

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int catId = cursor.getInt(1);
                String catName = cursor.getString(2);
                String catImage = cursor.getString(3);
                String wordImage = cursor.getString(4);
                String wordFromName = cursor.getString(5);
                String wordFromSound = cursor.getString(6);
                String wordToName = cursor.getString(7);
                String wordToPronounciation = cursor.getString(8);
                String wordToSound = cursor.getString(9);
                String favouriteFlag = cursor.getString(10);
                String favId = cursor.getString(11);
                int numberId = cursor.getInt(12);

                WordsList wordsList = new WordsList();
                wordsList.setCatId(catId);
                wordsList.setCatName(catName);
                wordsList.setCatImage(catImage);
                wordsList.setWordImage(wordImage);
                wordsList.setWordFromName(wordFromName);
                wordsList.setWordFromSound(wordFromSound);
                wordsList.setWordToName(wordToName);
                wordsList.setWordToPronounciation(wordToPronounciation);
                wordsList.setWordToSound(wordToSound);
                wordsList.setFavouriteFlag(favouriteFlag);
                wordsList.setFavId(favId);
                wordsList.setNumberId(numberId);
                wordsList.setLanguageFrom(languageFrom);
                wordsList.setLanguageTo(languageTo);

                wordsLists.add(wordsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordsLists;
    }
    public ArrayList<WordsList> getWordsList(int languageFrom, int languageTo, int categoryId, String noOfItemsToShow){
        ArrayList<WordsList>  wordsLists = new ArrayList<>();

        // select book query
        String query = null;
        if(noOfItemsToShow.equalsIgnoreCase("oneTo50")){
            query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId= "+ categoryId+" AND numberId <= "+51 ;
        }else if(noOfItemsToShow.equalsIgnoreCase("fiftyOneTo99")){
            query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId= "+ categoryId+" AND numberId BETWEEN "+52+" AND "+100 ;
        }else{
            query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId= "+ categoryId+" AND numberId >= "+101 ;
        }


        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int catId = cursor.getInt(1);
                String catName = cursor.getString(2);
                String catImage = cursor.getString(3);
                String wordImage = cursor.getString(4);
                String wordFromName = cursor.getString(5);
                String wordFromSound = cursor.getString(6);
                String wordToName = cursor.getString(7);
                String wordToPronounciation = cursor.getString(8);
                String wordToSound = cursor.getString(9);
                String favouriteFlag = cursor.getString(10);
                String favId = cursor.getString(11);
                int numberId = cursor.getInt(12);

                WordsList wordsList = new WordsList();
                wordsList.setCatId(catId);
                wordsList.setCatName(catName);
                wordsList.setCatImage(catImage);
                wordsList.setWordImage(wordImage);
                wordsList.setWordFromName(wordFromName);
                wordsList.setWordFromSound(wordFromSound);
                wordsList.setWordToName(wordToName);
                wordsList.setWordToPronounciation(wordToPronounciation);
                wordsList.setWordToSound(wordToSound);
                wordsList.setFavouriteFlag(favouriteFlag);
                wordsList.setFavId(favId);
                wordsList.setNumberId(numberId);
                wordsList.setLanguageFrom(languageFrom);
                wordsList.setLanguageTo(languageTo);

                wordsLists.add(wordsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordsLists;
    }
    public ArrayList<WordsList> getFavouriteWordsList(int languageFrom, int languageTo, boolean favFlag){
        ArrayList<WordsList>  wordsLists = new ArrayList<>();

        // select book query
        String query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND favouriteFlag= '"+ favFlag+"'" ;

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int catId = cursor.getInt(1);
                String catName = cursor.getString(2);
                String catImage = cursor.getString(3);
                String wordImage = cursor.getString(4);
                String wordFromName = cursor.getString(5);
                String wordFromSound = cursor.getString(6);
                String wordToName = cursor.getString(7);
                String wordToPronounciation = cursor.getString(8);
                String wordToSound = cursor.getString(9);
                String favouriteFlag = cursor.getString(10);
                String favId = cursor.getString(11);
                int numberId = cursor.getInt(12);

                WordsList wordsList = new WordsList();
                wordsList.setCatId(catId);
                wordsList.setCatName(catName);
                wordsList.setCatImage(catImage);
                wordsList.setWordImage(wordImage);
                wordsList.setWordFromName(wordFromName);
                wordsList.setWordFromSound(wordFromSound);
                wordsList.setWordToName(wordToName);
                wordsList.setWordToPronounciation(wordToPronounciation);
                wordsList.setWordToSound(wordToSound);
                wordsList.setFavouriteFlag(favouriteFlag);
                wordsList.setFavId(favId);
                wordsList.setNumberId(numberId);
                wordsList.setLanguageFrom(languageFrom);
                wordsList.setLanguageTo(languageTo);

                wordsLists.add(wordsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordsLists;
    }
    public ArrayList<WordsList> getFavouriteWordsList(int languageFrom, int languageTo, boolean favFlag, int catIdd){
        ArrayList<WordsList>  wordsLists = new ArrayList<>();

        // select book query
        String query = "SELECT * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId= "+ catIdd+" AND favouriteFlag= '"+ favFlag+"'" ;

        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        if (cursor.moveToFirst()) {
            do {
                int catId = cursor.getInt(1);
                String catName = cursor.getString(2);
                String catImage = cursor.getString(3);
                String wordImage = cursor.getString(4);
                String wordFromName = cursor.getString(5);
                String wordFromSound = cursor.getString(6);
                String wordToName = cursor.getString(7);
                String wordToPronounciation = cursor.getString(8);
                String wordToSound = cursor.getString(9);
                String favouriteFlag = cursor.getString(10);
                String favId = cursor.getString(11);
                int numberId = cursor.getInt(12);

                WordsList wordsList = new WordsList();
                wordsList.setCatId(catId);
                wordsList.setCatName(catName);
                wordsList.setCatImage(catImage);
                wordsList.setWordImage(wordImage);
                wordsList.setWordFromName(wordFromName);
                wordsList.setWordFromSound(wordFromSound);
                wordsList.setWordToName(wordToName);
                wordsList.setWordToPronounciation(wordToPronounciation);
                wordsList.setWordToSound(wordToSound);
                wordsList.setFavouriteFlag(favouriteFlag);
                wordsList.setFavId(favId);
                wordsList.setNumberId(numberId);
                wordsList.setLanguageFrom(languageFrom);
                wordsList.setLanguageTo(languageTo);

                wordsLists.add(wordsList);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordsLists;
    }
    public boolean isAlreadyWordsLoaded(int languageFrom, int languageTo,int catId) {
        // select book query
        String query = "SELECT  * FROM " + table_name_words + " WHERE languageFrom = " + languageFrom+" AND languageTo= "+ languageTo+" AND catId = "+catId;
        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isAlreadyCategoryLoaded() {
        // select book query
        String query = "SELECT  * FROM " + table_name_category;
        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public void updateWord(String flag, int favId, int numberId, int languageFrom, int languageTo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("favouriteFlag", flag);
        cv.put("favId", favId);

        db.update(table_name_words, cv, "numberId=" + numberId+" and languageFrom="+languageFrom+" and languageTo="+languageTo, null);
    }

    public void deleteAllTables() {
        // get reference of the WorldFmRadio database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete book
        db.delete(table_name_language, null, null);
        db.delete(table_name_category, null, null);
        db.delete(table_name_words, null, null);
        db.delete(table_name_adTop, null, null);
        db.delete(table_name_adBottom, null, null);
        db.close();
    }


//    public void deleteItem(String itemId) {
//        // get reference of the WorldFmRadio database
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // delete book
//        db.delete(table_name, "itemId = ?", new String[]{itemId});
//        db.close();
//    }


}

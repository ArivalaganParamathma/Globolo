package com.idl.globolo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.idl.globolo.commonPojos.LanguageList;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoAllInformation.AdsBottom;
import com.idl.globolo.pojoAllInformation.AdsTop;
import com.idl.globolo.pojoAllInformation.AllInformation;
import com.idl.globolo.pojoAllInformation.Category;
import com.idl.globolo.pojoAllInformation.CategoryList;
import com.idl.globolo.retrofitSupport.ApiClient;
import com.idl.globolo.retrofitSupport.ApiInterface;
import com.idl.globolo.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class ApplicationClass extends Application {
    public int nodeIncrement = 0;
    private ApiInterface apiService;
    private Call<AllInformation> getAllInformation;
    private JCGSQLiteHelper db;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        db = new JCGSQLiteHelper(this);
        sharedPref = getSharedPreferences("", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public void getAllInformation(final int firstNodeIndex, final int lastNodeIndex, final ArrayList<LanguageList> languageList) {
        int firstNode = languageList.get(firstNodeIndex).getLanguageId();
        int lastNode = languageList.get(lastNodeIndex).getLanguageId();
        Log.e("Node ", firstNode + " "+ lastNode);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        getAllInformation = apiService.getAllInformation(Constants.userId,firstNode, lastNode);
        getAllInformation.enqueue(new Callback<AllInformation>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<AllInformation> call, Response<AllInformation> response) {
                final AllInformation allInformation = response.body();
                if(allInformation.getStatus() .equalsIgnoreCase("Success")){
                    new AsyncTask<String, String, String>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected String doInBackground(String... strings) {
                            ArrayList<CategoryList> categoryLists = allInformation.getResultValue().getCategoriesList();
                            int categoryListSize = categoryLists.size();
                            for (int i=0;i< categoryListSize;i++){
                                ArrayList<Category> categories = categoryLists.get(i).getCategory();
                                ArrayList<AdsTop> adsTops = categoryLists.get(i).getAdsTop();
                                ArrayList<AdsBottom> adsBottoms = categoryLists.get(i).getAdsBottom();

                                int languageFrom = categoryLists.get(i).getLanguageFrom();
                                int languageTo = categoryLists.get(i).getLanguageTo();
                                db.addCategories(categories, languageFrom, languageTo);
                                db.addTopAds(adsTops, languageFrom, languageTo,"main");
                                db.addBottomAds(adsBottoms, languageFrom, languageTo,"main");
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);

                             /*Calle recursive function*/
                            nodeIncrement = nodeIncrement +1;
                            int firstNodeIndex = nodeIncrement;
                            nodeIncrement = nodeIncrement +1;
                            int lastNodeIndex = nodeIncrement;
                            if(firstNodeIndex < languageList.size()){
                                if(lastNodeIndex == languageList.size()){
                                    lastNodeIndex = firstNodeIndex;
                                }
                                getAllInformation(firstNodeIndex,lastNodeIndex,languageList);
                            }else {
                                editor.putBoolean("isMainPageLoaded", true);
                                editor.apply();
                                Log.e("Node ","Done");
                            }
                        }
                    }.execute();
                }
            }
            @Override
            public void onFailure(Call<AllInformation>call, Throwable t) {
                // Log error here since request failed
                editor.putBoolean("isMainPageLoaded", false);
                editor.apply();

                Log.e("TAG", t.toString());
            }
        });
    }

}

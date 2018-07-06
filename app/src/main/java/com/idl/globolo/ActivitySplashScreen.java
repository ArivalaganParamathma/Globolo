package com.idl.globolo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.idl.globolo.POJO.MainAd;
import com.idl.globolo.POJO.UserDetailResult;
import com.idl.globolo.POJO.UserIdInput;
import com.idl.globolo.commonPojos.LanguageList;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoLanguage.Language;
import com.idl.globolo.retrofitSupport.ApiClient;
import com.idl.globolo.retrofitSupport.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.idl.globolo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.idl.globolo.utils.Constants.isFirstTime;
import static com.idl.globolo.utils.Constants.mainAdImageUrl;
import static com.idl.globolo.utils.Constants.userId;

public class ActivitySplashScreen extends AppCompatActivity {
    //    https://www.journaldev.com/13639/retrofit-android-example-tutorial
    LinearLayout ll_toHide;
    ImageView imageView;
    private String deviceId,
            deviceName;
    private JCGSQLiteHelper db;
    private ApplicationClass applicationClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        db = new JCGSQLiteHelper(this);

        applicationClass = (ApplicationClass) getApplication();
        imageView = (ImageView) findViewById(R.id.img_mainAd);
        ll_toHide = (LinearLayout) findViewById(R.id.rl_toHide);


        try {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceName = android.os.Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean alreadyDataLoaded = applicationClass.sharedPref.getBoolean("isMainPageLoaded", false);
        if (alreadyDataLoaded) {
            userId = applicationClass.sharedPref.getInt("userId", 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isFirstTime = false;
                    getMainAd();
                }
            }, 2000);

        } else {
            db.deleteAllTables();
            /*get UserDetail*/
            UserIdInput userIdInput = new UserIdInput();
            userIdInput.setActive(true);
            userIdInput.setCreateDate("");
            userIdInput.setDevice(deviceName);
            userIdInput.setDeviceId(deviceId);
            userIdInput.setModifiedDate("");
            userIdInput.setPlatform("Android");
            userIdInput.setUserId(0);
            userIdInput.setUserToken("");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UserDetailResult> callUser = apiService.getUserId(userIdInput);
            callUser.enqueue(new Callback<UserDetailResult>() {
                @Override
                public void onResponse(Call<UserDetailResult> call, Response<UserDetailResult> response) {
                    if (response.isSuccessful()) {
                        userId = response.body().getResultValue().getUserId();

                        /*Store userId*/
                        applicationClass.editor.putInt("userId", userId);
                        applicationClass.editor.apply();
                        isFirstTime = true;

                        getLanguages();

                    } else {
                        Log.e("USER_ID ", "Failed");
                        Toast.makeText(getApplicationContext(), "Can not connect with server\nPlease try again", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserDetailResult> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("TAG", t.toString());
                }
            });
        }

    }

    private void getLanguages() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Language> callLanguages = apiService.getLanguages();
        callLanguages.enqueue(new Callback<Language>() {
            @Override
            public void onResponse(Call<Language> call, Response<Language> response) {
                Language language = response.body();
                if (language.getStatus().equalsIgnoreCase("Success")) {
                    db.addLanguage(language);
                }
                 /*Start recursive function*/
                applicationClass.editor.putBoolean("isMainPageLoaded", false);
                applicationClass.editor.apply();
                ArrayList<LanguageList> languageList = db.getLanguageList();
                int firstNodeIndex = applicationClass.nodeIncrement;
                applicationClass.nodeIncrement = applicationClass.nodeIncrement + 1;
                int lastNodeIndex = applicationClass.nodeIncrement;
                applicationClass.getAllInformation(firstNodeIndex, lastNodeIndex, languageList);

                /*Load Main Ad*/
                getMainAd();
            }

            @Override
            public void onFailure(Call<Language> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }

    private void getMainAd() {
    /*getMainAd*/
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainAd> callMainAd = apiService.getMainAd();
        callMainAd.enqueue(new Callback<MainAd>() {
            @Override
            public void onResponse(Call<MainAd> call, Response<MainAd> response) {
                if (response.isSuccessful()) {
                    MainAd mainAd = response.body();
                    String status = mainAd.getStatus();

                    if (status.equalsIgnoreCase("Success")) {
                        String prmImage = mainAd.getResultValue().getPrmImage();
                        final String url = mainAd.getResultValue().getUrl();
                        String imgUrl = mainAdImageUrl + prmImage;

                        Picasso.with(ActivitySplashScreen.this).load(imgUrl)
                                .into(imageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        ll_toHide.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);

                                        Handler mHandler = new Handler();
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent in = new Intent(ActivitySplashScreen.this, ActivityLandingPage.class);
                                                startActivity(in);
                                                finish();
                                            }

                                        }, 3000L);
                                    }

                                    @Override
                                    public void onError() {
                                        Intent in = new Intent(ActivitySplashScreen.this, ActivityLandingPage.class);
                                        startActivity(in);
                                        finish();
                                    }
                                });

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (url != null && !url.equalsIgnoreCase("")) {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                }
                            }
                        });

                    }
                } else {
                    Intent in = new Intent(ActivitySplashScreen.this, ActivityLandingPage.class);
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MainAd> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }
}

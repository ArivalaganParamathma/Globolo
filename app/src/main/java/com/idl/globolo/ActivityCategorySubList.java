package com.idl.globolo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoViewAdapter.pojoAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.idl.globolo.R;

import static com.idl.globolo.utils.Constants.mainAdImageUrl;

public class ActivityCategorySubList extends AppCompatActivity {

    private String catId,
            catName;
    private int languageFrom,
            languageTo;
    private  int bottom_ad_incrementer = 0,
            top_ad_incrementer = 0;
    private JCGSQLiteHelper db;
    private final Handler handler = new Handler();
    private ImageView img_topAds;
    private ImageView img_bottomAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Wishlist");

        Intent intent = getIntent();
        catId = intent.getStringExtra("catId");
        languageFrom = intent.getIntExtra("languageFrom",0);
        languageTo = intent.getIntExtra("languageTo",0);
        catName = intent.getStringExtra("catName");
        setTitle(catName);

        db = new JCGSQLiteHelper(this);

        RelativeLayout oneTo50 = findViewById(R.id.rl_oneTo50);
        RelativeLayout fiftyOneTo99 = findViewById(R.id.rl_fiftyOneTo99);
        RelativeLayout hundredAndAbove = findViewById(R.id.rl_hundredAndAbove);

        oneTo50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSubCategoryDetails("oneTo50");
            }
        });

        fiftyOneTo99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSubCategoryDetails("fiftyOneTo99");
            }
        });

        hundredAndAbove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSubCategoryDetails("hundredAndAbove");
            }
        });

        img_topAds = findViewById(R.id.img_topAd);
        img_bottomAds = findViewById(R.id.img_bottomAd);
        startTimerForTopAds();
        startBottomAds();
    }
    private void startSubCategoryDetails(String numberOfItemsToShow) {
        Intent intent = new Intent(this, ActivityCategoryDetails.class);
        intent.putExtra("catId",catId);
        intent.putExtra("catName",catName);
        intent.putExtra("languageFrom",languageFrom);
        intent.putExtra("languageTo",languageTo);
        intent.putExtra("isFavourite",false);
        intent.putExtra("noOfItemsToShow",numberOfItemsToShow);
        startActivity(intent);
    }

    private void startBottomAds() {
        final ArrayList<pojoAds> ads = db.getBottomAds(languageFrom, languageTo,"main");
        bottom_ad_incrementer = 0;
        showBottomAd(ads);
    }

    private void showBottomAd(final ArrayList<pojoAds> ads) {

        String promoIMage = ads.get(bottom_ad_incrementer).getPromoImage();
        final String reDirectUrl = ads.get(bottom_ad_incrementer).getUrl();
        String imgUrl = mainAdImageUrl + promoIMage;

        Picasso.with(ActivityCategorySubList.this).load(imgUrl)
                .into(img_bottomAds, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(bottom_ad_incrementer < ads.size()-1){
                                    bottom_ad_incrementer = bottom_ad_incrementer+1;
                                }else{
                                    bottom_ad_incrementer = 0;
                                }
                                showBottomAd(ads);
                            }
                        },2000);
                    }

                    @Override
                    public void onError() {

                    }
                });

        img_bottomAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = reDirectUrl;
                if(url !=null && !url.equalsIgnoreCase("")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });
    }

    private void startTimerForTopAds() {
        ArrayList<pojoAds> ads = db.getTopAds(languageFrom, languageTo,"main");
        top_ad_incrementer = 0;
        showTopAd(ads);
    }

    private void showTopAd(final ArrayList<pojoAds> ads) {
        String promoIMage = ads.get(top_ad_incrementer).getPromoImage();
        final String reDirectUrl = ads.get(top_ad_incrementer).getUrl();
        String imgUrl = mainAdImageUrl + promoIMage;

        Picasso.with(ActivityCategorySubList.this).load(imgUrl)
                .into(img_topAds, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(top_ad_incrementer < ads.size()-1){
                                    top_ad_incrementer = top_ad_incrementer+1;
                                }else{
                                    top_ad_incrementer = 0;
                                }
                                showTopAd(ads);
                            }
                        },2000);
                    }

                    @Override
                    public void onError() {

                    }
                });
        img_topAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = reDirectUrl;
                if(url !=null && !url.equalsIgnoreCase("")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.idl.globolo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.adapters.FavouriteListAdapter;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoViewAdapter.pojoAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.idl.globolo.R;

import static com.idl.globolo.utils.Constants.mainAdImageUrl;

public class ActivityFavourite extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FavouriteListAdapter favouriteListAdapter;
    private int languageFrom,
            languageTo,
            catId;
    private JCGSQLiteHelper db;

    private TextView tv_info;
    private ArrayList<WordsList> categories;
    private boolean isCategoryFilterEnabled = false;

    private  int bottom_ad_incrementer = 0,
            top_ad_incrementer = 0;
    private final Handler handler = new Handler();
    private ImageView img_topAds;
    private ImageView img_bottomAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Wishlist");

        Intent intent = getIntent();
        languageFrom = intent.getIntExtra("languageFrom",0);
        languageTo = intent.getIntExtra("languageTo",0);
        catId = intent.getIntExtra("catId",0);

        mRecyclerView = findViewById(R.id.rv_favourites);
        tv_info = findViewById(R.id.info);

        db = new JCGSQLiteHelper(this);
//        if(catId > 0) {
//            isCategoryFilterEnabled = true;
//            categories = db.getFavouriteWordsList(languageFrom, languageTo, true, catId);
//        }else {
//            isCategoryFilterEnabled = false;
//            categories = db.getFavouriteWordsList(languageFrom, languageTo, true);
//        }
        categories = db.getFavouriteWordsList(languageFrom, languageTo, true);

        setInfo();

        favouriteListAdapter = new FavouriteListAdapter(ActivityFavourite.this, categories, languageFrom, languageTo,isCategoryFilterEnabled);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityFavourite.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(favouriteListAdapter);


        img_topAds = findViewById(R.id.img_topAd);
        img_bottomAds = findViewById(R.id.img_bottomAd);
        startTimerForTopAds();
        startBottomAds();

    }
    public void setInfo(){
        if(categories.size() > 0){
            tv_info.setVisibility(View.GONE);
        }else {
            tv_info.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(favouriteListAdapter != null){
            favouriteListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void startBottomAds() {
        final ArrayList<pojoAds> ads = db.getBottomAds(languageFrom, languageTo,"main");
        bottom_ad_incrementer = 0;
        showBottomAd(ads);
    }

    private void showBottomAd(final ArrayList<pojoAds> ads) {
        if(ads.size() > 0){
            String promoIMage = ads.get(bottom_ad_incrementer).getPromoImage();
            final String reDirectUrl = ads.get(bottom_ad_incrementer).getUrl();
            String imgUrl = mainAdImageUrl + promoIMage;

            Picasso.with(ActivityFavourite.this).load(imgUrl)
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

    }

    private void startTimerForTopAds() {
        ArrayList<pojoAds> ads = db.getTopAds(languageFrom, languageTo,"main");
        top_ad_incrementer = 0;
        showTopAd(ads);
    }

    private void showTopAd(final ArrayList<pojoAds> ads) {

        if(ads.size() > 0){
            String promoIMage = ads.get(top_ad_incrementer).getPromoImage();
            final String reDirectUrl = ads.get(top_ad_incrementer).getUrl();
            String imgUrl = mainAdImageUrl + promoIMage;

            Picasso.with(ActivityFavourite.this).load(imgUrl)
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

    }
}

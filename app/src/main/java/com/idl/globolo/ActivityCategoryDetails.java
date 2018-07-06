package com.idl.globolo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.idl.globolo.POJO.CategoryDetailInput;
import com.idl.globolo.POJO.CategoryDetailsOutput;
import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.adapters.AdapterCategoryDetails;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoAllInformation.AdsBottom;
import com.idl.globolo.pojoAllInformation.AdsTop;
import com.idl.globolo.pojoViewAdapter.pojoAds;
import com.idl.globolo.retrofitSupport.ApiClient;
import com.idl.globolo.retrofitSupport.ApiInterface;
import com.idl.globolo.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.idl.globolo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.idl.globolo.utils.Constants.mainAdImageUrl;

public class ActivityCategoryDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private ImageView img_topAds;
    private ImageView img_bottomAds;

    private AdapterCategoryDetails homeScreenAdapter;
    private JCGSQLiteHelper db;

    private final Handler handler = new Handler();
    private int languageFrom, languageTo;
    private  int bottom_ad_incrementer = 0,
            top_ad_incrementer = 0;

    public boolean isFavourite = false;
    private DefaultTrackSelector trackSelector;
    private DefaultDataSourceFactory dataSourceFactory;
    private String catName,
            noOfItemsToShow;
    private int catId;

    private ProgressDialog progressDoalog;
    private boolean isCatFilter;
    private int position;
    ArrayList<WordsList> favouriteWordList = null;

    public DefaultDataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
    private SimpleExoPlayer player;
    public SimpleExoPlayer getPlayer() {
        return player;
    }

    private ArrayList<WordsList> wordsList = new ArrayList<>();
    public ArrayList<WordsList> getWordsList() {
        return wordsList;
    }

    public void setWordsList(ArrayList<WordsList> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        db = new JCGSQLiteHelper(this);
        Intent intent = getIntent();
        catId = Integer.parseInt(intent.getStringExtra("catId"));
        languageFrom = intent.getIntExtra("languageFrom",0);
        languageTo = intent.getIntExtra("languageTo",0);
        catName = intent.getStringExtra("catName");
        isFavourite = intent.getBooleanExtra("isFavourite",false);
        if(catId == 3){
            noOfItemsToShow = intent.getStringExtra("noOfItemsToShow");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init(toolbar);
        initExoPlayer();
        if(isFavourite) {
            isCatFilter = intent.getBooleanExtra("isCatFilter", false);
            position = intent.getIntExtra("position", 0);
        }

        ArrayList<pojoAds> ads = db.getTopAds(languageFrom, languageTo,"wordlist");
        if(ads.size() > 0){
            startTimerForTopAds();
            startBottomAds();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFavourite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isFavourite){
//            if(isCatFilter)
//                categories = db.getFavouriteWordsList(languageFrom, languageTo,true,catId);
//            else
//                categories = db.getFavouriteWordsList(languageFrom, languageTo,true);

            favouriteWordList = db.getFavouriteWordsList(languageFrom, languageTo,true);
            setWordsList(favouriteWordList);

            homeScreenAdapter = new AdapterCategoryDetails(this, getSupportFragmentManager(), getWordsList(),catName);
            viewPager.setAdapter(homeScreenAdapter);

            homeScreenAdapter.setWordsList(favouriteWordList);
            viewPager.setCurrentItem(position);
        }else{
            boolean alreadyDataLoaded = db.isAlreadyWordsLoaded(languageFrom, languageTo,catId);
            if(alreadyDataLoaded){
                ArrayList<WordsList> wordsList = null;
                if(catId != 3)
                    wordsList = db.getWordsList(languageFrom, languageTo, catId);
                else
                    wordsList = db.getWordsList(languageFrom, languageTo, catId,noOfItemsToShow);

                setWordsList(wordsList);
                homeScreenAdapter = new AdapterCategoryDetails(this, getSupportFragmentManager(), getWordsList(),catName);
                viewPager.setAdapter(homeScreenAdapter);

                homeScreenAdapter.setWordsList(wordsList);
            }else{
                processCategoryDetails(catId);
            }
        }

    }

    public void removePosition(int position){
        favouriteWordList.remove(position);
        setWordsList(favouriteWordList);
        homeScreenAdapter.notifyDataSetChanged();
        position = position-1;
        if(position <= getWordsList().size()-1){
            if(position != -1){
                WordsList wordsList = favouriteWordList.get(position);
                homeScreenAdapter = new AdapterCategoryDetails(this, getSupportFragmentManager(), getWordsList(),wordsList.getCatName());
                viewPager.setAdapter(homeScreenAdapter);
                homeScreenAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(position);
            }else{
                homeScreenAdapter = new AdapterCategoryDetails(this, getSupportFragmentManager(), getWordsList(),catName);
                viewPager.setAdapter(homeScreenAdapter);
                homeScreenAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
            }

        }else{
            onBackPressed();
        }
    }
    private void processCategoryDetails(final int catId) {
        showProgressDialogue();

        CategoryDetailInput categoryDetailInput = new CategoryDetailInput();
        categoryDetailInput.setCatId(catId);
        categoryDetailInput.setLanguageFrom(languageFrom);
        categoryDetailInput.setLanguageTo(languageTo);
        categoryDetailInput.setUserId(Constants.userId);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CategoryDetailsOutput> callUser = apiService.getCategoryDetails(categoryDetailInput);
        callUser.enqueue(new Callback<CategoryDetailsOutput>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<CategoryDetailsOutput>call, Response<CategoryDetailsOutput> response) {
                final CategoryDetailsOutput categoryDetailsOutput = response.body();
                if(response.isSuccessful()){
                    if(categoryDetailsOutput.getStatus() .equalsIgnoreCase("Success")){
                        new AsyncTask<String, String, ArrayList<WordsList>>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected ArrayList<WordsList> doInBackground(String... strings) {
                                ArrayList<WordsList> wordsList = categoryDetailsOutput.getResultValue().getWordsList();
                                ArrayList<AdsBottom> adsBottoms = categoryDetailsOutput.getResultValue().getAdsBottom();
                                ArrayList<AdsTop> adsTops = categoryDetailsOutput.getResultValue().getAdsTop();

                                db.addTopAds(adsTops, languageFrom, languageTo,"wordlist");
                                db.addBottomAds(adsBottoms, languageFrom, languageTo,"wordlist");
                                db.addWords(wordsList, languageFrom, languageTo);

                                return wordsList;
                            }

                            @Override
                            protected void onPostExecute(ArrayList<WordsList> wordsList) {
                                super.onPostExecute(wordsList);

                                dismissDialogue();

                                if(catId != 3)
                                    wordsList = db.getWordsList(languageFrom, languageTo, catId);
                                else
                                    wordsList = db.getWordsList(languageFrom, languageTo, catId,noOfItemsToShow);

                                setWordsList(wordsList);

                                homeScreenAdapter = new AdapterCategoryDetails(ActivityCategoryDetails.this, getSupportFragmentManager(), getWordsList(),catName);
                                viewPager.setAdapter(homeScreenAdapter);
                                homeScreenAdapter.setWordsList(wordsList);

                                startTimerForTopAds();
                                startBottomAds();

                            }
                        }.execute();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Can not connect with server\nPlease try again",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onFailure(Call<CategoryDetailsOutput>call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
            }
        });
    }
    private void showProgressDialogue() {
        progressDoalog = new ProgressDialog(ActivityCategoryDetails.this);
        progressDoalog.setTitle("Loading....");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

    }

    private void dismissDialogue() {
        if (progressDoalog != null) {
            if (progressDoalog.isShowing()) {
                progressDoalog.dismiss();
            }
        }
    }
    private void initExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this, com.google.android.exoplayer2.util.Util.getUserAgent(this, "Globol"), (TransferListener<? super DataSource>) bandwidthMeter);
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.setPlayWhenReady(true);
    }

    private void init(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = (ViewPager)findViewById(R.id.viewPager);
//        homeScreenAdapter = new AdapterCategoryDetails(this, getSupportFragmentManager(), getWordsList(),catName);
//        viewPager.setAdapter(homeScreenAdapter);

        img_topAds = (ImageView)findViewById(R.id.img_topAd);
        img_bottomAds = (ImageView)findViewById(R.id.img_bottomAd);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        LinearLayout ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        LinearLayout ll_aboutUs = (LinearLayout) view.findViewById(R.id.ll_aboutUs);
        LinearLayout ll_aboutGlobol = (LinearLayout) view.findViewById(R.id.ll_aboutGlobol);
        LinearLayout ll_donate = (LinearLayout) view.findViewById(R.id.ll_donate);
        LinearLayout ll_contact = (LinearLayout) view.findViewById(R.id.ll_contact);

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(ActivityCategoryDetails.this, ActivityLandingPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fromLanguage",languageFrom);
                intent.putExtra("toLanguage",languageTo);
                startActivity(intent);
                finish();
            }
        });
        ll_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityCategoryDetails.this, ActivityAboutUs.class);
                startActivity(in);
            }
        });
        ll_aboutGlobol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityCategoryDetails.this, ActivityAboutGlobol.class);
                startActivity(in);
            }
        });
        ll_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityCategoryDetails.this, ActivityAboutDonate.class);
                startActivity(in);
            }
        });
        ll_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityCategoryDetails.this, ActivityContactUs.class);
                startActivity(in);
            }
        });
    }

    public boolean hasNext(){
        int size = getWordsList().size();
        int currentScreen = viewPager.getCurrentItem();
        currentScreen = currentScreen +1;
        if(currentScreen < size){
            return true;
        }else{
            return false;
        }
    }
    public boolean hasPrevious(){
        int currentScreen = viewPager.getCurrentItem();
        currentScreen = currentScreen -1;
        if(currentScreen >= 0){
            return true;
        }else {
            return false;
        }
    }
    public void selectPreviousScreen(){
        if(viewPager != null){
            int currentScreen = viewPager.getCurrentItem();
            currentScreen = currentScreen -1;
            if(currentScreen >= 0){
                viewPager.setCurrentItem(currentScreen);
            }else {
                Toast.makeText(ActivityCategoryDetails.this,"This is the first item",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void selectNextScreen(){
        if(viewPager != null){
            int size = getWordsList().size();
            int currentScreen = viewPager.getCurrentItem();
            currentScreen = currentScreen +1;
            if(currentScreen < size){
                viewPager.setCurrentItem(currentScreen);
            }else{
                Toast.makeText(ActivityCategoryDetails.this,"You are end of screen",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(isFavourite){
                Intent intent = new Intent(this, ActivityFavourite.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("languageFrom",languageFrom);
                intent.putExtra("languageTo",languageTo);
                intent.putExtra("catId",catId);
                startActivity(intent);
                finish();
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent intent = new Intent(this, ActivityLandingPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("fromLanguage",languageFrom);
            intent.putExtra("toLanguage",languageTo);
            startActivity(intent);
            finish();
            return true;
        }else if (id == R.id.action_favourite) {
            Intent intent = new Intent(ActivityCategoryDetails.this, ActivityFavourite.class);
            intent.putExtra("languageFrom",languageFrom);
            intent.putExtra("languageTo",languageTo);
            intent.putExtra("catId",catId);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.whatsapp");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_title));
                String sAux = "\nLet me recommend you this application\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+ getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startBottomAds() {
        final ArrayList<pojoAds> ads = db.getBottomAds(languageFrom, languageTo,"wordlist");
        bottom_ad_incrementer = 0;
        showBottomAd(ads);
    }

    private void showBottomAd(final ArrayList<pojoAds> ads) {

        String promoIMage = ads.get(bottom_ad_incrementer).getPromoImage();
        final String reDirectUrl = ads.get(bottom_ad_incrementer).getUrl();
        String imgUrl = mainAdImageUrl + promoIMage;

        Picasso.with(ActivityCategoryDetails.this).load(imgUrl)
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
        ArrayList<pojoAds> ads = db.getTopAds(languageFrom, languageTo,"wordlist");
        top_ad_incrementer = 0;
        showTopAd(ads);
    }

    private void showTopAd(final ArrayList<pojoAds> ads) {
        String promoIMage = ads.get(top_ad_incrementer).getPromoImage();
        final String reDirectUrl = ads.get(top_ad_incrementer).getUrl();
        String imgUrl = mainAdImageUrl + promoIMage;

        Picasso.with(ActivityCategoryDetails.this).load(imgUrl)
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

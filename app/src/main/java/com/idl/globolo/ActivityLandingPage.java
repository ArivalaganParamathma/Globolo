package com.idl.globolo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idl.globolo.adapters.CategoryAdapter;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoViewAdapter.pojoAds;
import com.idl.globolo.pojoViewAdapter.pojoCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.idl.globolo.R;

import static com.idl.globolo.utils.Constants.isFirstTime;
import static com.idl.globolo.utils.Constants.mainAdImageUrl;

public class ActivityLandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private CardView cardViewLanguageChoose;
    private TextView tv_fromLanguage,
            tv_toLanguage;
    private Button bt_cancel,
            bt_update;
    private ImageView img_topAds,
            img_bottomAds;
    private AlertDialog mDialog;

    private JCGSQLiteHelper db;
    private ApplicationClass applicationClass;
    private TimerTask timerTask;
    private Timer timer;
    private final Handler handlerBottom = new Handler();
    private final Handler handlerTop = new Handler();
    private final Handler handler = new Handler();
    private int fromLanguage,
            toLanguage;
    private RecyclerView rv_categories;
    private ProgressBar progressBar_cyclic;
    private RelativeLayout rl_root;
    private RelativeLayout rl_initialInfo;

    private CategoryAdapter categoryAdapter;
    private  int bottom_ad_incrementer = 0,
            top_ad_incrementer = 0;
    private boolean alreadySet = false;

    /*Shared Pref*/
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private TextView tv_choose_language;
    private boolean isGridSpacingDone;
    private ArrayList<pojoAds> ads;
    private ArrayList<pojoAds> adsTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Gloβolo");

        init(toolbar);
        db = new JCGSQLiteHelper(this);
        applicationClass = (ApplicationClass) getApplication();

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        fromLanguage = sharedPref.getInt("fromLanguage", 0);
        toLanguage = sharedPref.getInt("toLanguage", 0);

        if(isFirstTime){
            startTimer();
        }else{
            if(fromLanguage == 0){
                progressBar_cyclic.setVisibility(View.GONE);
                rl_initialInfo.setVisibility(View.VISIBLE);
            }else{
                progressBar_cyclic.setVisibility(View.GONE);
                cardViewLanguageChoose.setVisibility(View.GONE);
                rl_initialInfo.setVisibility(View.GONE);
                rl_root.setBackgroundColor(Color.WHITE);

                startTimerForTopAds();
                startBottomAds();
                setGridAdapter();
            }

        }


        tv_fromLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] languageList = db.getLanguageNames();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityLandingPage.this);
                mBuilder.setTitle("Change Language");
                mBuilder.setSingleChoiceItems(languageList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedLanguage = languageList[i];
                        fromLanguage = db.getLanguageId(selectedLanguage);
                        toLanguage = 0;

                        tv_fromLanguage.setText(selectedLanguage);
                        tv_toLanguage.setText("");

                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_fromLanguage.setText("");
                        fromLanguage = 0;
                        mDialog.dismiss();
                    }
                });
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });

                mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        tv_toLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] translateLanguageList = db.getTranslateLanguageNames(fromLanguage);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityLandingPage.this);
                mBuilder.setTitle("Change Language");
                mBuilder.setSingleChoiceItems(translateLanguageList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String selectedLanguage = translateLanguageList[i];
                        toLanguage = db.getLanguageId(selectedLanguage);
                        tv_toLanguage.setText(selectedLanguage);

                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_toLanguage.setText("");
                        toLanguage = 0;
                        fromLanguage = sharedPref.getInt("fromLanguage", 0);
                        toLanguage = sharedPref.getInt("toLanguage", 0);
                        mDialog.dismiss();
                    }
                });
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });

                mDialog = mBuilder.create();
                mDialog.show();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewLanguageChoose.setVisibility(View.GONE);
                fromLanguage = sharedPref.getInt("fromLanguage", 0);
                toLanguage = sharedPref.getInt("toLanguage", 0);

                if(categoryAdapter == null) {
                    rl_initialInfo.setVisibility(View.VISIBLE);
                    rl_root.setBackgroundColor(Color.DKGRAY);
                }
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strFromLanguage = tv_fromLanguage.getText().toString();
                String strToLanguage = tv_toLanguage.getText().toString();
                if(!strFromLanguage.equalsIgnoreCase("") && !strToLanguage.equalsIgnoreCase("")){
                    if(fromLanguage != toLanguage){
                        cardViewLanguageChoose.setVisibility(View.GONE);

                        startTimerForTopAds();
                        startBottomAds();

                        setGridAdapter();
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Choose different Language ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Please Choose Language ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setGridAdapter() {
          /*Store userId*/
        editor.putInt("fromLanguage", fromLanguage);
        editor.putInt("toLanguage", toLanguage);
        editor.apply();

        ArrayList<pojoCategory> categories = db.getCategoryList(fromLanguage, toLanguage);
        categoryAdapter = new CategoryAdapter(ActivityLandingPage.this, categories, fromLanguage, toLanguage);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ActivityLandingPage.this, 3);
        rv_categories.setLayoutManager(mLayoutManager);
        if(!isGridSpacingDone){
            rv_categories.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
            isGridSpacingDone = true;
        }
        rv_categories.setItemAnimator(new DefaultItemAnimator());
        rv_categories.setAdapter(categoryAdapter);
    }

    private void startBottomAds() {
        ads = db.getBottomAds(fromLanguage, toLanguage,"main");
        bottom_ad_incrementer = 0;
        if(handlerBottom!=null){
            handlerBottom.removeCallbacks(runnableBottomAds);
        }
        showBottomAd();
    }

    private void showBottomAd() {

        if(ads.size() > 0){
            String promoIMage = ads.get(bottom_ad_incrementer).getPromoImage();
            final String reDirectUrl = ads.get(bottom_ad_incrementer).getUrl();
            String imgUrl = mainAdImageUrl + promoIMage;

            Picasso.with(ActivityLandingPage.this).load(imgUrl)
                    .into(img_bottomAds, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            handlerBottom.postDelayed(runnableBottomAds,2000);
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
    Runnable runnableBottomAds = new Runnable() {
        @Override
        public void run() {
            if(bottom_ad_incrementer < ads.size()-1){
                bottom_ad_incrementer = bottom_ad_incrementer+1;
            }else{
                bottom_ad_incrementer = 0;
            }
            showBottomAd();
        }
    };
    private void startTimerForTopAds() {
        adsTop = db.getTopAds(fromLanguage, toLanguage,"main");
        top_ad_incrementer = 0;
        if(handlerTop!=null){
            handlerTop.removeCallbacks(runnableTopAds);
        }
        showTopAd();
    }

    private void showTopAd() {
        if(adsTop.size() > 0){
            String promoIMage = adsTop.get(top_ad_incrementer).getPromoImage();
            final String reDirectUrl = adsTop.get(top_ad_incrementer).getUrl();
            String imgUrl = mainAdImageUrl + promoIMage;

            Picasso.with(ActivityLandingPage.this).load(imgUrl)
                    .into(img_topAds, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            handlerTop.postDelayed(runnableTopAds,2000);
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
    Runnable runnableTopAds = new Runnable() {
        @Override
        public void run() {
            if(top_ad_incrementer < adsTop.size()-1){
                top_ad_incrementer = top_ad_incrementer+1;
            }else{
                top_ad_incrementer = 0;
            }
            showTopAd();
        }
    };
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 0, 100); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        boolean alreadyDataLoaded = applicationClass.sharedPref.getBoolean("isMainPageLoaded", false);
                        if (alreadyDataLoaded) {
                            stoptimertask();
                            progressBar_cyclic.setVisibility(View.GONE);
                            rl_initialInfo.setVisibility(View.VISIBLE);
                            isFirstTime = false;
                        }
                    }
                });
            }
        };
    }

    private void init(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        cardViewLanguageChoose = (CardView) findViewById(R.id.cardViewLanguageChoose);
        cardViewLanguageChoose.setVisibility(View.GONE);
        tv_fromLanguage = (TextView) findViewById(R.id.tv_nativeLanguage);
        tv_toLanguage = (TextView) findViewById(R.id.tv_translateLanguage);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_update = (Button) findViewById(R.id.bt_update);

        rv_categories = (RecyclerView) findViewById(R.id.rv_categories);
        progressBar_cyclic = (ProgressBar)findViewById(R.id.progressBar_cyclic);
        rl_root = (RelativeLayout)findViewById(R.id.rl_root);
        rl_initialInfo = (RelativeLayout)findViewById(R.id.rl_initialInfo);
        rl_initialInfo.setVisibility(View.GONE);

        tv_choose_language = findViewById(R.id.tv_chooseLanguage);
        tv_choose_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewLanguageChoose.setVisibility(View.VISIBLE);
                rl_initialInfo.setVisibility(View.GONE);
                rl_root.setBackgroundColor(Color.WHITE);
            }
        });

        img_topAds = (ImageView)findViewById(R.id.img_topAd);
        img_bottomAds = (ImageView)findViewById(R.id.img_bottomAd);


        NavigationView view = (NavigationView) findViewById(R.id.nav_view);
        view.setNavigationItemSelectedListener(this);


        LinearLayout ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        LinearLayout ll_aboutUs = (LinearLayout) view.findViewById(R.id.ll_aboutUs);
        LinearLayout ll_aboutGlobol = (LinearLayout) view.findViewById(R.id.ll_aboutGlobol);
        LinearLayout ll_donate = (LinearLayout) view.findViewById(R.id.ll_donate);
        LinearLayout ll_contact = (LinearLayout) view.findViewById(R.id.ll_contact);

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        ll_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityLandingPage.this, ActivityAboutUs.class);
                startActivity(in);
            }
        });
        ll_aboutGlobol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityLandingPage.this, ActivityAboutGlobol.class);
                startActivity(in);
            }
        });
        ll_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityLandingPage.this, ActivityAboutDonate.class);
                startActivity(in);
            }
        });
        ll_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(ActivityLandingPage.this, ActivityContactUs.class);
                startActivity(in);
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_language) {
            cardViewLanguageChoose.setVisibility(View.VISIBLE);
            rl_initialInfo.setVisibility(View.GONE);
            rl_root.setBackgroundColor(Color.WHITE);
            return true;
        }else if (id == R.id.action_favourite) {
            Intent intent = new Intent(ActivityLandingPage.this, ActivityFavourite.class);
            intent.putExtra("languageFrom",fromLanguage);
            intent.putExtra("languageTo",toLanguage);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_share){
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setPackage("com.whatsapp");
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_title));
                String sAux = "\nHi this app is very useful to learn different languages through your native language.Hope you can try and let me know\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+ getPackageName();//com.idl.globolo
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {

        } */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

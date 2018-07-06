package com.idl.globolo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.idl.globolo.ActivityCategoryDetails;
import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoFavourite.CreateFavouriteInput;
import com.idl.globolo.pojoFavourite.FavouriteResponse;
import com.idl.globolo.retrofitSupport.ApiClient;
import com.idl.globolo.retrofitSupport.ApiInterface;
import com.squareup.picasso.Picasso;

import com.idl.globolo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.idl.globolo.utils.Constants.categoryImageUrl;
import static com.idl.globolo.utils.Constants.userId;
import static com.idl.globolo.utils.Constants.wordsAudio;
import static com.idl.globolo.utils.Constants.wordsImageUrl;

/**
 * Created by AParamathma on 25-03-2018.
 */

@SuppressLint("ValidFragment")
public class FragmentCategoryDetails extends Fragment {

    private JCGSQLiteHelper db;
    private Context mContext;
    private ImageView img_cateImage,
            img_favourite,
            img_wordFromSpeaker,
            img_wordToSpeaker,
            img_wordImage,
            img_leftArrow,
            img_rightArrow;
    private TextView tv_catName;
    private TextView bt_wordFromName,
            bt_wordToName;

    private WordsList wordsList;
    private RequestOptions requestOptions;
    private String catName;

    private boolean isToSpeaker = false;
    private boolean isFromSpeaker = false;

    private boolean isActivityCreated = false;
    private int position;


    private DefaultTrackSelector trackSelector;
    private DefaultDataSourceFactory dataSourceFactory;
    private SimpleExoPlayer player;

    public FragmentCategoryDetails(Context context) {

    }

    public FragmentCategoryDetails(Context context, WordsList wordsList, int position, String catName) {
        this.mContext = context;
        this.wordsList = wordsList;
        this.catName = catName;
        this.position = position;

        db = new JCGSQLiteHelper(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_details, container, false);

        img_cateImage = view.findViewById(R.id.img_category);
        img_favourite = view.findViewById(R.id.img_favourite);
        img_leftArrow = view.findViewById(R.id.img_left);
        img_rightArrow = view.findViewById(R.id.img_right);

        img_wordImage = view.findViewById(R.id.img_wordImage);
        img_wordFromSpeaker = view.findViewById(R.id.img_wordFromSound);
        img_wordToSpeaker = view.findViewById(R.id.img_wordToSound);

        tv_catName = view.findViewById(R.id.tv_categoryName);

        bt_wordFromName = view.findViewById(R.id.bt_wordFromName);
        bt_wordToName = view.findViewById(R.id.bt_wordToName);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPlayer();


        /*Glide Request Option*/
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_language);

        String catUrl = categoryImageUrl + wordsList.getCatImage();
        Picasso.with(getActivity()).load(catUrl).into(img_cateImage);



        if(!((ActivityCategoryDetails)getContext()).isFavourite) {
            tv_catName.setText(catName);
        }else{
            tv_catName.setText(wordsList.getCatName());
        }

        bt_wordFromName.setText(wordsList.getWordFromName());
        String wordToName = wordsList.getWordToName();
        String wordToPronounciation = wordsList.getWordToPronounciation();
        if(wordToName.equalsIgnoreCase(wordToPronounciation)){
            bt_wordToName.setText(wordToName);
        }else{
            bt_wordToName.setText(wordToName + "\n" + wordToPronounciation);
        }


        try {
            String wordUrl = wordsImageUrl + wordsList.getWordImage();
            /*Glide.with(getActivity())
                    .setDefaultRequestOptions(requestOptions)
                    .load(wordUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Failed ", e.getMessage() + "");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(img_wordImage);*/
            Picasso.with(getActivity()).load(wordUrl).into(img_wordImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isFavourite = Boolean.parseBoolean(wordsList.getFavouriteFlag());
        if (isFavourite) {
            if(!((ActivityCategoryDetails)getActivity()).isFavourite) {
                img_favourite.setImageResource(R.drawable.img_favourite_selected);
            }else {
                img_favourite.setImageResource(R.drawable.ic_delete);
            }
        } else {
            img_favourite.setImageResource(R.drawable.img_favourite_unselected);
        }


        if(!isActivityCreated)
            setLeftRightArrowState();


        img_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    processFavouriteAction();
                }catch (Exception e){
                 e.printStackTrace();
                }

            }
        });

        img_rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityCategoryDetails) getActivity()).selectNextScreen();

            }
        });
        img_leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityCategoryDetails) getActivity()).selectPreviousScreen();

            }
        });
        img_wordFromSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isToSpeaker = false;
                isFromSpeaker = true;
                img_wordFromSpeaker.setClickable(false);

                img_wordToSpeaker.setImageResource(R.drawable.ic_speaker);
                String url = wordsAudio + wordsList.getWordFromSound();
                Uri uri = Uri.parse(url);
                try {
//                    MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//                    ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(firstSource);
//                    player.prepare(concatenatingMediaSource);


                    String userAgent = Util.getUserAgent(getContext(), "Globolo-j");
                    ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                            uri,
                            new DefaultDataSourceFactory(getContext(), userAgent),
                            new DefaultExtractorsFactory(),
                            null,
                            null
                    );
                    player.prepare(mediaSource);
                    player.setPlayWhenReady(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        img_wordToSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isToSpeaker = true;
                isFromSpeaker = false;
                img_wordToSpeaker.setClickable(false);

                img_wordFromSpeaker.setImageResource(R.drawable.ic_speaker);
                String url = wordsAudio + wordsList.getWordToSound();
                Uri uri = Uri.parse(url);

                try {
//                    MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
//                    ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(firstSource);
//                    player.prepare(concatenatingMediaSource);

                    String userAgent = Util.getUserAgent(getContext(), "Globolo");
                    ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                            uri,
                            new DefaultDataSourceFactory(getContext(), userAgent),
                            new DefaultExtractorsFactory(),
                            null,
                            null
                    );
                    player.prepare(mediaSource);
                    player.setPlayWhenReady(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });




    }
    private void initPlayer() {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                                getContext(),
                                null,
                                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
                        );
                        TrackSelector trackSelector = new DefaultTrackSelector();
                        player = ExoPlayerFactory.newSimpleInstance(
                                renderersFactory,
                                trackSelector
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });




           /* BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(getActivity(), com.google.android.exoplayer2.util.Util.getUserAgent(getActivity(), "Globol-j"), (TransferListener<? super DataSource>) bandwidthMeter);
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            player.setPlayWhenReady(true);*/


            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case Player.STATE_BUFFERING:
                            break;
                        case Player.STATE_ENDED:
                            if (isToSpeaker) {
                                img_wordToSpeaker.setImageResource(R.drawable.ic_speaker);
                                isToSpeaker = false;

                            }
                            if (isFromSpeaker) {
                                img_wordFromSpeaker.setImageResource(R.drawable.ic_speaker);
                                isFromSpeaker = false;
                            }
                            img_wordToSpeaker.setClickable(true);
                            img_wordFromSpeaker.setClickable(true);
                            player.stop();
                            break;
                        case Player.STATE_IDLE:
                            break;
                        case Player.STATE_READY:
                            if (isToSpeaker) {
                                img_wordToSpeaker.setImageResource(R.drawable.ic_speaker_wave);
                            }
                            if (isFromSpeaker) {
                                img_wordFromSpeaker.setImageResource(R.drawable.ic_speaker_wave);
                            }
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    if (isToSpeaker) {
                        isToSpeaker = false;
                    }
                    if (isFromSpeaker) {
                        isFromSpeaker = false;
                    }
                    img_wordToSpeaker.setClickable(true);
                    img_wordFromSpeaker.setClickable(true);
                    player.stop();
                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        }
    }
    private void processFavouriteAction() {
        boolean isFavourite = Boolean.parseBoolean(wordsList.getFavouriteFlag());
        if (isFavourite) {
            img_favourite.setImageResource(R.drawable.img_favourite_unselected);
            wordsList.setFavouriteFlag(false + "");

            if(((ActivityCategoryDetails)getContext()).isFavourite) {
                ((ActivityCategoryDetails)mContext).removePosition(position);
            }

            String favId = wordsList.getFavId();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<FavouriteResponse> callUser = apiService.deleteWordFavoutite(userId, Integer.parseInt(favId));
            callUser.enqueue(new Callback<FavouriteResponse>() {
                @Override
                public void onResponse(Call<FavouriteResponse> call, Response<FavouriteResponse> response) {
                    FavouriteResponse body = response.body();
                    String status = body.getStatus();
                    if (status.equalsIgnoreCase("Success")) {
                        if(getActivity()!=null)
                            Toast.makeText(getActivity(), body.getMessage(), Toast.LENGTH_SHORT).show();

                        int favId = body.getResultValue().getFavId();
                        wordsList.setFavId(String.valueOf(favId));

                        db.updateWord("false",Integer.parseInt(wordsList.getFavId()),wordsList.getNumberId(),wordsList.getLanguageFrom(),wordsList.getLanguageTo());

                    }
                }

                @Override
                public void onFailure(Call<FavouriteResponse> call, Throwable t) {

                }
            });
        } else {
            wordsList.setFavouriteFlag(true + "");
            if(!((ActivityCategoryDetails)getContext()).isFavourite) {
                img_favourite.setImageResource(R.drawable.img_favourite_selected);
            }else {
                img_favourite.setImageResource(R.drawable.ic_delete);
            }
            CreateFavouriteInput createFavouriteInput = new CreateFavouriteInput();
            createFavouriteInput.setActive(true);
            createFavouriteInput.setCatId(wordsList.getCatId());
            createFavouriteInput.setCreateDate(System.currentTimeMillis() + "");
            createFavouriteInput.setFavId(0);
            createFavouriteInput.setLangFromId(wordsList.getLanguageFrom());
            createFavouriteInput.setLangToId(wordsList.getLanguageTo());
            createFavouriteInput.setModifiedDate(System.currentTimeMillis() + "");
            createFavouriteInput.setNumberId(wordsList.getNumberId());
            createFavouriteInput.setUserId(userId);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<FavouriteResponse> callUser = apiService.createWordFavourite(createFavouriteInput);
            callUser.enqueue(new Callback<FavouriteResponse>() {
                @Override
                public void onResponse(Call<FavouriteResponse> call, Response<FavouriteResponse> response) {
                    FavouriteResponse body = response.body();
                    String status = body.getStatus();
                    if (status.equalsIgnoreCase("Success")) {

                        int favId = body.getResultValue().getFavId();
                        wordsList.setFavId(String.valueOf(favId));

                        db.updateWord(wordsList.getFavouriteFlag(),Integer.parseInt(wordsList.getFavId()),wordsList.getNumberId(),wordsList.getLanguageFrom(),wordsList.getLanguageTo());

                        Toast.makeText(getActivity(), body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FavouriteResponse> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(((ActivityCategoryDetails) getActivity())!=null){
                isActivityCreated = true;
                setLeftRightArrowState();
            }else{
                isActivityCreated = false;
            }
        }
    }

    private void setLeftRightArrowState() {
        boolean hasNext = ((ActivityCategoryDetails) getActivity()).hasNext();
        if(hasNext){
            img_rightArrow.setAlpha((float) 1.0);
        }
        else{
            img_rightArrow.setAlpha((float) 0.5);
        }

        boolean hasPrevious = ((ActivityCategoryDetails) getActivity()).hasPrevious();
        if(hasPrevious){
            img_leftArrow.setAlpha((float) 1.0);
        }else{
            img_leftArrow.setAlpha((float) 0.5);
        }
    }
}

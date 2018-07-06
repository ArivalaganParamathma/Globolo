package com.idl.globolo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idl.globolo.ActivityCategoryDetails;
import com.idl.globolo.ActivityFavourite;
import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.database.JCGSQLiteHelper;
import com.idl.globolo.pojoFavourite.FavouriteResponse;
import com.idl.globolo.retrofitSupport.ApiClient;
import com.idl.globolo.retrofitSupport.ApiInterface;

import java.util.List;

import com.idl.globolo.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.idl.globolo.utils.Constants.userId;

/**
 * Created by arivalagan on 4/9/2018.
 */

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.MyViewHolder> {
    private final int lngFrm,
            lngTo;
    private Context mContext;
    private List<WordsList> wordsLists;
    private JCGSQLiteHelper db;
    private boolean isCategoryFilterEnabled;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_categoryName, tv_categoryId;
        public ImageView img_delete;
        public Button imgBt_fromLang,
                imgBt_toLang;
        public RelativeLayout rl_parent;

        public MyViewHolder(View view) {
            super(view);
            tv_categoryName = view.findViewById(R.id.tv_categoryName);
            tv_categoryId = view.findViewById(R.id.tv_categoryId);
            img_delete =  view.findViewById(R.id.img_delete);
            imgBt_fromLang = view.findViewById(R.id.imgBt_fromLang);
            imgBt_toLang = view.findViewById(R.id.imgBt_toLang);
            rl_parent = view.findViewById(R.id.rl_parent);
        }
    }


    public FavouriteListAdapter(Context context, List<WordsList> wordsLists, int languageFrom, int languageTo, boolean isCategoryFilterEnabled) {
        this.mContext = context;
        this.wordsLists = wordsLists;
        lngFrm = languageFrom;
        lngTo = languageTo;
        this.isCategoryFilterEnabled = isCategoryFilterEnabled;
        db = new JCGSQLiteHelper(context);
    }

    @Override
    public FavouriteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite, parent, false);

        return new FavouriteListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteListAdapter.MyViewHolder holder, final int position) {
        final WordsList wordsList = wordsLists.get(position);
        holder.tv_categoryName.setText(wordsList.getCatName());
        holder.tv_categoryId.setText(wordsList.getCatId() + "");

        holder.imgBt_fromLang.setText(wordsList.getWordFromName());
        holder.imgBt_toLang.setText(wordsList.getWordToName());

        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityCategoryDetails.class);
                intent.putExtra("catId",wordsList.getCatId()+"");
                intent.putExtra("catName",wordsList.getCatName());
                intent.putExtra("languageFrom",lngFrm);
                intent.putExtra("languageTo",lngTo);
                intent.putExtra("isFavourite",true);
                intent.putExtra("position",position);
                intent.putExtra("isCatFilter",isCategoryFilterEnabled);
                mContext.startActivity(intent);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordsList.setFavouriteFlag(false + "");

                String favId = wordsList.getFavId();
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<FavouriteResponse> callUser = apiService.deleteWordFavoutite(userId, Integer.parseInt(favId));
                callUser.enqueue(new Callback<FavouriteResponse>() {
                    @Override
                    public void onResponse(Call<FavouriteResponse> call, Response<FavouriteResponse> response) {
                        FavouriteResponse body = response.body();
                        String status = body.getStatus();
                        if (status.equalsIgnoreCase("Success")) {
                            if(wordsLists.size() > 0){
                                Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();
                                int favId = body.getResultValue().getFavId();
                                wordsList.setFavId(String.valueOf(favId));
                                wordsLists.remove(position);

                                db.updateWord("false",Integer.parseInt(wordsList.getFavId()),wordsList.getNumberId(),wordsList.getLanguageFrom(),wordsList.getLanguageTo());
                                ((ActivityFavourite)mContext).setInfo();
                                notifyDataSetChanged();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<FavouriteResponse> call, Throwable t) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return wordsLists.size();
    }
}

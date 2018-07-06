package com.idl.globolo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.idl.globolo.ActivityCategoryDetails;
import com.idl.globolo.ActivityCategorySubList;
import com.idl.globolo.pojoViewAdapter.pojoCategory;

import java.util.List;

import com.idl.globolo.R;

import static com.idl.globolo.utils.Constants.categoryImageUrl;


/**
 * Created by AParamathma on 25-03-2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<pojoCategory> categories;
    private int lngFrm,
            lngTo;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catName, catId;
        public ImageView catImage;

        public MyViewHolder(View view) {
            super(view);
            catName = (TextView) view.findViewById(R.id.tv_categoryName);
            catId = (TextView) view.findViewById(R.id.tv_categoryId);
            catImage = (ImageView) view.findViewById(R.id.img_category);
        }
    }


    public CategoryAdapter(Context context, List<pojoCategory> categories, int languageFrom, int languageTo) {
        this.mContext = context;
        this.categories = categories;
        lngFrm = languageFrom;
        lngTo = languageTo;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        pojoCategory category = categories.get(position);
        holder.catName.setText(category.getCatName());
        holder.catId.setText(category.getCatId()+"");

        String url =  categoryImageUrl+category.getCatImage();
//        Picasso.with(mContext).load(url).into(holder.catImage);

        try {
            Glide.with(mContext)
                    .load(url)
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
                    .into(holder.catImage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.catImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catId = holder.catId.getText().toString();
                String catName = holder.catName.getText().toString();
                if(Integer.parseInt(catId) == 3){
                    Intent intent = new Intent(mContext, ActivityCategorySubList.class);
                    intent.putExtra("catId",catId);
                    intent.putExtra("catName",catName);
                    intent.putExtra("languageFrom",lngFrm);
                    intent.putExtra("languageTo",lngTo);
                    intent.putExtra("isFavourite",false);
                    mContext.startActivity(intent);
                }else{
                    Intent intent = new Intent(mContext, ActivityCategoryDetails.class);
                    intent.putExtra("catId",catId);
                    intent.putExtra("catName",catName);
                    intent.putExtra("languageFrom",lngFrm);
                    intent.putExtra("languageTo",lngTo);
                    intent.putExtra("isFavourite",false);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

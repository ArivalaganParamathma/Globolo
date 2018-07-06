package com.idl.globolo.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.idl.globolo.POJO.WordsList;
import com.idl.globolo.fragment.FragmentCategoryDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AParamathma on 25-03-2018.
 */

public class AdapterCategoryDetails extends FragmentStatePagerAdapter {

    private Context context = null;
    private List<WordsList> wordsLists = new ArrayList<>();
    public List<FragmentCategoryDetails> fragmentsList;
    private String catName;

    public AdapterCategoryDetails(Context context, FragmentManager fm, List<WordsList> wordsLists, String catName) {
        super(fm);
        this.context = context;
        this.wordsLists = wordsLists;
        this.catName = catName;
        fragmentsList = new ArrayList<>();
    }

    @Override
    public FragmentCategoryDetails getItem(int position) {
        FragmentCategoryDetails myFragment = new FragmentCategoryDetails(context, wordsLists.get(position), position,catName);

        if (fragmentsList.size() > position) {
            fragmentsList.remove(position);
        }
        try {
            fragmentsList.add(position, myFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myFragment;
    }

    public List<FragmentCategoryDetails> getFragment() {
        return fragmentsList;
    }

    @Override
    public int getCount() {
        return wordsLists.size();
    }

    public WordsList getItemPosition(int position) {
        return wordsLists.get(position);
    }

    public List<WordsList> getWordsList() {
        return wordsLists;
    }

    public void setWordsList(List<WordsList> wordsList) {
        this.wordsLists = wordsList;
    }
    public void removeWord(int position){
        this.wordsLists.remove(position);
        notifyDataSetChanged();
    }
}

package com.example.android.mbejaranoe.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Manolo on 09/10/2017.
 */

public class RecipeDetailFragment extends Fragment {

    public RecipeDetailFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the Recipe detail fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        return rootView;
    }
}

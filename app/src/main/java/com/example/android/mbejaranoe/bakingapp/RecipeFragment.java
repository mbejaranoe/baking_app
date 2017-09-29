package com.example.android.mbejaranoe.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Manolo on 29/09/2017.
 */

public class RecipeFragment extends Fragment {

    //Constructor for instantiating the fragment
    public RecipeFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Recipe fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Get a reference to the CardView in the fragment layout
        CardView recipeCardView = rootView.findViewById(R.id.recipe_card_view);

        // Get a reference to the ImageView in the fragment layout
        ImageView recipeImageView = rootView.findViewById(R.id.recipe_image_view);
        recipeImageView.setImageResource(R.drawable.ic_baking_icon);

        // Get a reference to the name TextView in the fragment layout
        TextView recipeNameTextView = rootView.findViewById(R.id.recipe_name_text_view);
        recipeNameTextView.setText("Sopa de caracol");

        // Get a reference to the servings TextView in the fragment layout
        TextView recipeServingsTextView = rootView.findViewById(R.id.recipe_servings_text_view);
        recipeServingsTextView.setText("8 personajes");

        return rootView;
    }
}

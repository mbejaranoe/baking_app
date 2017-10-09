package com.example.android.mbejaranoe.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Manolo on 09/10/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Create the fragment for recipe details
        // RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        // Use the fragment manager and transaction to add the fragment to the screen
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction()
        //        .add(R.id.container, recipeDetailFragment)
        //        .commit();
    }
}

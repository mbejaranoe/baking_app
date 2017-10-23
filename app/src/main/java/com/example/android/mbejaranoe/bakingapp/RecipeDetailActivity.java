package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Manolo on 09/10/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    private String recipeName;
    private RecipeDetailFragment recipeDetailFragment;

    private final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private final String FRAGMENT_TAG = "recipeDetailFragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // only create a new fragment when it is not previously created
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("name")) {
                recipeName = intent.getStringExtra("name");
                setTitle(recipeName);
            }
            Log.v(LOG_TAG, "onCreate - savedInstanceState NULL");
            // Create the fragment for recipe details
            recipeDetailFragment = new RecipeDetailFragment();
            // Use the fragment manager and transaction to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, recipeDetailFragment, FRAGMENT_TAG)
                    .commit();
        } else {
            recipeName = savedInstanceState.getString("name");
            setTitle(recipeName);
            Log.v(LOG_TAG, "onCreate - savedInstanceState NOT NULL");

            RecipeDetailFragment newDetailFragment = new RecipeDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, newDetailFragment, FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("name", recipeName);

        Log.v(LOG_TAG, "onSaveInstanceState - recipeName: " + recipeName);
    }
}

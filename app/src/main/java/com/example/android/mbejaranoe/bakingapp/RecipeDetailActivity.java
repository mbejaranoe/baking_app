package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Manolo on 09/10/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    private String recipeName;
    private int recipe_Id;
    private RecipeDetailFragment recipeDetailFragment;

    private final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private final String FRAGMENT_TAG = "recipeDetailFragment";
    private final String SAVED_STATE_KEY = "savedState";
    private final String RECIPE_NAME_KEY = "name";
    private final String RECIPE_ID_KEY = "recipe_Id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // only create a new fragment when it is not previously created
        // find the retained fragment on activity restarts
        FragmentManager fragmentManager = getSupportFragmentManager();
        recipeDetailFragment = (RecipeDetailFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        // create the fragment and data the first time
        if (recipeDetailFragment == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(RECIPE_NAME_KEY)) {
                recipeName = intent.getStringExtra(RECIPE_NAME_KEY);
                setTitle(recipeName);
            }

            if (intent != null && intent.hasExtra(RECIPE_ID_KEY)) {
                recipe_Id = intent.getIntExtra(RECIPE_ID_KEY, -1);
            }

            Log.v(LOG_TAG, "onCreate - recipeDetailFragment NULL");

            // add the fragment
            recipeDetailFragment = new RecipeDetailFragment();
            Bundle args = new Bundle();
            args.putInt(RECIPE_ID_KEY, recipe_Id);
            recipeDetailFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.container, recipeDetailFragment, FRAGMENT_TAG)
                    .commit();
        } else {
            recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
            setTitle(recipeName);

            Log.v(LOG_TAG, "onCreate - recipeDetailFragment NOT NULL");

            Parcelable parcelable = savedInstanceState.getParcelable(SAVED_STATE_KEY);

            if (parcelable == null) {
                Log.v(LOG_TAG, "onCreate - parcelable NULL");
            } else {
                Log.v(LOG_TAG, "onCreate - parcelable NOT NULL");
            }
            recipeDetailFragment.getRecyclerView().getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(RECIPE_NAME_KEY, recipeName);
        outState.putInt(RECIPE_ID_KEY, recipe_Id);

        Parcelable parcelable = recipeDetailFragment.getLayoutManagerSavedInstanceState();
        outState.putParcelable(SAVED_STATE_KEY, parcelable);

        Log.v(LOG_TAG, "onSaveInstanceState - recipeName: " + recipeName);
        if (parcelable == null){
            Log.v(LOG_TAG, "onSaveInstanceState - parcelable NULL");
        } else {
            Log.v(LOG_TAG, "onSaveInstanceState - parcelable NOT NULL");
        }
    }
}

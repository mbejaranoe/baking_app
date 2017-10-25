package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Manolo on 25/10/2017.
 */

public class MasterDetailActivity extends AppCompatActivity {

    public final String LOG_TAG = MasterDetailActivity.class.getSimpleName();

    private final String RECIPE_ID_KEY = "recipe_Id";
    private final String STEP_INDEX_KEY = "stepIndex";
    private final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    private final String RESUME_POSITION_KEY = "resumePosition";
    private final String RECIPE_NAME_KEY = "name";

    private final String RECIPE_DETAILS_FRAGMENT_TAG = "recipeDetailsFragmentTag";
    private final String STEP_DETAILS_FRAGMENT_TAG = "stepDetailsFragmentTag";

    private int recipe_Id;
    private int stepIndex;
    private boolean shouldAutoPlay;
    private long resumePosition;
    private String recipeName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        if (savedInstanceState == null){
            Intent intent = getIntent();

            if (intent.hasExtra(RECIPE_ID_KEY)){
                recipe_Id = intent.getIntExtra(RECIPE_ID_KEY, -1);
            }

            if (intent.hasExtra(STEP_INDEX_KEY)){
                stepIndex = intent.getIntExtra(STEP_INDEX_KEY, -1);
            }

            if (intent.hasExtra(RECIPE_NAME_KEY)) {
                recipeName = intent.getStringExtra(RECIPE_NAME_KEY);
                setTitle(recipeName);
            }

            shouldAutoPlay = false;
            resumePosition = 0;

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle recipeArgs = new Bundle();
            recipeArgs.putInt(RECIPE_ID_KEY, recipe_Id);
            recipeDetailFragment.setArguments(recipeArgs);

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            Bundle stepArgs = new Bundle();
            stepArgs.putInt(STEP_INDEX_KEY, stepIndex);
            stepArgs.putInt(RECIPE_ID_KEY, recipe_Id);
            stepArgs.putBoolean(SHOULD_AUTO_PLAY_KEY, shouldAutoPlay);
            stepArgs.putLong(RESUME_POSITION_KEY, resumePosition);
            stepDetailFragment.setArguments(stepArgs);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .add(R.id.step_detail_container, stepDetailFragment, STEP_DETAILS_FRAGMENT_TAG)
                    .commit();
        }
    }
}

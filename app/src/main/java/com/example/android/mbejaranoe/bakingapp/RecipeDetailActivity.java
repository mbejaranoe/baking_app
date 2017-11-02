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

public class RecipeDetailActivity extends AppCompatActivity implements StepsViewHolder.OnStepClickListener{ // implements interface for click

    private String recipeName;
    private int recipe_Id;
    private int stepIndex;
    private boolean shouldAutoPlay;
    private long resumePosition;
    private boolean mTwoPane;

    private RecipeDetailFragment recipeDetailFragment;
    private StepDetailFragment stepDetailFragment;

    private final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();
    private final String RECIPE_DETAIL_FRAGMENT_TAG = "recipeDetailFragment";
    private final String STEP_DETAIL_FRAGMENT_TAG = "stepDetailFragment";

    private final String SAVED_STATE_KEY = "savedState";
    private final String RECIPE_NAME_KEY = "name";
    private final String STEP_INDEX_KEY = "stepIndex";
    private final String RECIPE_ID_KEY = "recipe_Id";
    private final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    private final String RESUME_POSITION_KEY = "resumePosition";
    private final String TWO_PANE_KEY = "twoPane";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // retrieve existing recipeDetailFragment
        recipeDetailFragment = (RecipeDetailFragment) fragmentManager
                .findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG);

        if (savedInstanceState != null) {
            recipeName = savedInstanceState.getString(RECIPE_NAME_KEY);
            recipe_Id = savedInstanceState.getInt(RECIPE_ID_KEY);
        }

        // Create recipeDetailFragment for the first time
        if (recipeDetailFragment == null) {
            recipeDetailFragment = new RecipeDetailFragment();

            if (savedInstanceState == null) {
                // get the intent data
                Intent intent = getIntent();
                if (intent != null && intent.hasExtra(RECIPE_NAME_KEY)) {
                    recipeName = intent.getStringExtra(RECIPE_NAME_KEY);
                }

                if (intent != null && intent.hasExtra(RECIPE_ID_KEY)) {
                    recipe_Id = intent.getIntExtra(RECIPE_ID_KEY, -1);
                }
            }

            Bundle argsRecipeDetailFragment = new Bundle();
            argsRecipeDetailFragment.putInt(RECIPE_ID_KEY, recipe_Id);

            Log.v(LOG_TAG, "recipe_Id: " + recipe_Id);

            recipeDetailFragment.setArguments(argsRecipeDetailFragment);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment, RECIPE_DETAIL_FRAGMENT_TAG)
                    .commit();
        }

        // set the Activity title
        setTitle(recipeName);

        // if we are in tablet mode, we also have to retrieve stepDetailFragment instance
        if (findViewById(R.id.master_detail_linear_layout) != null) { // tablet mode
            mTwoPane = true;

            // retrieve existing stepDetailFragment
            stepDetailFragment = (StepDetailFragment) fragmentManager
                    .findFragmentByTag(STEP_DETAIL_FRAGMENT_TAG);

            // Create stepDetailFragment for the first time
            if (stepDetailFragment == null) {
                stepDetailFragment = new StepDetailFragment();

                Bundle argsStepDetailFragment = new Bundle();
                stepIndex = 0;
                shouldAutoPlay = false;
                resumePosition = 0;
                argsStepDetailFragment.putInt(STEP_INDEX_KEY, stepIndex);
                argsStepDetailFragment.putInt(RECIPE_ID_KEY, recipe_Id);
                argsStepDetailFragment.putBoolean(SHOULD_AUTO_PLAY_KEY, shouldAutoPlay);
                argsStepDetailFragment.putLong(RESUME_POSITION_KEY, resumePosition);
                stepDetailFragment.setArguments(argsStepDetailFragment);

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment, STEP_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else { // phone mode
            mTwoPane = false;
        }
    }

    // define the behavior for onStepSelected
    public void onStepSelected(int stepId) {
        stepIndex = stepId;

        Log.v(LOG_TAG, "onStepSelected - stepIndex: " + stepIndex);

        if (mTwoPane) { // tablet mode

            Log.v(LOG_TAG, "onStepSelected - tablet mode");

            // create new StepDetailFragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment newFragment = new StepDetailFragment();

            // create new bundle with StepDetailFragment arguments
            Bundle argsStepDetailFragment = new Bundle();
            shouldAutoPlay = false;
            resumePosition = 0;
            argsStepDetailFragment.putInt(STEP_INDEX_KEY, stepIndex);
            argsStepDetailFragment.putInt(RECIPE_ID_KEY, recipe_Id);
            argsStepDetailFragment.putBoolean(SHOULD_AUTO_PLAY_KEY, shouldAutoPlay);
            argsStepDetailFragment.putLong(RESUME_POSITION_KEY, resumePosition);
            newFragment.setArguments(argsStepDetailFragment);

            Log.v(LOG_TAG, "onStepSelected - replace StepDetailFragment with new fragment");

            // replace StepDetailFragment with the new recently created fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, newFragment, STEP_DETAIL_FRAGMENT_TAG)
                    .commit();
        } else { // phone mode

            Log.v(LOG_TAG, "onStepSelected - phone mode");

            // create a new intent to launch the StepDetailActivity containing new StepDetailFragment
            Intent intent = new Intent(this, StepDetailActivity.class);

            // crete new bundle to pass extra to the new intent
            Bundle extras = new Bundle();
            shouldAutoPlay = false;
            resumePosition = 0;
            extras.putInt(STEP_INDEX_KEY, stepIndex);
            extras.putInt(RECIPE_ID_KEY, recipe_Id);
            extras.putBoolean(SHOULD_AUTO_PLAY_KEY, shouldAutoPlay);
            extras.putLong(RESUME_POSITION_KEY, resumePosition);
            intent.putExtras(extras);

            Log.v(LOG_TAG, "onStepSelected - launch new activity");

            // launch the StepDetailActivity
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(RECIPE_NAME_KEY, recipeName);
        outState.putInt(RECIPE_ID_KEY, recipe_Id);
    }
}

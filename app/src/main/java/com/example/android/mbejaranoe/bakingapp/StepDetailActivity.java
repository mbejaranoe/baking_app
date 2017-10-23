package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by Manolo on 10/10/2017.
 */

public class StepDetailActivity extends AppCompatActivity {

    StepDetailFragment stepDetailFragment;
    int stepIndex;
    int recipe_Id;
    String shortDescription;

    public final String LOG_TAG = StepDetailActivity.class.getSimpleName();

    private final String SHORT_DESCRIPTION_KEY = "shortDescription";
    private final String STEP_INDEX_KEY = "stepIndex";
    private final String RECIPE_ID_KEY = "recipe_Id";
    private final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    private final String RESUME_POSITION_KEY = "resumePosition";
    private final String FRAGMENT_TAG = "myFragment";

    private final int STEP_INDEX_DEFAULT_VALUE = -1;
    private final int RECIPE_ID_DEFAULT_VALUE = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
        }

        setContentView(R.layout.activity_step_detail);

        // only create a new fragment when it is not previously created
        if (savedInstanceState == null) {
            // get the shortDescription from the intent to title the activity
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(SHORT_DESCRIPTION_KEY)) {
                shortDescription = intent.getStringExtra(SHORT_DESCRIPTION_KEY);
                setTitle(shortDescription);
            }

            // get the step index from the intent
            if (intent != null && intent.hasExtra(STEP_INDEX_KEY)) {
                stepIndex = intent.getIntExtra(STEP_INDEX_KEY, STEP_INDEX_DEFAULT_VALUE);
            }

            // get the recipe _id from the intent
            if (intent != null && intent.hasExtra(RECIPE_ID_KEY)) {
                recipe_Id = intent.getIntExtra(RECIPE_ID_KEY, RECIPE_ID_DEFAULT_VALUE);
            }

            // Instantiate the fragment for step details
            stepDetailFragment = new StepDetailFragment();

            // Use the fragment manager and transaction to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Add the step index and recipe _id to the bundle, to pass it to the fragment
            Bundle args = new Bundle();
            args.putInt(STEP_INDEX_KEY, stepIndex);
            args.putInt(RECIPE_ID_KEY, recipe_Id);
            stepDetailFragment.setArguments(args);

            // call the fragment manager to add the fragment
            fragmentManager.beginTransaction()
                    .add(R.id.container, stepDetailFragment, FRAGMENT_TAG)
                    .commit();
        } else {
            shortDescription = savedInstanceState.getString(SHORT_DESCRIPTION_KEY);
            stepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
            recipe_Id = savedInstanceState.getInt(RECIPE_ID_KEY);
        }
    }

    public void prevButtonOnClick(View view){

        // decrement the step index
        stepIndex = stepIndex - 1;

        // create a new fragment to replace the old one
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment newFragment = new StepDetailFragment();

        // Add the new step index and recipe _id to the bundle, to pass it to the new fragment
        Bundle args = new Bundle();
        args.putInt(STEP_INDEX_KEY, stepIndex);
        args.putInt(RECIPE_ID_KEY, recipe_Id);
        newFragment.setArguments(args);
        Log.v(LOG_TAG, "Prev button, stepIndex: " + stepIndex);
        Log.v(LOG_TAG, "Prev button, recipe_Id: " + recipe_Id);

        // call the fragment manager to replace the old fragment with the new one
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, FRAGMENT_TAG)
                .commit();
    }

    public void nextButtonOnClick(View view){

        // increment the step index
        stepIndex = stepIndex + 1;

        // create a new fragment to replace the old one
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment newFragment = new StepDetailFragment();

        // Add the new step index and recipe _id to the bundle, to pass it to the new fragment
        Bundle args = new Bundle();
        args.putInt(STEP_INDEX_KEY, stepIndex);
        args.putInt(RECIPE_ID_KEY, recipe_Id);
        newFragment.setArguments(args);
        Log.v(LOG_TAG, "Next button, stepIndex: " + stepIndex);
        Log.v(LOG_TAG, "Next button, recipe_Id: " + recipe_Id);

        // call the fragment manager to replace the old fragment with the new one
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SHORT_DESCRIPTION_KEY, shortDescription);
        outState.putInt(STEP_INDEX_KEY, stepIndex);
        outState.putInt(RECIPE_ID_KEY, recipe_Id);

        Log.v(LOG_TAG, "onSaveInstanceState, shortDescription: " + shortDescription);
        Log.v(LOG_TAG, "onSaveInstanceState, stepIndex: " + stepIndex);
        Log.v(LOG_TAG, "onSaveInstanceState, recipe_Id: " + recipe_Id);
    }
}

package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Manolo on 10/10/2017.
 */

public class StepDetailActivity extends AppCompatActivity {

    StepDetailFragment stepDetailFragment;
    int stepIndex;
    int recipe_Id;

    private final int STEP_INDEX_DEFAULT_VALUE = -1;
    private final int RECIPE_ID_DEFAULT_VALUE = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        // get the shortDescription from the intent to title the activity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("shortDescription")) {
            String shortDescription = intent.getStringExtra("shortDescription");
            setTitle(shortDescription);
        }

        // get the step index from the intent
        if (intent != null && intent.hasExtra("stepIndex")) {
            stepIndex = intent.getIntExtra("stepIndex", STEP_INDEX_DEFAULT_VALUE);
        }

        // get the recipe _id from the intent
        if (intent != null && intent.hasExtra("recipe_Id")) {
            recipe_Id = intent.getIntExtra("recipe_Id", RECIPE_ID_DEFAULT_VALUE);
        }

        // Instantiate the fragment for step details
        stepDetailFragment = new StepDetailFragment();

        // Use the fragment manager and transaction to add the fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Add the step index and recipe _id to the bundle, to pass it to the fragment
        Bundle args = new Bundle();
        args.putInt("stepIndex", stepIndex);
        args.putInt("recipe_Id", recipe_Id);
        stepDetailFragment.setArguments(args);

        // call the fragment manager to add the fragment
        fragmentManager.beginTransaction()
                .add(R.id.container, stepDetailFragment)
                .commit();

    }

    public void prevButtonOnClick(View view){

        // decrement the step index
        stepIndex = stepIndex - 1;
        FragmentManager fragmentManager = getSupportFragmentManager();

        // create a new fragment to replace the old one
        StepDetailFragment newFragment = new StepDetailFragment();

        // Add the new step index and recipe _id to the bundle, to pass it to the new fragment
        Bundle args = new Bundle();
        args.putInt("stepIndex", stepIndex);
        args.putInt("recipe_Id", recipe_Id);
        newFragment.setArguments(args);

        // call the fragment manager to replace the old fragment with the new one
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();
    }

    public void nextButtonOnClick(View view){

        // increment the step index
        stepIndex = stepIndex + 1;
        FragmentManager fragmentManager = getSupportFragmentManager();

        // create a new fragment to replace the old one
        StepDetailFragment newFragment = new StepDetailFragment();

        // Add the new step index and recipe _id to the bundle, to pass it to the new fragment
        Bundle args = new Bundle();
        args.putInt("stepIndex", stepIndex);
        args.putInt("recipe_Id", recipe_Id);
        newFragment.setArguments(args);

        // call the fragment manager to replace the old fragment with the new one
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();
    }
}

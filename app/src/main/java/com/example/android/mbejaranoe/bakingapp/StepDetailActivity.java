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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("shortDescription")) {
            String shortDescription = intent.getStringExtra("shortDescription");
            setTitle(shortDescription);
        }

        // Instantiate the fragment for step details
        stepDetailFragment = new StepDetailFragment();
        // Use the fragment manager and transaction to add the fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, stepDetailFragment)
                .commit();

    }

    public void prevButtonOnClick(View view){
        //stepDetailFragment.prevButtonOnClick(view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment newFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt("stepIndex", stepDetailFragment.getStepIndex() - 1);
        args.putInt("recipeId", stepDetailFragment.getRecipeId());
        newFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();
    }

    public void nextButtonOnClick(View view){
        //stepDetailFragment.nextButtonOnClick(view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDetailFragment newFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt("stepIndex", stepDetailFragment.getStepIndex() + 1);
        args.putInt("recipeId", stepDetailFragment.getRecipeId());
        newFragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();
    }
}

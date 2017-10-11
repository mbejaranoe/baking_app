package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Manolo on 10/10/2017.
 */

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("shortDescription")) {
            String shortDescription = intent.getStringExtra("shortDescription");
            setTitle(shortDescription);
        }

        // Create the fragment for step details
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        // Use the fragment manager and transaction to add the fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, stepDetailFragment)
                .commit();

    }
}

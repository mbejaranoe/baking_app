package com.example.android.mbejaranoe.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // only create a new fragment when it is not previously created
        if (savedInstanceState == null) {
            // Create the fragment for recipe
            RecipeFragment recipeFragment = new RecipeFragment();
            // Use the fragment manager and transaction to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, recipeFragment)
                    .commit();
        }
    }
}

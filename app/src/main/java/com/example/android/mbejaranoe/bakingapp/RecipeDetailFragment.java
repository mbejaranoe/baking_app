package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;
import com.example.android.mbejaranoe.bakingapp.data.RecipeContract;
import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.mbejaranoe.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Manolo on 09/10/2017.
 * Fragment to show the recipe details
 */

public class RecipeDetailFragment extends Fragment {

    private final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private final String RECIPE_ID_KEY = "recipe_Id";

    public static final String[] DETAIL_RECIPE_PROJECTION = {
            RecipeEntry.COLUMN_INGREDIENTS,
            RecipeEntry.COLUMN_STEPS
    };

    // Member variables
    public static Ingredient[] mIngredients;
    public static Step[] mSteps;
    public static int mRecipe_ID;

    private Parcelable layoutManagerSavedInstanceState;

    private RecyclerView mRecipeDetailRecyclerView;
    private RecipeDetailAdapter mRecipeDetailAdapter;
    private RecyclerView.LayoutManager mRecipeDetailLayoutManager;

    // Constructor
    public RecipeDetailFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the Recipe detail fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        if (savedInstanceState == null) {
            // Get the intent and its extras in order to get the recipe ingredients and steps
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra(RECIPE_ID_KEY)) {
                mRecipe_ID = intent.getIntExtra(RECIPE_ID_KEY, 0);
            }
            Log.v(LOG_TAG, "onCreateView - savedInstanceState NULL");
        } else {
            mRecipe_ID = getArguments().getInt(RECIPE_ID_KEY);
            Log.v(LOG_TAG, "onCreateView - savedInstanceState NOT NULL");
        }

        updateRecipeDetails();

        mRecipeDetailRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_recipe_detail);
        mRecipeDetailLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecipeDetailRecyclerView.setLayoutManager(mRecipeDetailLayoutManager);

        mRecipeDetailAdapter = new RecipeDetailAdapter(mRecipe_ID, mIngredients, mSteps, getContext());
        mRecipeDetailRecyclerView.setAdapter(mRecipeDetailAdapter);
        layoutManagerSavedInstanceState = mRecipeDetailRecyclerView.getLayoutManager().onSaveInstanceState();

        return rootView;
    }

    public void updateRecipeDetails(){

        // Query the Content Provider
        Cursor cursorRecipe;
        String[] projection = DETAIL_RECIPE_PROJECTION;
        String selection = RecipeEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(mRecipe_ID)};
        Log.v(LOG_TAG, "updateRecipeDetails - mRecipe_ID: " + mRecipe_ID);

        cursorRecipe = getContext().getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        cursorRecipe.moveToFirst();

        // Get the ingredients in String format, as it was stored in the Content Provider
        String ingredientsString = cursorRecipe.getString(cursorRecipe.getColumnIndex(RecipeEntry.COLUMN_INGREDIENTS));
        // Parse the String to get the ingredients into an array
        JSONArray ingredientsJsonArray = null;
        try {
            ingredientsJsonArray = new JSONArray(ingredientsString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing string to JSONArray: " + ingredientsString);
        }
        mIngredients = new Ingredient[ingredientsJsonArray.length()];
        for (int i = 0; i < ingredientsJsonArray.length(); i++){
            try {
                mIngredients[i] = new Ingredient(ingredientsJsonArray.getJSONObject(i));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing string to JSONArray: " + ingredientsString);
            }
        }
        Log.v(LOG_TAG, "Retrieved " + mIngredients.length + " ingredients!");

        // Get the steps in String format, as it was stored in the Content Provider
        String stepsString = cursorRecipe.getString(cursorRecipe.getColumnIndex(RecipeEntry.COLUMN_STEPS));
        // Parse the String to get the steps into an array
        JSONArray stepsJsonArray = null;
        try {
            stepsJsonArray = new JSONArray(stepsString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing string to JSONArray: " + stepsString);
        }
        mSteps = new Step[stepsJsonArray.length()];
        for (int i = 0; i < stepsJsonArray.length(); i++){
            try {
                mSteps[i] = new Step(stepsJsonArray.getJSONObject(i));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error parsing string to JSONArray: " + stepsString);
            }
        }
        Log.v(LOG_TAG, "Retrieved " + mSteps.length + " steps!");
    }

    public RecyclerView getRecyclerView(){
        return mRecipeDetailRecyclerView;
    }

    public Parcelable getLayoutManagerSavedInstanceState(){
        return layoutManagerSavedInstanceState;
    }
}

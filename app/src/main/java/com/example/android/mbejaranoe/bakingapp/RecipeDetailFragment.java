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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Manolo on 09/10/2017.
 * Fragment to show the recipe details
 */

public class RecipeDetailFragment extends Fragment {

    private final String LOG_TAG = RecipeDetailFragment.class.getSimpleName();

    private final String RECIPE_ID_KEY = "recipe_Id";
    private final String LAYOUT_MANAGER_STATE_KEY = "layoutManagerSavedInstanceState";

    public static final String[] DETAIL_RECIPE_PROJECTION = {
            RecipeEntry.COLUMN_INGREDIENTS,
            RecipeEntry.COLUMN_STEPS
    };

    // Member variables
    public Ingredient[] mIngredients;
    public Step[] mSteps;
    public int mRecipe_ID;

    private Parcelable layoutManagerSavedInstanceState;

    @BindView(R.id.recyclerview_recipe_detail) RecyclerView mRecipeDetailRecyclerView;
    private RecipeDetailAdapter mRecipeDetailAdapter;
    private RecyclerView.LayoutManager mRecipeDetailLayoutManager;
    private Unbinder unbinder;

    // Constructor
    public RecipeDetailFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        setRetainInstance(true);

        Log.v(LOG_TAG, "onCreate - setRetainInstance: true");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the Recipe detail fragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600){ // tablet mode
            Log.v(LOG_TAG, "onCreateView - tablet mode");
            if (savedInstanceState == null) {
                mRecipe_ID = getArguments().getInt(RECIPE_ID_KEY);
                Log.v(LOG_TAG, "onCreateView - savedInstanceState: " + savedInstanceState);
            } else {
                mRecipe_ID = savedInstanceState.getInt(RECIPE_ID_KEY);
                mRecipeDetailLayoutManager
                        .onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
                Log.v(LOG_TAG, "onCreateView - savedInstanceState : NOT NULL");
            }
        } else { // phone mode
            Log.v(LOG_TAG, "onCreateView - phone mode");
            if (savedInstanceState == null) {
                Intent intent = getActivity().getIntent();
                mRecipe_ID = intent.getExtras().getInt(RECIPE_ID_KEY);
                Log.v(LOG_TAG, "onCreateView - savedInstanceState: " + savedInstanceState);
            } else {
                mRecipe_ID = savedInstanceState.getInt(RECIPE_ID_KEY);
                mRecipeDetailLayoutManager
                        .onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE_KEY));
                Log.v(LOG_TAG, "onCreateView - savedInstanceState : NOT NULL");
            }
        }

        updateRecipeDetails();

        mRecipeDetailLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecipeDetailRecyclerView.setLayoutManager(mRecipeDetailLayoutManager);

        mRecipeDetailAdapter = new RecipeDetailAdapter(mRecipe_ID, mIngredients, mSteps, getContext());
        mRecipeDetailRecyclerView.setAdapter(mRecipeDetailAdapter);
        layoutManagerSavedInstanceState = mRecipeDetailRecyclerView.getLayoutManager().onSaveInstanceState();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateRecipeDetails(){

        // Query the Content Provider
        Cursor cursorRecipe;
        String[] projection = DETAIL_RECIPE_PROJECTION;
        String selection = RecipeEntry.COLUMN_RECIPE_ID + "=?";
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(RECIPE_ID_KEY, mRecipe_ID);
        outState.putParcelable(LAYOUT_MANAGER_STATE_KEY, layoutManagerSavedInstanceState);
    }
}

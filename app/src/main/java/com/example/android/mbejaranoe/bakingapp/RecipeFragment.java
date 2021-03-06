package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Manolo on 29/09/2017.
 * Fragment to show the recipes
 */

public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.recyclerview_recipe) RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private RecyclerView.LayoutManager mRecipeLayoutManager;
    private Unbinder unbinder;

    private static final int RECIPE_LOADER_ID = 22;

    private static final String BUNDLE_RECYCLER_LAYOUT = "RecipeFragment.recycler.layout";

    public static final String[] FRAGMENT_RECIPE_PROJECTION = {RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,
            RecipeContract.RecipeEntry.COLUMN_NAME, RecipeContract.RecipeEntry.COLUMN_SERVINGS,
            RecipeContract.RecipeEntry.COLUMN_IMAGE_URL};

    //Constructor for instantiating the fragment
    public RecipeFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(LOG_TAG, "onCreate");

        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (getContext().getContentResolver() != null) {
                new FetchRecipeTask(getContext()).execute();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.offline_status_recipe_info_message), Toast.LENGTH_LONG).show();
        }
        updateRecipes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the Recipe fragment layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            Log.v(LOG_TAG, "onCreateView - smallestScreenWidthDp >= 600 - tablet");
            mRecipeLayoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            Log.v(LOG_TAG, "onCreateView - smallestScreenWidthDp < 600 - phone");
            mRecipeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }
        mRecipeRecyclerView.setLayoutManager(mRecipeLayoutManager);

        mRecipeAdapter = new RecipeAdapter();
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        return rootView;
    }

    private void updateRecipes(){
        int loaderId = RECIPE_LOADER_ID;

        LoaderManager loaderManager = this.getLoaderManager();
        LoaderManager.LoaderCallbacks<Cursor> callback = this;
        loaderManager.initLoader(loaderId, null, callback);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRecipes();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoaderManager loaderManager = this.getLoaderManager();
        loaderManager.restartLoader(RECIPE_LOADER_ID, null, this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecipeRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecipeRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId){
            case RECIPE_LOADER_ID:
                Uri recipeQueryUri = RecipeContract.RecipeEntry.CONTENT_URI;
                return new CursorLoader(getContext(),
                        recipeQueryUri,
                        FRAGMENT_RECIPE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mRecipeAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipeAdapter.swapCursor(null);
    }
}

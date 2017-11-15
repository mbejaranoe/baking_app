package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;
import com.example.android.mbejaranoe.bakingapp.data.RecipeContract;
import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Manolo on 13/11/2017.
 */

public class RecipeWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private final String LOG_TAG = RecipeWidgetDataProvider.class.getSimpleName();

    private final String RECIPE_ID_KEY = "recipe_Id";

    private Ingredient[] ingredients;
    private int recipe_Id;
    private Context context;
    private Intent intent;

    private void getRecipeData() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        recipe_Id = sharedPreferences.getInt(RECIPE_ID_KEY, -1);
        Log.v(LOG_TAG, "recipe_Id: " + recipe_Id);

        if (recipe_Id != -1) {
            // Query the Content Provider
            Cursor cursorRecipe;
            String[] projection = {RecipeEntry.COLUMN_INGREDIENTS};
            String selection = RecipeEntry.COLUMN_RECIPE_ID + "=?";
            String[] selectionArgs = new String[]{String.valueOf(recipe_Id)};
            Log.v(LOG_TAG, "getIngredients - recipe_Id: " + recipe_Id);

            cursorRecipe = context.getContentResolver().query(RecipeContract.RecipeEntry.CONTENT_URI,
                    projection, // projection,
                    selection,  // selection,
                    selectionArgs, // selectionArgs,
                    null);

            if (cursorRecipe.moveToFirst()) {
                // Get the ingredients in String format, as it was stored in the Content Provider
                String ingredientsString = cursorRecipe.getString(cursorRecipe.getColumnIndex(RecipeEntry.COLUMN_INGREDIENTS));
                // Parse the String to get the ingredients into an array
                JSONArray ingredientsJsonArray = null;
                try {
                    ingredientsJsonArray = new JSONArray(ingredientsString);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing string to JSONArray: " + ingredientsString);
                }
                ingredients = new Ingredient[ingredientsJsonArray.length()];
                for (int i = 0; i < ingredientsJsonArray.length(); i++) {
                    try {
                        ingredients[i] = new Ingredient(ingredientsJsonArray.getJSONObject(i));
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error parsing string to JSONArray: " + ingredientsString);
                    }
                }
                Log.v(LOG_TAG, "Retrieved " + ingredients.length + " ingredients!");
            }
            cursorRecipe.close();
        }
    }

    public RecipeWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        // getRecipeData();
    }

    @Override
    public void onDataSetChanged() {
        getRecipeData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return (ingredients != null) ? ingredients.length : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // here we generate the views for our widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.list_item_widget);

        getRecipeData();

        remoteViews.setTextViewText(R.id.widget_ingredient_name_text_view,
                ingredients[position].getIngredient());
        remoteViews.setTextViewText(R.id.widget_quantity_text_view,
                String.valueOf(ingredients[position].getQuantity()));
        remoteViews.setTextViewText(R.id.widget_measure_text_view,
                ingredients[position].getMeasure());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

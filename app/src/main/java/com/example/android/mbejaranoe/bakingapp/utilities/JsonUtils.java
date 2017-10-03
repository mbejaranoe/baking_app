package com.example.android.mbejaranoe.bakingapp.utilities;

import android.content.ContentValues;
import android.util.Log;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;
import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.mbejaranoe.bakingapp.data.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manolo on 03/10/2017.
 * utilities method for handling Json objects
 */

public final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * Take the String representing the recipes in JSON Format and
     * pull out the data we need
     */
    public static ContentValues[] getRecipeDataFromJson(String recipeJsonStr) throws JSONException {

        /* Key values for recipes in Json format */
        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        /* Key values for ingredients in Json format */
        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASURE = "measure";
        final String INGREDIENT_NAME = "ingredient";

        /* Key values for steps in Json format */
        final String STEP_ID = "id";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";

        JSONArray recipesArray = new JSONArray(recipeJsonStr);
        ContentValues[] recipes = new ContentValues[recipesArray.length()];

        for (int i = 0; i < recipesArray.length(); i++){
            recipes[i] = new ContentValues();

            recipes[i].put(RecipeEntry.COLUMN_RECIPE_ID, recipesArray.getJSONObject(i).getInt(RECIPE_ID));
            Log.v(TAG,RecipeEntry.COLUMN_RECIPE_ID + ": " + recipesArray.getJSONObject(i).getInt(RECIPE_ID));

            recipes[i].put(RecipeEntry.COLUMN_NAME, recipesArray.getJSONObject(i).getString(RECIPE_NAME));
            Log.v(TAG,RecipeEntry.COLUMN_NAME + ": " + recipesArray.getJSONObject(i).getString(RECIPE_NAME));

            /* Código para obtener los ingredientes */
            JSONArray ingredientsArray = recipesArray.getJSONObject(i).getJSONArray(RECIPE_INGREDIENTS);
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (int j = 0; j < ingredientsArray.length() ; j++) {
                JSONObject ingredientJson = ingredientsArray.getJSONObject(j);
                ingredients.add(new Ingredient(ingredientJson));
            }
            String ingredientsString = "";
            try {
                ingredientsString = JsonUtils.ingredientsToString(ingredients);
            } catch (JSONException e) {
                Log.e(TAG, "Error en ingredientsToString");
            }
            recipes[i].put(RecipeEntry.COLUMN_INGREDIENTS, ingredientsString);
            Log.v(TAG,RecipeEntry.COLUMN_INGREDIENTS + ": " + ingredientsString);

            /* Código para obtener los pasos */
            JSONArray stepsArray = recipesArray.getJSONObject(i).getJSONArray(RECIPE_STEPS);
            ArrayList<Step> steps = new ArrayList<>();
            for (int k = 0; k < stepsArray.length() ; k++) {
                JSONObject stepJson = stepsArray.getJSONObject(k);
                steps.add(new Step(stepJson));
            }
            String stepsString = "";
            try {
                stepsString = JsonUtils.stepsToString(steps);
            } catch (JSONException e) {
                Log.e(TAG, "Error en stepsToString");
            }
            recipes[i].put(RecipeEntry.COLUMN_STEPS, stepsString);
            Log.v(TAG,RecipeEntry.COLUMN_STEPS + ": " + stepsString);

            recipes[i].put(RecipeEntry.COLUMN_SERVINGS, recipesArray.getJSONObject(i).getInt(RECIPE_SERVINGS));
            Log.v(TAG,RecipeEntry.COLUMN_SERVINGS + ": " + recipesArray.getJSONObject(i).getInt(RECIPE_SERVINGS));

            String imageURL = recipesArray.getJSONObject(i).getString(RECIPE_IMAGE);
            byte[] image = new byte[0];
            if (imageURL != null && !(imageURL.equals(""))) {
                image = NetworkUtils.getImageFromURL(imageURL);
            }
            recipes[i].put(RecipeEntry.COLUMN_IMAGE, image);
            Log.v(TAG,RecipeEntry.COLUMN_IMAGE + ": " + imageURL);
        }

        return recipes;
    }

    public static String ingredientsToString(ArrayList<Ingredient> ingredientsArray) throws JSONException {
        String arrayList;
        JSONObject json = new JSONObject();
        json.put("ingredients", new JSONArray(ingredientsArray));
        arrayList = json.toString();

        return arrayList;
    }

    public static String stepsToString(ArrayList<Step> stepsArray) throws JSONException{
        String arrayList;
        JSONObject json = new JSONObject();
        json.put("steps", new JSONArray(stepsArray));
        arrayList = json.toString();

        return arrayList;
    }
}

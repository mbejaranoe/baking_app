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
 * Utility methods for handling Json objects
 */

public final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     * Take the String representing the recipes in JSON Format and
     * pull out the data we need
     */
    public static ContentValues[] getRecipeDataFromJson(String recipeJsonStr) throws JSONException {

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE_URL = "image";

        JSONArray recipesArray = new JSONArray(recipeJsonStr);
        ContentValues[] recipes = new ContentValues[recipesArray.length()];

        for (int i = 0; i < recipesArray.length(); i++){
            recipes[i] = new ContentValues();

            recipes[i].put(RecipeEntry.COLUMN_RECIPE_ID, recipesArray.getJSONObject(i).getInt(RECIPE_ID));
            Log.v(TAG,RecipeEntry.COLUMN_RECIPE_ID + ": " + recipesArray.getJSONObject(i).getInt(RECIPE_ID));

            recipes[i].put(RecipeEntry.COLUMN_NAME, recipesArray.getJSONObject(i).getString(RECIPE_NAME));
            Log.v(TAG,RecipeEntry.COLUMN_NAME + ": " + recipesArray.getJSONObject(i).getString(RECIPE_NAME));

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

            String imageURL = recipesArray.getJSONObject(i).getString(RECIPE_IMAGE_URL);
            recipes[i].put(RecipeEntry.COLUMN_IMAGE_URL, imageURL);
            Log.v(TAG,RecipeEntry.COLUMN_IMAGE_URL + ": " + imageURL);
        }

        return recipes;
    }

    /* Takes an ArrayList<Ingredient> and returns a String in order to store it in the db*/
    public static String ingredientsToString(ArrayList<Ingredient> ingredientsArray) throws JSONException {
        String arrayList;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i <ingredientsArray.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("quantity", ingredientsArray.get(i).getQuantity());
            jsonObject.put("measure", ingredientsArray.get(i).getMeasure());
            jsonObject.put("ingredient", ingredientsArray.get(i).getIngredient());
            jsonArray.put(jsonObject);
        }
        arrayList = jsonArray.toString();

        return arrayList;
    }

    /* Takes an ArrayList<Step> and returns a String in order to store it in the db*/
    public static String stepsToString(ArrayList<Step> stepsArray) throws JSONException{
        String arrayList;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i <stepsArray.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", stepsArray.get(i).getId());
            jsonObject.put("shortDescription", stepsArray.get(i).getShortDescription());
            jsonObject.put("description", stepsArray.get(i).getDescription());
            jsonObject.put("videoURL", stepsArray.get(i).getVideoURL());
            jsonObject.put("thumbnailURL", stepsArray.get(i).getThumbnailURL());
            jsonArray.put(jsonObject);
        }
        arrayList = jsonArray.toString();

        return arrayList;
    }
}

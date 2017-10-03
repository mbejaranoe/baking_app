package com.example.android.mbejaranoe.bakingapp.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Manolo on 02/10/2017.
 * Class to store ingredients data
 */

public class Ingredient {

    private final String LOG_TAG = Ingredient.class.getSimpleName();

    /* Member variable storing the quantity */
    long mQuantity;

    /* Member variable storing the measure */
    String mMeasure;

    /* Member variable storing the name */
    String mIngredient;

    /* Constructors */
    public Ingredient(){
        mQuantity = 0;
        mMeasure = null;
        mIngredient = null;
    }

    public Ingredient(JSONObject jsonObject){
        try {
            mQuantity = jsonObject.getLong("quantity");
            mMeasure = jsonObject.getString("measure");
            mIngredient = jsonObject.getString("ingredient");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid Json object");
        }
    }

    /* Helper method for handling Ingredient objects */
    public JSONObject getJsonFromIngredient(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("quantity", mQuantity);
            jsonObject.put("measure", mMeasure);
            jsonObject.put("ingredient", mIngredient);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid Json object");
        }

        return  jsonObject;
    }

    /* Setter methods */
    public void setQuantity(long quantity){
        mQuantity = quantity;
    }

    public void setMeasure(String measure){
        mMeasure = measure;
    }

    public void setIngredient(String ingredient){
        mIngredient = ingredient;
    }

    /* Getter methods */
    public long getQuantity(){
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredient(){
        return mIngredient;
    }
}

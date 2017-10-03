package com.example.android.mbejaranoe.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.mbejaranoe.bakingapp.utilities.JsonUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Manolo on 29/09/2017.
 */

public class FetchRecipeTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchRecipeTask.class.getSimpleName();

    private final Context mContext;

    public FetchRecipeTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... strings) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String recipeJsonStr = null;

        try {
            final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            URL url = new URL(RECIPES_URL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            recipeJsonStr = buffer.toString();
            ContentValues[] recipes = JsonUtils.getRecipeDataFromJson(recipeJsonStr);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error ", e);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}

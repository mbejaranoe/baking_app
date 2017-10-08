package com.example.android.mbejaranoe.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Manolo on 02/10/2017.
 * Defines table and column names for the recipes database.
 */

public class RecipeContract {

    // Helping constants for RecipeContentProvider
    public static final String AUTHORITY = "com.example.android.mbejaranoe.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipes";

    /* Inner class that defines the table contents of the recipes table */
    public static final class RecipeEntry implements BaseColumns {

        // Helping constants for RecipeContentProvider
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        /* Column with the id for the recipe_id in the JSon object, stored as an integer */
        /* Note it is different than the _ID column when using DBHelper */
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        /* Column with the name of the recipe, stored as a String */
        public static final String COLUMN_NAME = "name";

        /* Column with the list of ingredients, stored as an ArrayList<Ingredient> */
        public static final String COLUMN_INGREDIENTS = "ingredients";

        /* Column with the list of steps, stored as an ArrayList<Step> */
        public static final String COLUMN_STEPS = "steps";

        /* Column with the servings, stored as an integer */
        public static final String COLUMN_SERVINGS = "servings";

        /* Column with the image URL for the recipe, stored as a String */
        public static final String COLUMN_IMAGE_URL = "image_url";
    }

    /* Inner class that defines the table contents of the ingredients table */
    public static final class IngredientEntry implements BaseColumns {

        public static final String TABLE_NAME = "ingredients";

        /* Column with the quantity of the ingredient, stored as an integer */
        public static final String COLUMN_QUANTITY = "quantity";

        /* Column with the measure, stored as a String */
        public static final String COLUMN_MEASURE = "measure";

        /* Column with the name of the ingredient, stored as a String */
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

    /* Inner class that defines the table contents of the ingredients table */
    public static final class StepEntry implements BaseColumns {

        public static final String TABLE_NAME = "steps";

        /* Column with the id of the step, stored as an integer */
        public static final String COLUMN_STEP_ID = "step_id";

        /* Column with the short description, stored as a String */
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";

        /* Column with the description, stored as a String */
        public static final String COLUMN_DESCRIPTION = "description";

        /* Column with the video URL, stored as a String */
        public static final String COLUMN_VIDEO_URL = "video_url";

        /* Column with the thumbnail URL, stored as a String */
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    }


}

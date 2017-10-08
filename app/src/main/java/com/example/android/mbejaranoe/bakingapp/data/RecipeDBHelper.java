package com.example.android.mbejaranoe.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;



/**
 * Created by Manolo on 02/10/2017.
 */

public class RecipeDBHelper extends SQLiteOpenHelper {

    /* String representing the name of the database */
    private static final String DATABASE_NAME = "recipes.db";

    /* Database version for updating database schema */
    private static final int DATABASE_VERSION = 2;

    /* Constructor */
    public RecipeDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /* String containing que SQL sentence for creating the table */
        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " +
                RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_STEPS + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_IMAGE_URL + " TEXT," +
                "UNIQUE (" + RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE);";

        /* Call to execute the creating table sentence */
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

package com.example.android.mbejaranoe.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;

/**
 * Created by Manolo on 04/10/2017.
 * Implementation of a ContentProvider for recipes
 */

public class RecipeContentProvider extends ContentProvider {

    private RecipeDBHelper mRecipeDBHelper;

    // Constants for the UriMatcher
    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Helper function to build a UriMatcher
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES + "/#", RECIPES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeDBHelper = new RecipeDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mRecipeDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match){
            case RECIPES:
                retCursor = db.query(RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "recipe_id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(RecipeEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mRecipeDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsInserted;
        long _id;
        String tableName;

        switch (match){
            case (RECIPES):
                tableName = RecipeEntry.TABLE_NAME;
                break;
            case (RECIPES_WITH_ID):
                tableName = RecipeEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        db.beginTransaction();
        rowsInserted = 0;

        try {
            for (ContentValues value : values) {
                _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (rowsInserted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case RECIPES:
                return "vnd.android.cursor.dir" + "/" + RecipeContract.AUTHORITY + "/"
                        + RecipeContract.PATH_RECIPES;
            case RECIPES_WITH_ID:
                return "vnd.android.cursor.item" + "/" + RecipeContract.AUTHORITY + "/"
                        + RecipeContract.PATH_RECIPES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mRecipeDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPES:
                long id = db.insert(RecipeEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed: " + id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    // This method is not necessary for the purpose of the Content Provider
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    // This method is not necessary for the purpose of the Content Provider
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

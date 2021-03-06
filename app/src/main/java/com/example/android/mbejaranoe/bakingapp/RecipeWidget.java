package com.example.android.mbejaranoe.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */

public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Set up the intent that starts the RecipeWidgetService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, RecipeWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects
            // to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            views.setRemoteAdapter(R.id.widget_list, intent);

            // Set the text for the widget header
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String recipeName = sharedPreferences.getString("name", "none");
            int recipe_Id = sharedPreferences.getInt("recipe_Id", -1);
            Log.v(RecipeWidget.class.getSimpleName(), "recipeName: " + recipeName);
            if (recipeName.equals("none") || recipe_Id == -1) {
                views.setTextViewText(R.id.widget_header_text_view,
                        context.getResources().getString(R.string.widget_message_no_recipe_selected));
            } else {
                views.setTextViewText(R.id.widget_header_text_view, recipeName);
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            ComponentName widget = new ComponentName(context, RecipeWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = appWidgetManager.getAppWidgetIds(widget);
            onUpdate(context, appWidgetManager, ids);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


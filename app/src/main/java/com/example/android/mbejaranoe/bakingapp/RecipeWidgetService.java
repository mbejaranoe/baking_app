package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Manolo on 13/11/2017.
 */

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // return remote view factory here
        return new RecipeWidgetDataProvider(this, intent);
    }

}

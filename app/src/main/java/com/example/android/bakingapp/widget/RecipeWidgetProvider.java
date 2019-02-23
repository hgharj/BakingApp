package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.RecipeUtils;

import java.util.ArrayList;

public class RecipeWidgetProvider extends AppWidgetProvider {
    SharedPreferences mSharedPreferences;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
                         String recipeName, String ingredients) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipe_name_tv, recipeName);
        views.setTextViewText(R.id.ingredients_tv, ingredients);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
        mSharedPreferences = context.getSharedPreferences(RecipeUtils.getPrefKey(),
                Context.MODE_PRIVATE);

        String recipeName = mSharedPreferences.getString(RecipeUtils.getPrefRecipeNameKey(),null);
        String ingredients = mSharedPreferences.getString(RecipeUtils.getPrefIngredientsKey(),null);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, ingredients);
        }
    }
}

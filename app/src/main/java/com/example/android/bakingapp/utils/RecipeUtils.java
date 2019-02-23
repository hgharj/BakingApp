package com.example.android.bakingapp.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import java.util.ArrayList;

public class RecipeUtils {
    private static final String CARRIAGE_RETURN = "\n";
    private static final String PREF_KEY = "shared-pref";
    private static final String PREF_RECIPE_NAME_KEY = "pref-recipe-name";
    private static final String PREF_INGREDIENTS_KEY = "pref-recipe-ingredients";
    public static final String VIDEO_EXT = ".mp4";

    public static String formatIngredients(ArrayList<Ingredient> ingredients) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (ingredients != null && ingredients.size() > 0) {
            for (Ingredient ingredient : ingredients) {
                ssb.append(ingredient.getIngredient().toLowerCase());
                int start = ssb.length();
                ssb.append(" (");
                ssb.append(ingredient.getQuantity().toString());
                ssb.append(" ");
                ssb.append(ingredient.getMeasure().toLowerCase());
                ssb.append(")");
                ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(CARRIAGE_RETURN);
                ssb.append(CARRIAGE_RETURN);
            }
        }

        return ssb.toString();
    }

    public static String getPrefKey() {
        return PREF_KEY;
    }

    public static String getPrefRecipeNameKey() {
        return PREF_RECIPE_NAME_KEY;
    }

    public static String getPrefIngredientsKey() {
        return PREF_INGREDIENTS_KEY;
    }

    public static String getVideoUrl(Step step) {
        String videoUrl = null;
        if (!step.getVideoUrl().equals("")) {
            videoUrl = step.getVideoUrl();
        } else if (!step.getThumbnailUrl().equals("") && step.getThumbnailUrl().contains(VIDEO_EXT)) {
            videoUrl = step.getThumbnailUrl();
        }
        return videoUrl;
    }

    public static String formatDescription(Step step) {
        String description = step.getDescription();
        String replaceString;
        if (step.getDescription().indexOf(".") > 0) {
            replaceString = step.getDescription().substring(0, step.getDescription().indexOf(".") + 1);
            description = step.getDescription().replace(replaceString, "").trim();
        }
        return description;
    }
}

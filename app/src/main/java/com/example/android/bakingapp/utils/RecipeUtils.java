package com.example.android.bakingapp.utils;

import android.os.Bundle;
import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeUtils {
    private static final String CARRIAGE_RETURN="\\n";
    private static final String PREF_KEY="shared-pref";
    private static final String PREF_RECIPE_NAME_KEY="pref-recipe-name";
    private static final String PREF_INGREDIENTS_KEY="pref-recipe-ingredients";
    public static final String VIDEO_EXT=".mp4";

    public static String formatIngredients(ArrayList<Ingredient> ingredients){
        StringBuilder sb = new StringBuilder();
        if (ingredients != null && ingredients.size()>0) {
            for (Ingredient ingredient : ingredients) {
                sb.append(ingredient.getIngredient().toLowerCase());
                sb.append("     ");
                sb.append(ingredient.getQuantity().toString());
                sb.append(" ");
                sb.append(ingredient.getMeasure().toLowerCase());
                sb.append(CARRIAGE_RETURN);
            }
        }
        return sb.toString();
    }

    public static String getPrefKey(){
        return PREF_KEY;
    }

    public static String getPrefRecipeNameKey(){
        return PREF_RECIPE_NAME_KEY;
    }

    public static String getPrefIngredientsKey(){
        return PREF_INGREDIENTS_KEY;
    }

    public static String getVideoUrl(Step step){
        String videoUrl=null;
        if(!step.getVideoUrl().equals("")){
            videoUrl=step.getVideoUrl();
        } else if(!step.getThumbnailUrl().equals("") && step.getThumbnailUrl().contains(VIDEO_EXT)){
            videoUrl=step.getThumbnailUrl();
        }
        return videoUrl;
    }

    public static String formatDescription(Step step){
        String description = step.getDescription();
        String replaceString;
        if(step.getDescription().indexOf(".")>0) {
            replaceString = step.getDescription().substring(0, step.getDescription().indexOf(".") + 1);
            description = step.getDescription().replace(replaceString, "").trim();
        }
        return description;
    }
}

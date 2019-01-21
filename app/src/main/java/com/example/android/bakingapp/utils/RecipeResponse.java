package com.example.android.bakingapp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jcgray on 1/17/19.
 */

public class RecipeResponse {
    @SerializedName("results")
    @Expose
    private List<Recipe> recipeList;

    public List<Recipe> getRecipeList() {
        return recipeList;
    }
}

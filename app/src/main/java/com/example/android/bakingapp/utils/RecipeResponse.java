package com.example.android.bakingapp.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jcgray on 1/17/19.
 */

public class RecipeResponse {
    @SerializedName("")
    @Expose
    private List<Recipe> recipeList;

    private Recipe recipe;

    public Recipe getRecipe(){
        return recipe;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

//    class List<Recipe> {
//        public List<Recipe> recipes;
//
//        public List<Recipe> getRecipes(){
//            return recipes;
//        }
//    }
}

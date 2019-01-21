package com.example.android.bakingapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private List<Recipe> mRecipeList;
    private static final String LOG_TAG = "Controller";

    public List<Recipe> getRecipeList() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RecipeDbAPI recipeDbAPI = retrofit.create(RecipeDbAPI.class);

        Call<RecipeResponse> call;
        call = recipeDbAPI.getRecipes();

        try {
            Response<RecipeResponse> response = call.execute();
            if (response.errorBody() == null) {
                mRecipeList = response.body().getRecipeList();
            } else {
                Log.v(LOG_TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mRecipeList;
    }
}


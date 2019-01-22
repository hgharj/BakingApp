package com.example.android.bakingapp.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeDbAPI {
    @GET("baking.json")
//    Call<List<RecipeResponse>> getRecipes();
    Call<List<Recipe>> getRecipes();

//    @GET("/3/movie/popular")
//    Call<RecipeResponse> getMostPopularMovies(@Query("sort_by") String sortBy, @Query("api_key") String key);
//
//    @GET("/3/movie/{id}/videos")
//    Call<TrailerResponse> getRelatedVideos(@Path("id") Long id, @Query("api_key") String key);
//
//    @GET("/3/movie/{id}/reviews")
//    Call<UserReviewResponse> getUserReviews(@Path("id") Long id, @Query("api_key") String key);
//
//    @GET("/3/movie/{id}")
//    Call<RecipeResponse> getMovie(@Path("id") String id, @Query("api_key") String key);
}



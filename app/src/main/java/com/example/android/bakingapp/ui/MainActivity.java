package com.example.android.bakingapp.ui;

//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MasterRecipeFragment.OnDataPass{
    private static final String INGREDIENT_DATA = "pass-ingredients";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onDataPass(Recipe recipe) {
        Intent passIngredients = new Intent(this, RecipeDetailActivity.class);
        passIngredients.putExtra(INGREDIENT_DATA,recipe.getIngredients());
        startActivity(passIngredients);
    }
}

package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String INGREDIENT_DATA = "pass-ingredients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent data = getIntent();
        ArrayList<Ingredient> ingredients = data.getParcelableArrayListExtra(INGREDIENT_DATA);
    }
}

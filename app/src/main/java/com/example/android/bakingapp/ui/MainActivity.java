package com.example.android.bakingapp.ui;

//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MasterRecipeFragment.OnDataPass, MasterRecipeFragment.OnRecipesPassed{
    private static final String INGREDIENT_DATA = "pass-ingredients";
    private static final String STEP_LIST_DATA = "pass-step-list";
    private static final String STEP_LIST_FRAGMENT = "step-list-fragment";
    private StepListFragment mStepListFragment;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private boolean mTwoPane;
    ArrayList<Recipe> mRecipes;
    ArrayList<Ingredient> mIngredients;
    ArrayList<Step> mSteps;
    Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            mTwoPane = true;

            mIngredients = mRecipe.getIngredients();
            mSteps = mRecipe.getSteps();

            FragmentManager fragmentManager = getSupportFragmentManager();
            IngredientsMiniFragment ingredientsMiniFragment = new IngredientsMiniFragment();
            Bundle bundleIngredients = new Bundle();
            bundleIngredients.putParcelableArrayList(INGREDIENT_DATA,mIngredients);
            ingredientsMiniFragment.setArguments(bundleIngredients);
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_container, ingredientsMiniFragment)
                    .commit();

            if(savedInstanceState!=null){
                mStepListFragment = (StepListFragment) getSupportFragmentManager().getFragment(savedInstanceState,STEP_LIST_FRAGMENT);
            } else {
                mStepListFragment = new StepListFragment();
                Bundle bundleSteps = new Bundle();
                bundleSteps.putParcelableArrayList(STEP_LIST_DATA, mSteps);
                mStepListFragment.setArguments(bundleSteps);

            }
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_rv, mStepListFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onDataPass(Recipe recipe) {
        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
                mRecipe = recipe;
        } else {
            Intent passData = new Intent(this, RecipeDetailActivity.class);
            passData.putExtra(INGREDIENT_DATA, recipe.getIngredients());
            passData.putExtra(STEP_LIST_DATA, recipe.getSteps());
            startActivity(passData);
        }
    }

    @Override
    public void onRecipesPassed(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
    }

    public NetworkInfo checkNetworkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        return connMgr.getActiveNetworkInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, STEP_LIST_FRAGMENT, mStepListFragment);
    }
}

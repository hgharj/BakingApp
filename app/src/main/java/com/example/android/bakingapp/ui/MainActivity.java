package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MasterRecipeFragment.OnDataPass{
    private static final String INGREDIENT_DATA = "pass-ingredients-as-string";
    private static final String STEP_LIST_DATA = "pass-step-list";
    private static final String RECIPE_TITLE = "recipe-title";
    @BindView(R.id.empty_view)
    TextView mEmptyTextView;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(checkNetworkConnection()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MasterRecipeFragment masterFragment = new MasterRecipeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_list_container, masterFragment)
                    .commit();

            mEmptyTextView.setVisibility(View.GONE);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public void onDataPass(Recipe recipe) {
        String ingredients = RecipeUtils.formatIngredients(recipe.getIngredients());
        SharedPreferences sharedPref = getSharedPreferences(RecipeUtils.getPrefKey(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_recipe_name_key), recipe.getName());
        editor.putString(getString(R.string.pref_recipe_ingredients_key),ingredients);
        editor.apply();

        Intent passData = new Intent(this, RecipeDetailActivity.class);
        passData.putExtra(INGREDIENT_DATA, ingredients);
        passData.putExtra(STEP_LIST_DATA, recipe.getSteps());
        passData.putExtra(RECIPE_TITLE, recipe.getName());
        startActivity(passData);
    }

    public boolean checkNetworkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        return connMgr.getActiveNetworkInfo().isConnected();
    }
}

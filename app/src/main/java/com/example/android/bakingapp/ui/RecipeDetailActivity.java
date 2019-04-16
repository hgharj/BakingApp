package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepListFragment.OnDataPass{
    private static final String INGREDIENT_DATA = "pass-ingredients-as-string";
    private static final String INGREDIENT_LIST_DATA = "pass-ingredients-as-arraylist";
    private static final String STEP_LIST_DATA = "pass-step-list";
    private static final String STEP_LIST_FRAGMENT = "step-list-fragment";
    private static final String INGREDIENTS_FRAGMENT = "ingredients-fragment";
    private static final String LOG_TAG = RecipeDetailActivity.class.getName();
    private StepListFragment mStepListFragment;
    private IngredientsFragment mIngredientsFragment;
    private boolean mTwoPane;
    private Toolbar mToolbar;
    private static final String STEP_DATA="pass-step";
    private static final String STEP_POSITION ="pass-step-position";
    private static final String RECIPE_TITLE = "recipe-title";
    @BindView(R.id.pager)
    ViewPager mPager;
    PagerAdapter mPagerAdapter;
    ArrayList<Step> mSteps;
    int mStepPosition=-1;
    String mIngredients;
    String mRecipeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(findViewById(R.id.pager) != null) {
            mTwoPane = true;
//            setContentView(R.layout.recipe_detail_list_600dp);
            ButterKnife.bind(this);
            mToolbar = findViewById(R.id.toolbar);
            mPagerAdapter = new RecipeDetailActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mStepPosition);
        } else {
            mTwoPane = false;
            setContentView(R.layout.recipe_detail_list);
            mToolbar = findViewById(R.id.toolbar);
        }

        Intent data = getIntent();
        mIngredients = data.getStringExtra(INGREDIENT_DATA);
        mSteps = data.getParcelableArrayListExtra(STEP_LIST_DATA);
        mRecipeTitle = data.getStringExtra(RECIPE_TITLE);

        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(mRecipeTitle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mIngredientsFragment = new IngredientsFragment();
        Bundle bundleIngredients = new Bundle();

        if (savedInstanceState != null) {
            mStepListFragment = (StepListFragment) getSupportFragmentManager().getFragment(savedInstanceState, STEP_LIST_FRAGMENT);
            mIngredientsFragment = (IngredientsFragment) getSupportFragmentManager().getFragment(savedInstanceState, INGREDIENTS_FRAGMENT);
            mSteps = savedInstanceState.getParcelableArrayList(STEP_DATA);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
            mIngredients = savedInstanceState.getString(INGREDIENT_DATA);
            mRecipeTitle = savedInstanceState.getString(RECIPE_TITLE);
        } else {
            bundleIngredients.putString(INGREDIENT_DATA, mIngredients);
            mIngredientsFragment.setArguments(bundleIngredients);

            mStepListFragment = new StepListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(STEP_LIST_DATA, mSteps);
            bundle.putString(INGREDIENT_DATA, mIngredients);
            bundle.putString(RECIPE_TITLE,mRecipeTitle);
            mStepListFragment.setArguments(bundle);

            mStepPosition = 0;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.ingredients_container, mIngredientsFragment)
//                .commit();
//
//        fragmentManager.beginTransaction()
                .replace(R.id.steps_rv, mStepListFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, STEP_LIST_FRAGMENT, mStepListFragment);
        getSupportFragmentManager().putFragment(outState, INGREDIENTS_FRAGMENT, mIngredientsFragment);

        if(mStepPosition>-1){
            outState.putInt(STEP_POSITION,mStepPosition);
        }

        if(mSteps!=null){
            outState.putParcelableArrayList(STEP_DATA,mSteps);
        }
        outState.putString(INGREDIENT_DATA,mIngredients);
        outState.putString(RECIPE_TITLE,mRecipeTitle);
    }

    @Override
    public void onDataPass(ArrayList<Step> steps, int position) {
        mSteps = steps;
        mStepPosition = position;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(getResources().getConfiguration().orientation ==
//                Configuration.ORIENTATION_LANDSCAPE) {
//            mTwoPane = true;
//            if (mPager.getCurrentItem() == 0) {
//                super.onBackPressed();
//            } else {
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//            }
//        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StepFragment stepFragment = new StepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_DATA, mSteps.get(position));
            stepFragment.setArguments(bundle);

            mStepPosition = position;
            return stepFragment;
        }

        @Override
        public int getCount() {
            return mSteps.size();
        }
    }
}

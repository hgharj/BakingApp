package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;
import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.BindView;

public class StepSlidePagerActivity extends AppCompatActivity{
    private static final String INGREDIENT_DATA = "pass-ingredients";
    private static final String STEP_DATA="pass-step";
    private static final String STEP_FRAGMENT="step-fragment";
    private static final String STEP_LIST_DATA ="pass-step-list";
    private static final String STEP_POSITION ="pass-step-position";
    private static final String CURRENT_STEP ="Current Step";
    private static final String RECIPE_TITLE = "recipe-title";
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    PagerAdapter mPagerAdapter;
    private StepFragment mStepFragment;
    ArrayList<Step> mSteps;
    ArrayList<Ingredient> mIngredients;
    String mRecipeTitle;
    int mStepPosition=-1;
    boolean mSaveToHistory;
    Stack<Integer> mPageHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_screen_slide);
        ButterKnife.bind(this);
        if(savedInstanceState != null){
            mSteps = savedInstanceState.getParcelableArrayList(STEP_DATA);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
        } else {
            Intent data = getIntent();
            mSteps = data.getParcelableArrayListExtra(STEP_LIST_DATA);
            mStepPosition = data.getIntExtra(STEP_POSITION, 0);
            mIngredients = data.getParcelableArrayListExtra(INGREDIENT_DATA);
            mRecipeTitle = data.getStringExtra(RECIPE_TITLE);
        }

        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(CURRENT_STEP);
        }

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);
        mPager.setCurrentItem(mStepPosition);

        mPageHistory = new Stack<>();
        mPageHistory.push(mStepPosition);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if(mSaveToHistory)
                    mPageHistory.push(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mSaveToHistory = true;
    }

    @Override
    public void onBackPressed() {
        if(mPageHistory == null || mPageHistory.size()< 1)
            super.onBackPressed();
        else {
            mSaveToHistory = false;
            if(mPageHistory.peek()==mPager.getCurrentItem()){
                mPageHistory.pop();
                if(mPageHistory.size()< 1){
                    super.onBackPressed();
                }
            }
            mPager.setCurrentItem(mPageHistory.pop());
            mSaveToHistory = true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        Intent recipeDetail = new Intent(this,RecipeDetailActivity.class);
        recipeDetail.putExtra(RECIPE_TITLE,mRecipeTitle);
        recipeDetail.putParcelableArrayListExtra(STEP_LIST_DATA,mSteps);
        recipeDetail.putParcelableArrayListExtra(INGREDIENT_DATA,mIngredients);
        startActivity(recipeDetail);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        //Save the fragment's instance
//        if(mStepFragment!=null) {
//            getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT, mStepFragment);
//        }

        if(mStepPosition>-1){
            outState.putInt(STEP_POSITION,mStepPosition);
        }

        if(mSteps!=null){
            outState.putParcelableArrayList(STEP_DATA,mSteps);
        }
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

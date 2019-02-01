package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;
import butterknife.BindView;

public class RecipeDetailActivity extends AppCompatActivity implements Transition.TransitionListener {
    private static final String INGREDIENT_DATA = "pass-ingredients";
    private static final String STEP_LIST_DATA = "pass-step-list";
    private static final String LOG_TAG = RecipeDetailActivity.class.getName();
    private ViewGroup mViewGroup;
    final Context mContext = this;
    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    @BindView(R.id.ingredients_card) View ingredientsCard;
    @BindView(R.id.ingredients_title_tv) View ingredientsTitleTv;
    @BindView(R.id.ingredients_tv) View ingredientsTv;


    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private Handler mDelayedTransactionHandler = new Handler();
    private Runnable mRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_list);

        Intent data = getIntent();
        ArrayList<Ingredient> ingredients = data.getParcelableArrayListExtra(INGREDIENT_DATA);
        ArrayList<Step> steps = data.getParcelableArrayListExtra(STEP_LIST_DATA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientsMiniFragment ingredientsMiniFragment = new IngredientsMiniFragment();
        Bundle bundleIngredients = new Bundle();
        bundleIngredients.putParcelableArrayList(INGREDIENT_DATA,ingredients);
        ingredientsMiniFragment.setArguments(bundleIngredients);
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsMiniFragment)
                .commit();

        StepListFragment stepListFragment = new StepListFragment();
        Bundle bundleSteps = new Bundle();
        bundleSteps.putParcelableArrayList(STEP_LIST_DATA,steps);
        stepListFragment.setArguments(bundleSteps);
        fragmentManager.beginTransaction()
                .add(R.id.steps_rv, stepListFragment)
                .commit();
//        mRunnable=new Runnable() {
//            @Override
//            public void run() {
//                performTransition();
//            }
//        };
//        mRunnable.run();

//        ingredientsMiniFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        performTransition();
//                    }
//                };
//            }
//        });

//        loadInitialFragment();
//        mDelayedTransactionHandler.postDelayed(mRunnable, 1000);

//        ingredientsMiniFragment.setTra
//        mViewGroup = (ViewGroup)findViewById(R.id.ingredients_container);
//        ingredientsMiniFragment.onClick();
//        ingredientsMiniFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
//
//                    TransitionManager.go(Scene.getSceneForLayout(mViewGroup,R.layout.fragment_ingredients,mContext));
//
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//                    mViewGroup.setElevation(4);
//            }
//        });
    }

//    @Override
//    public void onClick(View v) {
//        String s = "d";
//    }

    private void loadInitialFragment()
    {
        Fragment initialFragment = IngredientsMiniFragment.newInstance();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ingredients_container, initialFragment);
        fragmentTransaction.commit();

        Fragment stepListFragment = StepListFragment.newInstance();
        mFragmentManager.beginTransaction()
                .replace(R.id.ingredients_container, initialFragment)
                .commit();
    }

    private void performTransition(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            if(!isDestroyed()){
                Fragment previousFragment = mFragmentManager.findFragmentById(R.id.ingredients_container);
                Fragment nextFragment = IngredientsFragment.newInstance();

                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                //Exit Previous Fragment
                Fade exitFade = new Fade();
                exitFade.setDuration(FADE_DEFAULT_TIME);
                previousFragment.setExitTransition(exitFade);

                //Share Elements Transition
                TransitionSet enterTransitionSet = new TransitionSet();
                enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(R.transition.card_exit));
                enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
                enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
                nextFragment.setSharedElementEnterTransition(enterTransitionSet);

                //Enter New Fragment Transition
                Fade enterFade = new Fade();
                enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
                enterFade.setDuration(FADE_DEFAULT_TIME);
                nextFragment.setEnterTransition(enterFade);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                    fragmentTransaction.addSharedElement(ingredientsCard, ingredientsCard.getTransitionName());
                    fragmentTransaction.addSharedElement(ingredientsTitleTv, ingredientsTitleTv.getTransitionName());
                    fragmentTransaction.addSharedElement(ingredientsTv, ingredientsTv.getTransitionName());
                    fragmentTransaction.replace(R.id.ingredients_container,nextFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }

            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        mDelayedTransactionHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onTransitionStart(Transition transition) {
        String s = "";
    }

    @Override
    public void onTransitionEnd(Transition transition) {
        String s = "";
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {
        String s = "";
    }
}

package com.example.android.bakingapp.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import butterknife.ButterKnife;
import butterknife.BindView;

public class IngredientsMiniFragment extends Fragment implements Transition.TransitionListener {
    private static final String INGREDIENT_DATA = "pass-ingredients";
    private static final String INGREDIENTS_TITLE = "ingredients-title";
    private static final String INGREDIENTS_DETAIL = "ingredients-detail";
    private OnDataPass dataPasser;
//    private OnClickListener listener;
    private String mIngredientsTitle;
    private String mIngredientsDetail;
    @BindView(R.id.ingredients_card) MaterialCardView mMcw;
    @BindView(R.id.ingredients_title_tv) TextView mIngredients_title_tv;
    @BindView(R.id.ingredients_tv) TextView mIngredients_tv;
//    private View mRoot;
//    private Context mContext;

    public IngredientsMiniFragment() {
        super();
    }

    public interface OnDataPass{
        void onDataPass(ArrayList<Ingredient> ingredients);
    }

//    public interface OnClickListener{
//        void onClick(List<Ingredient> ingredients);
//    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (dataPasser != null){
        dataPasser = (OnDataPass)context;}
//        mContext=context;
    }

    public static IngredientsMiniFragment newInstance() {

        Bundle args = new Bundle();

        IngredientsMiniFragment fragment = new IngredientsMiniFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_mini,container,false);
        ButterKnife.bind(this,rootView);
        List<Ingredient> ingredients = getArguments().getParcelableArrayList(INGREDIENT_DATA);

        StringBuilder sb = new StringBuilder();
        if(savedInstanceState!=null){
            mIngredientsTitle = savedInstanceState.getString(INGREDIENTS_TITLE);
            mIngredientsDetail = savedInstanceState.getString(INGREDIENTS_DETAIL);
        } else {
            mIngredientsTitle=getString(R.string.ingredients_title);
            if (ingredients != null && ingredients.size()>0) {
                for (Ingredient ingredient : ingredients) {
                    sb.append(ingredient.getIngredient().toLowerCase());
                    sb.append("     ");
                    sb.append(ingredient.getQuantity().toString());
                    sb.append(" ");
                    sb.append(ingredient.getMeasure().toLowerCase());
                    sb.append(getString(R.string.carriage_return));
                }
            }
        }

        mIngredientsDetail = sb.toString();
        mIngredients_title_tv.setText(mIngredientsTitle);
        mIngredients_tv.setText(mIngredientsDetail);

//        mMcw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
//                TransitionManager.go(Scene.getSceneForLayout(container,R.layout.fragment_ingredients,getContext()));
//
//                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//                mMcw.setElevation(4);
//            }
//        });

//        if(savedInstanceState!=null){
//
//        }else{
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//            savedInstanceState=ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
//        }
//        mRoot=rootView;
            return rootView;
//        return rootView;
    }

    public void passData(ArrayList<Ingredient> ingredients){
        dataPasser.onDataPass(ingredients);
    }

//    public void setOnClickListener(View.OnClickListener onClickListener){
////        ViewGroup container = (ViewGroup)findViewById(R.id.ingredients_container);
////        View rootView = LayoutInflater.inflate(R.layout.fragment_ingredients_mini,container,false);
////        onClickListener.onClick(mRoot);
//    }


//    @Override
//    public void onClick(View v) {
//
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INGREDIENTS_TITLE,mIngredientsTitle);
        outState.putString(INGREDIENTS_DETAIL,mIngredientsDetail);
    }

    @Override
    public void onTransitionStart(Transition transition) {
        TransitionValues transitionValues = new TransitionValues();
        transitionValues.values.put(INGREDIENTS_TITLE,mIngredientsTitle);
        transitionValues.values.put(INGREDIENTS_DETAIL,mIngredientsDetail);
        transition.captureStartValues(transitionValues);
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
//        transition.getTransitionValues(rootView,);
        mIngredients_title_tv.setText(mIngredientsTitle);
        mIngredients_tv.setText(mIngredientsDetail);
    }
}

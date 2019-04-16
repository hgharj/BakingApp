package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.RecipeUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.BindView;

public class IngredientsFragment extends Fragment {
    private static final String INGREDIENT_DATA = "pass-ingredients-as-string";
    private static final String INGREDIENTS_TITLE = "ingredients-title";
    private static final String INGREDIENTS_DETAIL = "ingredients-detail";
    private String mIngredientsTitle;
    private String mIngredientsDetail;
    @BindView(R.id.ingredients_card) MaterialCardView mMcw;
    @BindView(R.id.ingredients_title_tv) TextView mIngredients_title_tv;
    @BindView(R.id.ingredients_tv) TextView mIngredients_tv;

    public IngredientsFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients,container,false);
        ButterKnife.bind(this,rootView);
        String ingredients = getArguments().getString(INGREDIENT_DATA);

        if(savedInstanceState!=null){
            mIngredientsTitle = savedInstanceState.getString(INGREDIENTS_TITLE);
            mIngredientsDetail = savedInstanceState.getString(INGREDIENTS_DETAIL);
        } else {
            mIngredientsTitle=getString(R.string.ingredients_title);
            mIngredientsDetail=ingredients;
        }

        mIngredients_title_tv.setText(mIngredientsTitle);
        mIngredients_tv.setText(mIngredientsDetail);
            return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INGREDIENTS_TITLE,mIngredientsTitle);
        outState.putString(INGREDIENTS_DETAIL,mIngredientsDetail);
    }
}

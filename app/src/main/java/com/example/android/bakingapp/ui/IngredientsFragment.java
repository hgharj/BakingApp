package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.BindView;

public class IngredientsFragment extends Fragment {
    private static final String INGREDIENT_DATA = "pass-ingredients";
    private static final String INGREDIENTS_TITLE = "ingredients-title";
    private static final String INGREDIENTS_DETAIL = "ingredients-detail";
    private OnDataPass dataPasser;
    private String mIngredientsTitle;
    private String mIngredientsDetail;
    @BindView(R.id.ingredients_card) MaterialCardView mMcw;
    @BindView(R.id.ingredients_title_tv) TextView mIngredients_title_tv;
    @BindView(R.id.ingredients_tv) TextView mIngredients_tv;

    public IngredientsFragment() {
        super();
    }

    public interface OnDataPass{
        void onDataPass(ArrayList<Ingredient> ingredients);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (dataPasser != null){
        dataPasser = (OnDataPass)context;}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients,container,false);
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
            return rootView;
    }

    public void passData(ArrayList<Ingredient> ingredients){
        dataPasser.onDataPass(ingredients);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INGREDIENTS_TITLE,mIngredientsTitle);
        outState.putString(INGREDIENTS_DETAIL,mIngredientsDetail);
    }
}

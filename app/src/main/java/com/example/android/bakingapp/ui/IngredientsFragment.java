package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.fragment.app.Fragment;

public class IngredientsFragment extends Fragment {
    private static final String INGREDIENTS_LIST="ingredients-list";

    public IngredientsFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients,container,false);
        MaterialCardView mcw = rootView.findViewById(R.id.ingredients_card);
        TextView ingredients_tv = rootView.findViewById(R.id.ingredients_tv);
        TextView ingredients_title_tv = rootView.findViewById(R.id.ingredients_title_tv);

        List<Ingredient> ingredients = getArguments().getParcelableArrayList(INGREDIENTS_LIST);

        StringBuilder sb = new StringBuilder();
        for(Ingredient ingredient:ingredients){
            sb.append(ingredient.toString() + getResources().getString(R.string.carriage_return));
        }

        ingredients_tv.setText(getString(R.string.ingredients_title + R.string.carriage_return));
        ingredients_tv.setText(sb.toString());

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

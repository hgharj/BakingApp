package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Controller;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeResponse;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MasterRecipeFragment extends Fragment {
    public MasterRecipeFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_master_list,container,false);

        RecyclerView masterList = (RecyclerView) rootView.findViewById(R.id.master_recipe_list);
//        List<Recipe> recipes;
        Controller controller = new Controller();
        controller.getRecipeList();
//        RecipeResponse response = new RecipeResponse();

        MasterListAdapter masterListAdapter = new MasterListAdapter(rootView.getContext(),controller.getRecipeList());
        masterList.setAdapter(masterListAdapter);

        return rootView;
    }
}

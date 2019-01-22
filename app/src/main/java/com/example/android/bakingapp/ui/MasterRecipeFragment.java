package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.AsyncTask;
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
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.app.Fragment;
//import android.support.v7.widget.RecyclerView;

public class MasterRecipeFragment extends Fragment {
    private RecyclerView mMasterList;
    public MasterRecipeFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_master_list,container,false);

        mMasterList = (RecyclerView) rootView.findViewById(R.id.master_recipe_list);
//        List<Recipe> recipes;

//        RecipeResponse response = new RecipeResponse();
        new getRecipes().execute();


        return rootView;
    }

    private class getRecipes extends AsyncTask<Void,Void,List<Recipe>> {
        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            MasterListAdapter masterListAdapter = new MasterListAdapter(getContext(),recipes);
            mMasterList.setAdapter(masterListAdapter);
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            Controller controller = new Controller();

            return controller.getRecipeList();
        }
    }
}

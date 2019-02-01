package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.BindView;

public class MasterRecipeFragment extends Fragment {
    @BindView(R.id.master_recipe_list) RecyclerView mMasterList;
    private OnDataPass dataPasser;

    public MasterRecipeFragment(){}

    public interface OnDataPass {
        void onDataPass(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recipe_master_list,container,false);

        ButterKnife.bind(this,rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mMasterList.setLayoutManager(linearLayoutManager);

        new getRecipes().execute();

        return rootView;
    }

    public void passData(Recipe recipe){
        dataPasser.onDataPass(recipe);
    }

    private class getRecipes extends AsyncTask<Void,Void,List<Recipe>> {
        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            MasterListAdapter masterListAdapter = new MasterListAdapter(getContext(), recipes, new MasterListAdapter.OnClickListener() {
                @Override
                public void onItemClick(Recipe recipe) {
                    passData(recipe);
                }
            });
            mMasterList.setAdapter(masterListAdapter);
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            Controller controller = new Controller();
            return controller.getRecipeList();
        }
    }


}

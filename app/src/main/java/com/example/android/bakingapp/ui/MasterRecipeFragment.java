package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Controller;
import com.example.android.bakingapp.utils.NetworkUtils;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.RecipeResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.BindView;

public class MasterRecipeFragment extends Fragment {
    @BindView(R.id.master_recipe_list) RecyclerView mMasterList;
    @BindView(R.id.empty_view)
    TextView mEmptyStateTextView;
    private OnDataPass dataPasser;
    private OnRecipesPassed recipesPasser;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private MasterListAdapter mAdapter;
    private static final String RECYCLER_LAYOUT = "recycler-layout";
    private static final String RECYCLER_LIST_DATA = "recycler-list-data";

    public MasterRecipeFragment(){}

    public interface OnDataPass {
        void onDataPass(Recipe recipe);
    }

    public interface OnRecipesPassed {
        void onRecipesPassed(ArrayList<Recipe> recipes);
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
        ArrayList<Recipe> recipes = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mMasterList.setLayoutManager(linearLayoutManager);

        mAdapter = new MasterListAdapter(getContext(), mRecipes, new MasterListAdapter.OnClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                passData(recipe);
            }
        });

        if(savedInstanceState!=null){
            mMasterList.setAdapter(mAdapter);
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            mMasterList.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            mRecipes = savedInstanceState.getParcelableArrayList(RECYCLER_LIST_DATA);
            mAdapter.addAll(mRecipes);
        } else {
            mAdapter.clear();
            new getRecipes().execute();
        }

        passRecipes(mRecipes);
        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            passData(mRecipes.get(0));
        }
        return rootView;
    }

    public void passData(Recipe recipe){
        dataPasser.onDataPass(recipe);
    }

    public void passRecipes(ArrayList<Recipe> recipes) {recipesPasser.onRecipesPassed(recipes);}

    private class getRecipes extends AsyncTask<Void,Void,List<Recipe>> {
        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);
            if (mAdapter != null)
                mAdapter.clear();

            // If there is a valid list of {@link Recipe}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (recipes != null && !recipes.isEmpty()) {
                mRecipes= new ArrayList<>();
                mRecipes.addAll(recipes);
                mAdapter.addAll(recipes);
                mMasterList.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
            } else {
                mEmptyStateTextView.setText(R.string.no_recipes);
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }

            mMasterList.setAdapter(mAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
            mMasterList.setLayoutManager(linearLayoutManager);
            mMasterList.setHasFixedSize(true);
//            if (mSwipeRefreshLayout.isRefreshing()) {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            Controller controller = new Controller();
            return controller.getRecipeList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_LAYOUT, mMasterList.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(RECYCLER_LIST_DATA,mRecipes);
    }
}

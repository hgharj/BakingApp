package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {
    private static final String INGREDIENT_DATA = "pass-ingredients-as-string";
    private static final String INGREDIENT_LIST_DATA = "pass-ingredients-as-arraylist";
    private static final String STEP_LIST_DATA = "pass-step-list";
    private static final String STEP_POSITION = "pass-step-position";
    @BindView(R.id.step_list)
    RecyclerView mStepListRecyclerView;
    ArrayList<Step> mSteps;
    String mIngredients;
    private OnDataPass dataPasser;
    private static final String RECYCLER_LAYOUT = "recycler-layout";
    private static final String RECYCLER_LIST_DATA = "recycler-list-data";
    private static final String RECIPE_TITLE = "recipe-title";
    String mRecipeTitle;

    public interface OnDataPass {
        void onDataPass(ArrayList<Step> steps, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (dataPasser != null) {
            dataPasser = (OnDataPass) context;
        }
    }

    public StepListFragment() {
        super();
    }

    public void passData(ArrayList<Step> steps, int position) {
        dataPasser.onDataPass(steps, position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mStepListRecyclerView.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            mStepListRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            mSteps = savedInstanceState.getParcelableArrayList(RECYCLER_LIST_DATA);
        } else {
            mSteps = getArguments().getParcelableArrayList(STEP_LIST_DATA);
            mIngredients = getArguments().getString(INGREDIENT_DATA);
            mRecipeTitle = getArguments().getString(RECIPE_TITLE);
        }

        final StepListAdapter stepListAdapter;
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {

            stepListAdapter = new StepListAdapter(getContext(), mSteps, new StepListAdapter.OnClickListener() {
                @Override
                public void onItemClick(Step step, int position) {
                    passData(mSteps, position);
                }
            });
        } else {
            stepListAdapter = new StepListAdapter(getContext(), mSteps, new StepListAdapter.OnClickListener() {
                @Override
                public void onItemClick(Step step, int position) {
                    Intent viewPagerIntent = new Intent(getContext(), StepSlidePagerActivity.class);
                    viewPagerIntent.putParcelableArrayListExtra(STEP_LIST_DATA, mSteps);
                    viewPagerIntent.putExtra(STEP_POSITION, position);
                    viewPagerIntent.putExtra(INGREDIENT_DATA, mIngredients);
                    viewPagerIntent.putExtra(RECIPE_TITLE, mRecipeTitle);
                    startActivity(viewPagerIntent);
                }
            });
        }

        mStepListRecyclerView.setAdapter(stepListAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_LAYOUT, mStepListRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(RECYCLER_LIST_DATA, mSteps);
    }
}

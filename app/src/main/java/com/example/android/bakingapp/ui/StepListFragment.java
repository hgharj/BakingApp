package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.BindView;

public class StepListFragment extends Fragment {
    private static final String STEP_LIST_DATA ="pass-step-list";
    private static final String STEP_POSITION ="pass-step-position";
    @BindView(R.id.step_list)
    RecyclerView mStepList;
    ArrayList<Step> mSteps;
    private OnDataPass dataPasser;
    private StepListAdapter mAdapter;
    private static final String RECYCLER_LAYOUT = "recycler-layout";
    private static final String RECYCLER_LIST_DATA = "recycler-list-data";

    public interface OnDataPass {
        void onDataPass(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        dataPasser = (OnDataPass)context;
        if (dataPasser != null){
            dataPasser = (OnDataPass)context;}
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(savedInstanceState!=null){
//            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
//            mStepList.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
//            mSteps = savedInstanceState.getParcelableArrayList(RECYCLER_LIST_DATA);
//        } else {
//            mSteps = getArguments().getParcelableArrayList(STEP_LIST_DATA);
//        }
//    }

    public StepListFragment() {
        super();
    }

    public void passData(Step step){
        dataPasser.onDataPass(step);
    }

//    public static StepListFragment newInstance() {
//        Bundle args = new Bundle();
//
//        StepListFragment fragment = new StepListFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this,rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mStepList.setLayoutManager(linearLayoutManager);

        if(savedInstanceState!=null){
            Parcelable savedRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            mStepList.getLayoutManager().onRestoreInstanceState(savedRecyclerViewState);
            mSteps = savedInstanceState.getParcelableArrayList(RECYCLER_LIST_DATA);
        } else {
            mSteps = getArguments().getParcelableArrayList(STEP_LIST_DATA);
        }

        StepListAdapter stepListAdapter = new StepListAdapter(getContext(), mSteps, new StepListAdapter.OnClickListener() {
            @Override
            public void onItemClick(Step step, int position) {
                Intent viewPagerIntent = new Intent(getContext(), StepSlidePagerActivity.class);
                viewPagerIntent.putParcelableArrayListExtra(STEP_LIST_DATA,mSteps);
                viewPagerIntent.putExtra(STEP_POSITION,position);
                startActivity(viewPagerIntent);
            }
        });

        mStepList.setAdapter(stepListAdapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_LAYOUT, mStepList.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(RECYCLER_LIST_DATA,mSteps);
    }
}

package com.example.android.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {
    private static final String STEP_DATA="pass-step";
    @BindView(R.id.step_title_tv)
    TextView mStepTitleTv;
    @BindView(R.id.step_desc_tv)
    TextView mStepDescTv;
    private OnDataPass dataPasser;

    public interface OnDataPass {
        void onDataPass(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (dataPasser != null){
            dataPasser = (OnDataPass)context;}
    }

    public StepFragment() {
        super();
    }

    public void passData(Step step){
        dataPasser.onDataPass(step);
    }

    public static StepFragment newInstance() {
        Bundle args = new Bundle();

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_list_item, container, false);
        ButterKnife.bind(this,rootView);

       Step step = getArguments().getParcelable(STEP_DATA);

       mStepTitleTv.setText(step.getShortDescription());
       mStepDescTv.setText(step.getDescription());

        return rootView;
    }
}

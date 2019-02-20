package com.example.android.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

public class StepSlidePagerActivity extends FragmentActivity {
    private static final String STEP_DATA="pass-step";
    private static final String STEP_FRAGMENT="step-fragment";
    private static final String STEP_LIST_DATA ="pass-step-list";
    private static final String STEP_POSITION ="pass-step-position";
    @BindView(R.id.pager) ViewPager mPager;
    PagerAdapter mPagerAdapter;
    private StepFragment mStepFragment;
    ArrayList<Step> mSteps;
    int mStepPosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_screen_slide);
        ButterKnife.bind(this);
        if(savedInstanceState != null){
            mSteps = savedInstanceState.getParcelableArrayList(STEP_DATA);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION);
        } else {
            Intent data = getIntent();
            mSteps = data.getParcelableArrayListExtra(STEP_LIST_DATA);
            mStepPosition = data.getIntExtra(STEP_POSITION, 0);
        }

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mStepPosition);
    }

    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(getActionBar()!=null) {
                getActionBar().hide();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(getActionBar()!=null) {
                getActionBar().show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        //Save the fragment's instance
//        if(mStepFragment!=null) {
//            getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT, mStepFragment);
//        }

        if(mStepPosition>-1){
            outState.putInt(STEP_POSITION,mStepPosition);
        }

        if(mSteps!=null){
            outState.putParcelableArrayList(STEP_DATA,mSteps);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StepFragment stepFragment = new StepFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEP_DATA, mSteps.get(position));
            stepFragment.setArguments(bundle);

            mStepPosition = position;
            return stepFragment;
        }

        @Override
        public int getCount() {
            return mSteps.size();
        }
    }
}

package com.example.android.bakingapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private Context context;
    private final List<Step> steps;
    private final OnClickListener listener;

    public interface OnClickListener{
        void onItemClick(Step step,int position);
    }

    public StepListAdapter(Context context, List<Step> steps, OnClickListener listener){
        this.context=context;
        this.steps=steps;
        this.listener=listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(steps.get(position), position, listener);
    }

    public void clear() {
        steps.clear();
    }

    public void addAll(List<Step> steps) {
        this.steps.addAll(steps);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.step_card) MaterialCardView stepCard;
        @BindView(R.id.step_title_tv) TextView stepTitleTv;
        @BindView(R.id.step_desc_tv) TextView stepDescTv;

        public StepViewHolder(View v){
            super(v);
            ButterKnife.bind(this,v);
        }

        private void bind(final Step step, final int position, final OnClickListener listener){
            String stepNumber = Integer.toString(step.getId());
            stepTitleTv.setText(stepNumber + " " + step.getShortDescription());
            stepDescTv.setText(step.getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(step, position);
                }
            });
        }
    }
}

package com.example.android.bakingapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Ingredient;
import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.Step;

import java.util.ArrayList;
import java.util.List;

//import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeViewHolder> {

    private Context context;
    private final List<Recipe> recipes;
    private final OnClickListener listener;

    public interface OnClickListener{
        void onItemClick(Recipe recipe);
    }

    public MasterListAdapter(Context context, List<Recipe> recipes, OnClickListener listener){
        this.context=context;
        this.recipes=recipes;
        this.listener=listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position), listener);
    }

    public void clear() {
        recipes.clear();
    }

    public void addAll(List<Recipe> recipes) {
        this.recipes.addAll(recipes);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recipe_title_tv) TextView recipe_title_tv;
        @BindView(R.id.recipe_servings_tv) TextView recipe_servings_tv;

        public RecipeViewHolder(View v){
            super(v);
            ButterKnife.bind(this,v);
        }

        private void bind(final Recipe recipe, final OnClickListener listener){
            String servings = "Servings: " + recipe.getServings();
            recipe_title_tv.setText(recipe.getName());
            recipe_servings_tv.setText(servings);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(recipe);
                }
            });
        }

        private void ingredientsAndSteps(Recipe recipe){
            StringBuilder sb = new StringBuilder();
            sb.append("Name: " + recipe.getName() + "\n");
            sb.append("Id: " + recipe.getId() + "\n");
            sb.append("Servings: " + recipe.getServings() + "\n" + "\n");
            sb.append("     " + "Ingredients " + "\n");
            for(Ingredient ingredient: recipe.getIngredients()){
                sb.append("          " + "Ingredient: " + ingredient.getIngredient() + "\n");
                sb.append("          " + "Measure: " + ingredient.getMeasure() + "\n");
                sb.append("          " + "Quantity: " + ingredient.getQuantity() + "\n" + "\n");
            }
            sb.append("     " + "Steps " + "\n");
            for(Step step: recipe.getSteps()){
                sb.append("          " + "Id: " + step.getId() + "\n");
                sb.append("          " + "Short Description: " + step.getShortDescription() + "\n");
                sb.append("          " + "Description: " + step.getDescription() + "\n");
                sb.append("          " + "Video Url: " + step.getVideoUrl() + "\n");
                sb.append("          " + "Thumbnail Url: " + step.getThumbnailUrl() + "\n");
            }
            //            card_tv.setText(sb.toString());
        }
    }
}

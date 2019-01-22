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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListAdapter extends RecyclerView.Adapter<MasterListAdapter.RecipeViewHolder> {

    private Context context;
    private final List<Recipe> recipes;

    public MasterListAdapter(Context context, List<Recipe> recipes){
        this.context=context;
        this.recipes=recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
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
        @BindView(R.id.card_tv) TextView card_tv;

        public RecipeViewHolder(View v){
            super(v);
            ButterKnife.bind(this,v);
        }

        private void bind(final Recipe recipe){
            StringBuilder sb = new StringBuilder();
            sb.append("Name: " + recipe.getName() + "\n");
            sb.append("Id: " + recipe.getId() + "\n");
            sb.append("Servings: " + recipe.getServings() + "\n");
            sb.append("Ingredients: " + "\n");
            for(Ingredient ingredient: recipe.getIngredients()){
                sb.append("Ingredient" + ingredient.getIngredient() + "\n");
                sb.append("Measure" + ingredient.getMeasure() + "\n");
                sb.append("Quantity" + ingredient.getQuantity() + "\n");
            }
            for(Step step: recipe.getSteps()){
                sb.append("Id" + step.getId());
                sb.append("Short Description:" + step.getShortDescription() + "\n");
                sb.append("Description" + step.getDescription() + "\n");
                sb.append("Video Url:" + step.getVideoUrl() + "\n");
                sb.append("Thumbnail Url" + step.getThumbnailUrl() + "\n");
            }
            card_tv.setText(sb.toString());
        }
    }
}

package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;
import com.example.android.mbejaranoe.bakingapp.data.Step;

/**
 * Created by Manolo on 10/10/2017.
 * Adapter class for ingredients and steps RecyclerView
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Declare integers for both ingredients and steps view types
    private final int VIEW_TYPE_INGREDIENTS = 0;
    private final int VIEW_TYPE_STEPS = 1;

    // Member variables
    private Ingredient[] mIngredients;
    private Step[] mSteps;
    private Context mContext;

    // Constructor
    public RecipeDetailAdapter(Ingredient[] ingredients, Step[] steps, Context context){
        mIngredients = ingredients;
        mSteps = steps;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mIngredients.length){
            return VIEW_TYPE_STEPS;
        } else {
            return VIEW_TYPE_INGREDIENTS;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_INGREDIENTS){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_ingredient, parent, false);
            return new IngredientsViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_step, parent, false);
            return new StepsViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_INGREDIENTS:
                IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) holder;
                IngredientsViewHolder.setIngredientsViewHolder(ingredientsViewHolder, mIngredients[position]);
                break;
            case VIEW_TYPE_STEPS:
                StepsViewHolder stepsViewHolder = (StepsViewHolder) holder;
                StepsViewHolder.setStepsViewHolder(mContext, stepsViewHolder, mSteps[position - mIngredients.length]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mIngredients.length + mSteps.length;
    }
}

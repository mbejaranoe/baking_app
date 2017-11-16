package com.example.android.mbejaranoe.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Manolo on 10/10/2017.
 * ViewHolder for ingredients
 */

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    private final String LOG_TAG = IngredientsViewHolder.class.getSimpleName();

    @BindView(R.id.ingredient_name_text_view) TextView ingredientNameTextView;
    @BindView(R.id.quantity_text_view) TextView quantityTextView;
    @BindView(R.id.measure_text_view) TextView measureTextView;

    public IngredientsViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static void setIngredientsViewHolder(IngredientsViewHolder holder, Ingredient  ingredient){
        holder.ingredientNameTextView.setText(ingredient.getIngredient());
        holder.quantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        holder.measureTextView.setText(ingredient.getMeasure());
    }
}

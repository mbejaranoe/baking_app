package com.example.android.mbejaranoe.bakingapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.Ingredient;

/**
 * Created by Manolo on 10/10/2017.
 * ViewHolder for ingredients
 */

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    private TextView ingredientNameTextView;
    private TextView quantityTextView;
    private TextView measureTextView;
    private CardView ingredientCardView;

    public IngredientsViewHolder(View itemView) {
        super(itemView);

        ingredientNameTextView = (TextView) itemView.findViewById(R.id.ingredient_name_text_view);
        quantityTextView = (TextView) itemView.findViewById(R.id.quantity_text_view);
        measureTextView = (TextView) itemView.findViewById(R.id.measure_text_view);
        ingredientCardView = (CardView) itemView.findViewById(R.id.ingredient_card_view);

        /*
        int smallestScreenWidthDp = itemView.getContext().getResources().getConfiguration().smallestScreenWidthDp;
        int orientation = itemView.getContext().getResources().getConfiguration().orientation;
        if (smallestScreenWidthDp >= 600 && orientation == Configuration.ORIENTATION_PORTRAIT) {
            ingredientNameTextView.setSingleLine(false);
            int cardViewWidth = ingredientCardView.getWidth();
            int width = (2*cardViewWidth)/3;
            int height = ingredientNameTextView.getHeight();
            ingredientNameTextView.setLayoutParams(new FrameLayout.LayoutParams(width,height));
        }
        */
    }

    public static void setIngredientsViewHolder(IngredientsViewHolder holder, Ingredient  ingredient){
        holder.ingredientNameTextView.setText(ingredient.getIngredient());
        holder.quantityTextView.setText(String.valueOf(ingredient.getQuantity()));
        holder.measureTextView.setText(ingredient.getMeasure());
    }
}

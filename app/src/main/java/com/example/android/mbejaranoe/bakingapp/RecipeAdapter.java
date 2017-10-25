package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by Manolo on 04/10/2017.
 * Adapter to show in the screen the recipes
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final String RECIPE_NAME_KEY = "name";
    private final String RECIPE_ID_KEY = "recipe_Id";

    // Cursor to store the recipes data
    private Cursor mCursor;
    private Context mContext;

    // Constructor
    public RecipeAdapter(){
    }

    // Required Adapter method
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutId = R.layout.list_item_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(view);

        return viewHolder;
    }

    // Required Adapter method
    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int imageUrlIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE_URL);
        int nameIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
        int servingsIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_SERVINGS);

        if (mCursor.getString(imageUrlIndex).length() == 0) { // there is no image
            Picasso.with(mContext).load(R.drawable.recipestepplaceholder_blue).into(holder.recipeImageView);
        } else { // there is an image
            Picasso.with(mContext).load(mCursor.getString(imageUrlIndex)).into(holder.recipeImageView);
        }
        holder.recipeNameTextView.setText(mCursor.getString(nameIndex));
        holder.recipeServingsTextView.setText(mCursor.getString(servingsIndex));
    }

    // Required Adapter method
    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    // ViewHolder class
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView recipeCardView;
        private ImageView recipeImageView;
        private TextView recipeNameTextView;
        private TextView recipeServingsTextView;

        // Constructor
        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            recipeCardView = (CardView) itemView.findViewById(R.id.recipe_card_view);
            recipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image_view);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            recipeServingsTextView = (TextView) itemView.findViewById(R.id.recipe_servings_text_view);

            itemView.setOnClickListener(this);
        }

        // onClick method to start the recipe detail activity
        @Override
        public void onClick(View view) {

            Intent intent;
            if (mContext.getResources().getConfiguration().smallestScreenWidthDp >= 600) { // tablet
                intent = new Intent (view.getContext(), MasterDetailActivity.class);
            } else { // phone
                intent = new Intent(view.getContext(), RecipeDetailActivity.class);
            }

            int adapterPosition = getAdapterPosition();
            Log.v("RecipeAdapter", "Adapter position: " + adapterPosition);
            mCursor.moveToPosition(adapterPosition);
            int recipe_id = mCursor.getInt(mCursor.getColumnIndex(RecipeEntry._ID));
            Log.v("RecipeAdapter", "Recipe _id: " + recipe_id);
            String recipeName = mCursor.getString(mCursor.getColumnIndex(RecipeEntry.COLUMN_NAME));
            Log.v("RecipeAdapter", "Recipe name: " + recipeName);
            intent.putExtra(RECIPE_ID_KEY, recipe_id);
            intent.putExtra(RECIPE_NAME_KEY, recipeName);
            view.getContext().startActivity(intent);
        }
    }
}

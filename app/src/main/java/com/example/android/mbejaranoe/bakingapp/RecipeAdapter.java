package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.squareup.picasso.Picasso;

/**
 * Created by Manolo on 04/10/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private int indexImagePlaceHolder;

    public RecipeAdapter(){

    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        indexImagePlaceHolder = 0;
        int layoutId = R.layout.list_item_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int imageUrlIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE_URL);
        int nameIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
        int servingsIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_SERVINGS);

        if (mCursor.getString(imageUrlIndex).length() == 0) {
            Picasso.with(mContext).load(getRecipeImagePlaceholder(indexImagePlaceHolder)).into(holder.recipeImageView);
        } else {
            Picasso.with(mContext).load(mCursor.getString(imageUrlIndex)).into(holder.recipeImageView);
        }
        holder.recipeNameTextView.setText(mCursor.getString(nameIndex));
        holder.recipeServingsTextView.setText(mCursor.getString(servingsIndex));
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public int getRecipeImagePlaceholder(int index){
        int[] recipeImagePlaceholders = {
                R.drawable.generic01,
                R.drawable.generic02,
                R.drawable.generic03,
                R.drawable.generic04
        };
        if (indexImagePlaceHolder == 3) {
            indexImagePlaceHolder = 0;
        } else {
            indexImagePlaceHolder++;
        }
        return recipeImagePlaceholders[index];
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView recipeCardView;
        private ImageView recipeImageView;
        private TextView recipeNameTextView;
        private TextView recipeServingsTextView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            recipeCardView = (CardView) itemView.findViewById(R.id.recipe_card_view);
            recipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image_view);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            recipeServingsTextView = (TextView) itemView.findViewById(R.id.recipe_servings_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String recipeId = mCursor.getString(mCursor.getColumnIndex(RecipeEntry.COLUMN_RECIPE_ID));
            String recipeName = mCursor.getString(mCursor.getColumnIndex(RecipeEntry.COLUMN_NAME));
            intent.putExtra("recipe_id", recipeId);
            intent.putExtra("name", recipeName);
            view.getContext().startActivity(intent);
        }
    }
}

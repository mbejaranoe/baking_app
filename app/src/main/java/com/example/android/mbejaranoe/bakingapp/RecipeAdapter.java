package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;

/**
 * Created by Manolo on 04/10/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private Cursor mCursor;

    public RecipeAdapter(){

    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.list_item_recipe;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int imageIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE);
        int nameIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_NAME);
        int servingsIndex = mCursor.getColumnIndex(RecipeEntry.COLUMN_SERVINGS);

        byte[] imageByteArrayRecipe = mCursor.getBlob(imageIndex);
        if (imageByteArrayRecipe.length == 0){
            holder.recipeImageView.setImageResource(R.drawable.generic_recipe_image);
        } else {
            Bitmap bitmapRecipe = BitmapFactory.decodeByteArray(imageByteArrayRecipe, 0, imageByteArrayRecipe.length);
            holder.recipeImageView.setImageBitmap(bitmapRecipe);
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
            /*
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String tmdbId = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TMDB_ID));
            intent.putExtra("movie", tmdbId);
            view.getContext().startActivity(intent);
             */
        }
    }
}

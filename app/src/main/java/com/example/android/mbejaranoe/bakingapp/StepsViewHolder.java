package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

/**
 * Created by Manolo on 10/10/2017.
 * ViewHolder for steps
 */

public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView stepThumbnailImageView;
    private TextView stepShortDescriptionTextView;
    private int numIngred;
    private int recipe_Id;

    public StepsViewHolder(View itemView) {
        super(itemView);

        stepThumbnailImageView = (ImageView) itemView.findViewById(R.id.step_thumbnail_image_view);
        stepShortDescriptionTextView = (TextView) itemView.findViewById(R.id.step_short_description_text_view);

        itemView.setOnClickListener(this);
    }

    public static void setStepsViewHolder(Context context, StepsViewHolder holder, Step step){

        if (step.getThumbnailURL().length() == 0) {
            Picasso.with(context).load(R.drawable.recipestepplaceholder_blue).into(holder.stepThumbnailImageView);
        } else {
            Picasso.with(context).load(step.getThumbnailURL()).placeholder(R.drawable.recipestepplaceholder_blue)
                    .into(holder.stepThumbnailImageView);
        }

        holder.stepShortDescriptionTextView.setText(step.getShortDescription());
    }

    public void setRecipe_id(int id){
        recipe_Id = id;
    }

    public void setNumIngred(int num) {
        numIngred = num;
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(view.getContext(), StepDetailActivity.class);
        // pass the recipe _id in order to query the content provider to obtain the json string with steps
        intent.putExtra("recipe_Id", recipe_Id);
        Log.v("StepsViewHolder", "-onClick recipe_Id: " + recipe_Id);
        // pass the step index in order to access the item in the Step[] and retrieve the correct info
        // to populate the views
        int stepIndex = getAdapterPosition() - numIngred;
        intent.putExtra("stepIndex", stepIndex);
        Log.v("StepsViewHolder", "-onClick step index: " + stepIndex);
        intent.putExtra("shortDescription", stepShortDescriptionTextView.getText());
        view.getContext().startActivity(intent);
    }
}

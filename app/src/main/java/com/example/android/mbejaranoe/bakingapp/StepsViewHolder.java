package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
    private static Step[] mSteps;
    private int numIngredients;

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

    @Override
    public void onClick(View view) {

        int stepIndex = getAdapterPosition() - numIngredients;
        Intent intent = new Intent(view.getContext(), StepDetailActivity.class);
        intent.putExtra("shortDescription", mSteps[stepIndex].getShortDescription());
        intent.putExtra("description", mSteps[stepIndex].getDescription());
        intent.putExtra("videoURL", mSteps[stepIndex].getVideoURL());
        intent.putExtra("thumbnailURL", mSteps[stepIndex].getThumbnailURL());
        view.getContext().startActivity(intent);

    }

    public void setStepsArrayToStepsViewHolder(Step[] steps, int i){
        mSteps = steps;
        numIngredients = i;
    }

}

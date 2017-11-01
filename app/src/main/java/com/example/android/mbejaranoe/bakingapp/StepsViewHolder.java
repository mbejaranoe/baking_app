package com.example.android.mbejaranoe.bakingapp;

import android.content.Context;
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
    private int numIngred;
    private int recipe_Id;

    OnStepClickListener mcallback;

    //onStepClickListener interface calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(int stepId);
    }

    public StepsViewHolder(View itemView) {
        super(itemView);

        stepThumbnailImageView = (ImageView) itemView.findViewById(R.id.step_thumbnail_image_view);
        stepShortDescriptionTextView = (TextView) itemView.findViewById(R.id.step_short_description_text_view);

        itemView.setOnClickListener(this);

        try {
            mcallback = (OnStepClickListener) itemView.getContext();
        } catch (ClassCastException e){
            throw new ClassCastException(itemView.getContext().toString()
            + " must implement OnStepClickListener");
        }
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
        /*
        if (view.getContext().getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            // change StepDetailFragment to show the new step details
            int stepIndex = getAdapterPosition() - numIngred;
            mcallback.onStepSelected(stepIndex);
            // marcar el step en color --> see Sunshine
        } else {
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
         */
        int stepIndex = getAdapterPosition() - numIngred;
        mcallback.onStepSelected(stepIndex);
    }
}

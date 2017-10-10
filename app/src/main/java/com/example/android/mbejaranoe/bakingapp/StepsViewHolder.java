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

public class StepsViewHolder extends RecyclerView.ViewHolder {

    private ImageView stepThumbnailImageView;
    private TextView stepShortDescriptionTextView;
    private Context mContext;

    public StepsViewHolder(View itemView) {
        super(itemView);

        stepThumbnailImageView = (ImageView) itemView.findViewById(R.id.step_thumbnail_image_view);
        stepShortDescriptionTextView = (TextView) itemView.findViewById(R.id.step_short_description_text_view);
        mContext = itemView.getContext();
    }

    public static void setStepsViewHolder(Context context, StepsViewHolder holder, Step step){

        if (step.getThumbnailURL().length() == 0) {
            Picasso.with(context).load(R.drawable.ic_recipestepplaceholder).into(holder.stepThumbnailImageView);
        } else {
            Picasso.with(context).load(step.getThumbnailURL()).placeholder(R.drawable.ic_recipestepplaceholder)
                    .into(holder.stepThumbnailImageView);
        }

        holder.stepShortDescriptionTextView.setText(step.getShortDescription());
    }
}

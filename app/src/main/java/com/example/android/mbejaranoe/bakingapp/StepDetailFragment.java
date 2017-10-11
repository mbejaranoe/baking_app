package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Manolo on 10/10/2017.
 * Fragment to show the step details
 */

public class StepDetailFragment extends Fragment {

    private final String LOG_TAG = "StepDetailFragment";

    private ImageView stepDetailThumbnailImageView;
    private TextView stepDetailDescriptionTextView;

    // Constructor
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the step detail fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        stepDetailThumbnailImageView = (ImageView) rootView.findViewById(R.id.step_detail_thumbnail_image_view);
        stepDetailDescriptionTextView = (TextView) rootView.findViewById(R.id.step_detail_description_text_view);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("description")){
            stepDetailDescriptionTextView.setText(intent.getStringExtra("description"));
        } else {
            stepDetailDescriptionTextView.setText(getResources().getString(R.string.no_step_description_message));
        }

        if (intent.hasExtra("thumbnailURL")) {
            if (intent.getStringExtra("thumbnailURL").length() == 0) {
                Picasso.with(getContext()).load(R.drawable.recipestepplaceholder_blue).into(stepDetailThumbnailImageView);
            } else {
                Picasso.with(getContext()).load(intent.getStringExtra("thumbnailURL")).placeholder(R.drawable.recipestepplaceholder_blue)
                        .into(stepDetailThumbnailImageView);
            }
        }

        /*
        intent.putExtra("shortDescription", mSteps[stepIndex].getShortDescription());
        intent.putExtra("videoURL", mSteps[stepIndex].getVideoURL());
         */

        return rootView;
    }
}

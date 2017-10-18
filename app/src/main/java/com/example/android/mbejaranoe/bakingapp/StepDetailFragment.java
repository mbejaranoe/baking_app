package com.example.android.mbejaranoe.bakingapp;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.mbejaranoe.bakingapp.data.Step;
import com.example.android.mbejaranoe.bakingapp.utilities.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Manolo on 10/10/2017.
 * Fragment to show the step details
 */

public class StepDetailFragment extends Fragment {

    private final String LOG_TAG = "StepDetailFragment";

    private SimpleExoPlayerView stepDetailSimpleExoPlayerView;
    private ImageView stepDetailVideoPlaceholderImageView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private TextView stepDetailDescriptionTextView;
    private Button prevStepButton;
    private Button nextStepButton;
    private Step[] mSteps;
    private int mStepIndex;
    private int mRecipe_Id;
    private Cursor mCursor;

    // Constructor
    public StepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the step detail fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        stepDetailSimpleExoPlayerView = (SimpleExoPlayerView) rootView
                .findViewById(R.id.step_detail_simple_exoplayer_view);
        stepDetailVideoPlaceholderImageView = (ImageView) rootView
                .findViewById(R.id.step_detail_video_placeholder_image_view);
        stepDetailDescriptionTextView = (TextView) rootView
                .findViewById(R.id.step_detail_description_text_view);
        prevStepButton = (Button) rootView.findViewById(R.id.prev_step_button);
        nextStepButton = (Button) rootView.findViewById(R.id.next_step_button);

        Bundle args = getArguments();
        mStepIndex = args.getInt("stepIndex");
        mRecipe_Id = args.getInt("recipe_Id");

        updateStepDetails();

        return rootView;
    }

    public void updateStepDetails(){

        // query the content provider using the recipe_Id to get the recipe row
        mCursor = getContext().getContentResolver().query(
                RecipeEntry.CONTENT_URI,
                null,
                "_id=?",
                new String[]{String.valueOf(mRecipe_Id)},
                null);

        // get the steps string and parse it to get a Step[]
        if (mCursor.moveToFirst()){
            // get the json string with the steps details
            String stringSteps = mCursor.getString(mCursor.getColumnIndex(RecipeEntry.COLUMN_STEPS));
            JSONArray stepsJsonArray = null;
            try {
                stepsJsonArray = new JSONArray(stringSteps);
            } catch (JSONException e){
                Log.e(LOG_TAG, "Error parsing steps JSONArray: " + stringSteps);
            }
            // parse the json string into a Step[]
            mSteps = new Step[stepsJsonArray.length()];
            for (int i = 0; i < stepsJsonArray.length(); i++){
                try {
                    mSteps[i] = new Step(stepsJsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSONObject step.");
                }
            }
        }

        // get the appropriate step
        Step step = mSteps[mStepIndex];

        // get the screen orientation
        Configuration configuration = getActivity().getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { // portrait mode

            /*
            // show the action bar
            if(getActivity().getActionBar() != null) {
                getActivity().getActionBar().show();
            }
            */

            // set the title for the parent activity
            getActivity().setTitle(step.getShortDescription());

            // populate description view
            if (step.getDescription().equals("")) {
                stepDetailDescriptionTextView.setVisibility(View.VISIBLE);
                stepDetailDescriptionTextView.setText(getResources()
                        .getString(R.string.no_step_description_message));
            } else {
                stepDetailDescriptionTextView.setText(step.getDescription());
            }

            // populate the thumbnail view
            Bitmap artwork = null;
            if (step.getThumbnailURL().equals("") || (NetworkUtils.getImageFromURL(step.getThumbnailURL()) == null)) {
                artwork = BitmapFactory.decodeResource(getResources(), R.drawable.recipestepplaceholder_black);
            } else {
                artwork = NetworkUtils.getImageFromURL(step.getThumbnailURL());
            }
            stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
            stepDetailSimpleExoPlayerView.setDefaultArtwork(artwork);

            // set the video url for video playback, or the imageview in case there is no video url
            releasePlayer();
            if (!(step.getVideoURL().equals(""))) {
                stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
                stepDetailVideoPlaceholderImageView.setVisibility(View.INVISIBLE);
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else {
                stepDetailSimpleExoPlayerView.setVisibility(View.INVISIBLE);
                stepDetailVideoPlaceholderImageView.setImageResource(R.drawable.recipestepplaceholder_black);
                stepDetailVideoPlaceholderImageView.setVisibility(View.VISIBLE);
                Log.v(LOG_TAG, "No video url available.");
            }

            // enable/disable nav button when on the first/last step
            prevStepButton.setVisibility(View.VISIBLE);
            nextStepButton.setVisibility(View.VISIBLE);
            if (mStepIndex == 0) {
                prevStepButton.setEnabled(false);
            } else if (mStepIndex == mSteps.length - 1) {
                nextStepButton.setEnabled(false);
            } else {
                prevStepButton.setEnabled(true);
                nextStepButton.setEnabled(true);
            }
        } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { // landscape mode

            /*
            // hide the action bar
            if(getActivity().getActionBar() != null) {
                getActivity().getActionBar().hide();
            }
            */

            // hide all the views but exoplayer view and video image placeholder view
            stepDetailDescriptionTextView.setVisibility(View.GONE);
            prevStepButton.setVisibility(View.GONE);
            nextStepButton.setVisibility(View.GONE);

            // change the width and height of both exoplayer view and video image placeholder view
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)
                    stepDetailSimpleExoPlayerView.getLayoutParams();
            params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            stepDetailSimpleExoPlayerView.setLayoutParams(params);
            stepDetailVideoPlaceholderImageView.setLayoutParams(params);

            // set the video url for video playback, or the imageview in case there is no video url
            if (!(step.getVideoURL().equals(""))) {
                stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
                stepDetailVideoPlaceholderImageView.setVisibility(View.INVISIBLE);
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else {
                stepDetailSimpleExoPlayerView.setVisibility(View.INVISIBLE);
                stepDetailVideoPlaceholderImageView.setImageResource(R.drawable.recipestepplaceholder_black);
                stepDetailVideoPlaceholderImageView.setVisibility(View.VISIBLE);
                Log.v(LOG_TAG, "No video url available.");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mSimpleExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            stepDetailSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "CookMe");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }
}

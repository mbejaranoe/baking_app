package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private SimpleExoPlayer mSimpleExoPlayer;
    private TextView stepDetailDescriptionTextView;
    private Button prevStepButton;
    private Button nextStepButton;
    private Step[] mSteps;
    private int mStepIndex;
    private int mRecipeId;
    private final int RECIPE_ID_DEFAULT_VALUE = -1;
    private final int STEP_INDEX_DEFAULT_VALUE = -1;
    private Cursor mCursor;

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

        stepDetailSimpleExoPlayerView = (SimpleExoPlayerView) rootView
                .findViewById(R.id.step_detail_simple_exoplayer_view);
        stepDetailDescriptionTextView = (TextView) rootView
                .findViewById(R.id.step_detail_description_text_view);
        prevStepButton = (Button) rootView.findViewById(R.id.prev_step_button);
        nextStepButton = (Button) rootView.findViewById(R.id.next_step_button);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("_id") && intent.hasExtra("stepIndex")) {
            // get the recipe _id
            mRecipeId = intent.getIntExtra("_id", RECIPE_ID_DEFAULT_VALUE);
            Log.v(LOG_TAG, " intent recipe_id: " + mRecipeId);
            if (mRecipeId == RECIPE_ID_DEFAULT_VALUE) {
                Log.v(LOG_TAG, "Error retrieving recipe_id from the intent. _id: " + mRecipeId);
                return rootView;
            }
            // query the content provider to get the recipe
            mCursor = getContext().getContentResolver().query(
                    RecipeEntry.CONTENT_URI,
                    null,
                    "_id=?",
                    new String[]{String.valueOf(mRecipeId)},
                    null);

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
            // get the step index
            mStepIndex = intent.getIntExtra("stepIndex", STEP_INDEX_DEFAULT_VALUE);
            Log.v(LOG_TAG, " intent step index: " + mStepIndex);
            if (mStepIndex == STEP_INDEX_DEFAULT_VALUE){
                Log.v(LOG_TAG, "Error retrieving step index from the intent. stepIndex: " + mStepIndex);
                return rootView;
            }
            // get the step
            Step step = mSteps[mStepIndex];
            // populate description view
            if (step.getDescription().equals("")) {
                stepDetailDescriptionTextView.setText(getResources()
                        .getString(R.string.no_step_description_message));
            } else {
                stepDetailDescriptionTextView.setText(step.getDescription());
            }
            // populate the thumbnail view
            Bitmap artwork = null;
            if (step.getThumbnailURL().equals("") || (NetworkUtils.getImageFromURL(step.getThumbnailURL()) == null)) {
                artwork = BitmapFactory.decodeResource(getResources(),R.drawable.recipestepplaceholder_black);
            } else {
                artwork = NetworkUtils.getImageFromURL(step.getThumbnailURL());
            }

            stepDetailSimpleExoPlayerView.setDefaultArtwork(artwork);

            if (!(step.getVideoURL().equals(""))) {
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else {
                Log.v(LOG_TAG, "No video url available.");
            }
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    /*
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }
    */

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

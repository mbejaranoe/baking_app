package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by Manolo on 10/10/2017.
 * Fragment to show the step details
 */

public class StepDetailFragment extends Fragment {

    private final String LOG_TAG = "StepDetailFragment";

    private SimpleExoPlayerView stepDetailSimpleExoPlayerView;
    private TextView stepDetailDescriptionTextView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Button[] mButtons;

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

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("description")){
            stepDetailDescriptionTextView.setText(intent.getStringExtra("description"));
        } else {
            stepDetailDescriptionTextView.setText(getResources().getString(R.string.no_step_description_message));
        }

        Bitmap artwork = null;
        if (intent.hasExtra("thumbnailURL")) {
            if (intent.getStringExtra("thumbnailURL").length() == 0) {
                artwork = BitmapFactory.decodeResource(getResources(),R.drawable.recipestepplaceholder_black);
            } else {
                artwork = NetworkUtils.getImageFromURL(intent.getStringExtra("thumbnailURL"));
            }
        }
        stepDetailSimpleExoPlayerView.setDefaultArtwork(artwork);

        if (intent.hasExtra("videoURL")){
            if (intent.getStringExtra("videoURL").length() != 0) {
                initializePlayer(Uri.parse(intent.getStringExtra("videoURL")));
            }
        }

        /*
        intent.putExtra("shortDescription", mSteps[stepIndex].getShortDescription());
        intent.putExtra("videoURL", mSteps[stepIndex].getVideoURL());
         */

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
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }
}

package com.example.android.mbejaranoe.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.mbejaranoe.bakingapp.data.RecipeContract.RecipeEntry;
import com.example.android.mbejaranoe.bakingapp.data.Step;
import com.example.android.mbejaranoe.bakingapp.utilities.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

/**
 * Created by Manolo on 10/10/2017.
 * Fragment to show the step details
 */

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener{

    private final String LOG_TAG = StepDetailFragment.class.getSimpleName();

    private final String STEP_INDEX_KEY = "stepIndex";
    private final String RECIPE_ID_KEY = "recipe_Id";
    private final String SHOULD_AUTO_PLAY_KEY = "shouldAutoPlay";
    private final String RESUME_POSITION_KEY = "resumePosition";

    @BindView(R.id.step_detail_simple_exoplayer_view) SimpleExoPlayerView stepDetailSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    @BindView(R.id.step_detail_description_text_view) TextView stepDetailDescriptionTextView;
    @BindView(R.id.nav_buttons_linear_layout) LinearLayout navButtonsLinearLayout;
    @BindView(R.id.prev_step_button) Button prevStepButton;
    @BindView(R.id.next_step_button) Button nextStepButton;
    private Unbinder unbinder;

    private Step[] mSteps;
    private int mStepIndex;
    private int mRecipe_Id;
    private Cursor mCursor;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean shouldAutoPlay;
    private long resumePosition;

    // Constructor
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (getContext().getResources().getConfiguration().smallestScreenWidthDp >= 600) { // tablet mode
            if (savedInstanceState == null) {
                Bundle args = getArguments();
                mStepIndex = args.getInt(STEP_INDEX_KEY);
                mRecipe_Id = args.getInt(RECIPE_ID_KEY);
                shouldAutoPlay = false;
                resumePosition = 0;
                Log.v(LOG_TAG, "onCreateView: tablet mode and savedInstanceState NULL");
            } else {
                mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
                mRecipe_Id = savedInstanceState.getInt(RECIPE_ID_KEY);
                shouldAutoPlay = savedInstanceState.getBoolean(SHOULD_AUTO_PLAY_KEY);
                resumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY);
                Log.v(LOG_TAG, "onCreateView: tablet mode and savedInstanceState NOT NULL");
            }
        } else { // phone mode
            if (getArguments() == null) {
                Intent intent = getActivity().getIntent();

                mStepIndex = intent.getExtras().getInt(STEP_INDEX_KEY);
                mRecipe_Id = intent.getExtras().getInt(RECIPE_ID_KEY);
                shouldAutoPlay = false;
                resumePosition = 0;
                Log.v(LOG_TAG, "onCreateView: phone mode and savedInstanceState NULL");
            } else {
                if (savedInstanceState == null) {
                    mStepIndex = getArguments().getInt(STEP_INDEX_KEY);
                    mRecipe_Id = getArguments().getInt(RECIPE_ID_KEY);
                    shouldAutoPlay = getArguments().getBoolean(SHOULD_AUTO_PLAY_KEY);
                    resumePosition = getArguments().getLong(RESUME_POSITION_KEY);
                    Log.v(LOG_TAG, "onCreateView: phone mode and savedInstanceState NULL");
                } else {
                    mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
                    mRecipe_Id = savedInstanceState.getInt(RECIPE_ID_KEY);
                    shouldAutoPlay = savedInstanceState.getBoolean(SHOULD_AUTO_PLAY_KEY);
                    resumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY);
                    Log.v(LOG_TAG, "onCreateView: phone mode and savedInstanceState NOT NULL");
                }
            }
        }

        updateStepDetails();

        return rootView;
    }

    public int getRecipeId(){
        return mRecipe_Id;
    }

    public int getStepIndex(){
        return mStepIndex;
    }

    public void updateStepDetails(){

        // query the content provider using the recipe_Id to get the recipe row
        mCursor = getContext().getContentResolver().query(
                RecipeEntry.CONTENT_URI,
                null,
                RecipeEntry.COLUMN_RECIPE_ID + "=?",
                new String[]{String.valueOf(mRecipe_Id)},
                null);

        // get the steps string and parse it to get a Step[]
        if (mCursor.moveToFirst()){

            Log.v(LOG_TAG, "updateStepDetails - Cursor.moveToFirst = TRUE");

            // get the json string with the steps details
            String stringSteps = mCursor.getString(mCursor.getColumnIndex(RecipeEntry.COLUMN_STEPS));
            JSONArray stepsJsonArray = null;
            try {
                stepsJsonArray = new JSONArray(stringSteps);
            } catch (JSONException e){
                Log.e(LOG_TAG, "updateStepDetails - Error parsing steps JSONArray: " + stringSteps);
            }

            Log.v(LOG_TAG, "updateStepDetails - Number of steps after parsing: " + stepsJsonArray.length());

            // parse the json string into a Step[]
            mSteps = new Step[stepsJsonArray.length()];

            Log.v(LOG_TAG, "updateStepDetails - mSteps array length: " + mSteps.length);

            for (int i = 0; i < stepsJsonArray.length(); i++){
                try {
                    mSteps[i] = new Step(stepsJsonArray.getJSONObject(i));
                    Log.v(LOG_TAG, "updateStepDetails - Step[" + i + "] creation successful");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "updateStepDetails - Error parsing JSONObject step.");
                }
            }
        }

        if (mSteps == null) {
            Log.v(LOG_TAG, "updateStepDetails - mSteps array is NULL");
        } else {
            Log.v(LOG_TAG, "updateStepDetails - mSteps array is NOT NULL");
        }
        // get the appropriate step
        Step step = mSteps[mStepIndex];

        // get the screen orientation
        Configuration configuration = getActivity().getResources().getConfiguration();

        // get the smallestScreenWidth
        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;

        // populate the thumbnail view
        stepDetailSimpleExoPlayerView.setUseArtwork(true);
        Bitmap artwork = null;
        if (step.getThumbnailURL().equals("") || (NetworkUtils.getImageFromURL(step.getThumbnailURL()) == null)) {
            artwork = BitmapFactory.decodeResource(getResources(), R.drawable.recipestepplaceholder_black);
        } else {
            artwork = NetworkUtils.getImageFromURL(step.getThumbnailURL());
        }
        stepDetailSimpleExoPlayerView.setDefaultArtwork(artwork);

        if (smallestScreenWidthDp < 600) { // phone mode
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) { // portrait mode

                // set the title for the parent activity
                getActivity().setTitle(step.getShortDescription());

                // populate description view
                stepDetailDescriptionTextView.setVisibility(View.VISIBLE);
                if (step.getDescription().equals("")) {
                    stepDetailDescriptionTextView.setText(getResources()
                            .getString(R.string.no_step_description_message));
                } else {
                    stepDetailDescriptionTextView.setText(step.getDescription());
                }

                // set the video url for video playback, or the imageview in case there is no video url
                releasePlayer();
                if (!(step.getVideoURL().equals(""))) {
                    stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
                    // Initialize the Media Session
                    initializeMediaSession();
                    // Initialize the Player
                    initializePlayer(Uri.parse(step.getVideoURL()));
                } else {
                    stepDetailSimpleExoPlayerView.setVisibility(View.GONE);
                    Log.v(LOG_TAG, "No video url available.");
                }

                // show nav buttons
                navButtonsLinearLayout.setVisibility(View.VISIBLE);
                prevStepButton.setVisibility(View.VISIBLE);
                nextStepButton.setVisibility(View.VISIBLE);

                // enable/disable nav button when on the first/last step
                if (mStepIndex == 0) {
                    prevStepButton.setEnabled(false);
                } else if (mStepIndex == mSteps.length - 1) {
                    nextStepButton.setEnabled(false);
                } else {
                    prevStepButton.setEnabled(true);
                    nextStepButton.setEnabled(true);
                }

            } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { // landscape mode

                // hide all the views but exoplayer view or video image placeholder view
                navButtonsLinearLayout.setVisibility(View.GONE);
                prevStepButton.setVisibility(View.GONE);
                nextStepButton.setVisibility(View.GONE);

                // change the width and height of both exoplayer view and video image placeholder view
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                        stepDetailSimpleExoPlayerView.getLayoutParams();
                params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                stepDetailSimpleExoPlayerView.setLayoutParams(params);

                // set the video url for video playback, or the imageview in case there is no video url
                if (!(step.getVideoURL().equals(""))) {
                    stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
                    initializeMediaSession();
                    initializePlayer(Uri.parse(step.getVideoURL()));
                } else {
                    stepDetailDescriptionTextView.setVisibility(View.VISIBLE);
                    if (step.getDescription().equals("")) {
                        stepDetailDescriptionTextView.setText(getResources()
                                .getString(R.string.no_step_description_message));
                    } else {
                        stepDetailDescriptionTextView.setText(step.getDescription());
                    }

                    stepDetailSimpleExoPlayerView.setVisibility(View.GONE);
                    Log.v(LOG_TAG, "No video url available.");
                }
            }
        } else { // tablet mode
            // hide nav buttons
            navButtonsLinearLayout.setVisibility(View.GONE);
            prevStepButton.setVisibility(View.GONE);
            nextStepButton.setVisibility(View.GONE);

            // populate description view
            stepDetailDescriptionTextView.setVisibility(View.VISIBLE);
            if (step.getDescription().equals("")) {
                stepDetailDescriptionTextView.setText(getResources()
                        .getString(R.string.no_step_description_message));
            } else {
                stepDetailDescriptionTextView.setText(step.getDescription());
            }

            // set the video url for video playback, or the imageview in case there is no video url
            releasePlayer();
            if (!(step.getVideoURL().equals(""))) {
                stepDetailSimpleExoPlayerView.setVisibility(View.VISIBLE);
                // Initialize the Media Session
                initializeMediaSession();
                // Initialize the Player
                initializePlayer(Uri.parse(step.getVideoURL()));
            } else {
                stepDetailSimpleExoPlayerView.setVisibility(View.GONE);
                Log.v(LOG_TAG, "No video url available.");
            }
        }
        mCursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();

        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
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
            mSimpleExoPlayer.addListener(this);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "CookMe");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(shouldAutoPlay);
            mSimpleExoPlayer.seekTo(resumePosition);
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

    /**
     * Initialize MediaSession.
     */
    private void initializeMediaSession(){

        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
        
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mSimpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mSimpleExoPlayer.seekTo(0);
        }
    }

    // ExoPlayer.EventListener required methods
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.v(LOG_TAG, "onSaveInstanceState");

        outState.putInt(STEP_INDEX_KEY, mStepIndex);
        outState.putInt(RECIPE_ID_KEY, mRecipe_Id);
        if (mSimpleExoPlayer != null) {
            shouldAutoPlay = mSimpleExoPlayer.getPlayWhenReady();
            resumePosition = mSimpleExoPlayer.getCurrentPosition();
        } else {
            shouldAutoPlay = false;
            resumePosition = 0;
        }
        outState.putBoolean(SHOULD_AUTO_PLAY_KEY, shouldAutoPlay);
        outState.putLong(RESUME_POSITION_KEY, resumePosition);
    }
}

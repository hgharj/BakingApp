package com.example.android.bakingapp.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import android.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements Player.EventListener {
    private static final String STEP_DATA="pass-step";
    private static final String STEP_SAVED_DATA="step-saved-data";
    private static final String STATE_PLAYER_FULLSCREEN="state-player-fullscreen";
    private static final String LOG_TAG=StepFragment.class.getName();
    private static final String TAG=StepFragment.class.getSimpleName();
    @BindView(R.id.step_title_tv)
    TextView mStepTitleTv;
    @BindView(R.id.step_desc_tv)
    TextView mStepDescTv;
    @BindView(R.id.step_player)
    PlayerView mPlayerView;
//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private OnDataPass dataPasser;
    private Context mContext;
    private SimpleExoPlayer mPlayer;
    private Step mStep;
    private ActionBar mActionBar;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;
    @BindView(R.id.media_container)
    FrameLayout mMediaContainer;
    @BindView(R.id.step_card)
    MaterialCardView mMcw;

    public interface OnDataPass {
        void onDataPass(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        StepSlidePagerActivity stepSlidePagerActivity = (StepSlidePagerActivity)context;
        mActionBar = stepSlidePagerActivity.getActionBar();
        if (dataPasser != null){
            dataPasser = (OnDataPass)context;}
    }

    public StepFragment() {
        super();
    }

    public void passData(Step step){
        dataPasser.onDataPass(step);
    }

    public static StepFragment newInstance() {
        Bundle args = new Bundle();

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this,rootView);

//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState!=null) {
            mStep = savedInstanceState.getParcelable(STEP_SAVED_DATA);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        } else {
            mStep = getArguments().getParcelable(STEP_DATA);
        }

        mStepTitleTv.setText(mStep.getShortDescription());
        mStepDescTv.setText(mStep.getDescription());

        boolean videoAvailable = false;
        String videoUrl="";
        if(!mStep.getVideoUrl().equals("")){
            videoUrl=mStep.getVideoUrl();
            videoAvailable=true;
        } else if(!mStep.getThumbnailUrl().equals("")){
            videoUrl=mStep.getThumbnailUrl();
            videoAvailable=true;
        }

        if(videoAvailable) {
            mContext = getContext();
            initializeMediaSession();
            Uri mp4VideoUri = Uri.parse(videoUrl);
            initializePlayer(mp4VideoUri);
        } else {
            int height = mMediaContainer.getHeight();
            int width = mMediaContainer.getWidth();
            if (mPlayer!=null) {
                mPlayer.getPlaybackState();
                mPlayer.clearVideoSurface();

            }

            if(mMcw!=null){
                int mcwHeight = mMcw.getHeight();
                int mcwWidth = mMcw.getWidth();
            }


        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_SAVED_DATA,mStep);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mMediaContainer.addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));
    }

    private void initializePlayer(Uri mp4VideoUri){
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext);

        mPlayerView.setPlayer(player);

        String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                userAgent);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(mp4VideoUri);
//        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, new DefaultDataSourceFactory(
//                mContext, userAgent), new DefaultExtractorsFactory(), null, null);
        // Prepare the player with the source.
        player.prepare(videoSource);

        Player.EventListener eventListener = new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(LOG_TAG,error.toString());
                player.release();
            }
        };
        // Add a listener to receive events from the player.
        player.addListener(eventListener);

        if (mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            initFullscreenDialog();
            openFullscreenDialog();
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
//                    mPlayerView.getLayoutParams();
//            params.width = params.MATCH_PARENT;
//            params.height = params.MATCH_PARENT;
//            mPlayerView.setLayoutParams(params);
//            StepFragment.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN || View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(mContext, TAG);

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
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
//        mNotificationManager.cancelAll();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

// ExoPlayer Event Listeners

//    @Override
//    public void onTimelineChanged(Timeline timeline, Object manifest) {
//    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
//        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        String s="";
    }

//    @Override
//    public void onPositionDiscontinuity() {
//    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);

//            if(getSupportActionBar()!=null) {
//                getSupportActionBar().hide();
//            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            mPlayerView.setLayoutParams(params);

            //To show the action bar
//            if(getSupportActionBar()!=null) {
//                getSupportActionBar().show();
//            }
        }
    }
}

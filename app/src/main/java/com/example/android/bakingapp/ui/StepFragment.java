package com.example.android.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements Player.EventListener {
    private static final String STEP_DATA="pass-step";
    private static final String LOG_TAG=StepFragment.class.getName();
    private static final String TAG=StepFragment.class.getSimpleName();
    @BindView(R.id.step_title_tv)
    TextView mStepTitleTv;
    @BindView(R.id.step_desc_tv)
    TextView mStepDescTv;
    @BindView(R.id.step_player)
    PlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private OnDataPass dataPasser;
    private Context mContext;
    private SimpleExoPlayer mPlayer;

    public interface OnDataPass {
        void onDataPass(Step step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        View rootView = inflater.inflate(R.layout.step_screen_slide, container, false);
        ButterKnife.bind(this,rootView);

        Step step = getArguments().getParcelable(STEP_DATA);

        mStepTitleTv.setText(step.getShortDescription());
        mStepDescTv.setText(step.getDescription());

        boolean videoAvailable = false;
        String videoUrl="";
        if(!step.getVideoUrl().equals("")){
            videoUrl=step.getVideoUrl();
            videoAvailable=true;
        } else if(!step.getThumbnailUrl().equals("")){
            videoUrl=step.getThumbnailUrl();
            videoAvailable=true;
        }

        if(videoAvailable) {
            initializeMediaSession();
            Uri mp4VideoUri = Uri.parse(videoUrl);
            initializePlayer(mp4VideoUri);
        }
        return rootView;
    }

    private void initializePlayer(Uri mp4VideoUri){

        mContext = getContext();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext);

        mPlayerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, getString(R.string.app_name)));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
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
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
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
}

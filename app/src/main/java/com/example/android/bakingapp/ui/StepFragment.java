package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.utils.RecipeUtils;
import com.example.android.bakingapp.utils.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepFragment extends Fragment implements Player.EventListener {
    private static final String STEP_DATA="pass-step";
    private static final String STEP_SAVED_DATA="step-saved-data";
    private static final String STATE_PLAYER_FULLSCREEN="state-player-fullscreen";
    private static final String PLAYER_POSITION="player-position";
    private static final String LOG_TAG=StepFragment.class.getName();
    private static final String TAG=StepFragment.class.getSimpleName();
    @BindView(R.id.step_title_tv)
    TextView mStepTitleTv;
    @BindView(R.id.step_desc_tv)
    TextView mStepDescTv;
    @BindView(R.id.step_player)
    PlayerView mSimpleExoPlayerView;
    @BindView(R.id.placeholder_image)
    ImageView mPlaceholderImage;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
//    private OnDataPass dataPasser;
    private Context mContext;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Step mStep;
    private boolean mExoPlayerFullscreen = false;
    @BindView(R.id.media_container)
    FrameLayout mMediaContainer;
    @BindView(R.id.step_card)
    MaterialCardView mMcw;
    Uri mp4VideoUri;
    long mPlayerPosition;
    private static final String VIDEO_EXT=".mp4";
    Unbinder mUnbinder;

//    public interface OnDataPass {
//        void onDataPass(Step step);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (dataPasser != null){
//            dataPasser = (OnDataPass)context;}
    }

    public StepFragment() {
        super();
    }

//    public void passData(Step step){
//        dataPasser.onDataPass(step);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mUnbinder = ButterKnife.bind(this,rootView);
        mContext = getContext();

        if(savedInstanceState!=null) {
            mStep = savedInstanceState.getParcelable(STEP_SAVED_DATA);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
        } else {
            mStep = getArguments().getParcelable(STEP_DATA);
        }

        mStepTitleTv.setText(mStep.getShortDescription());
        mStepDescTv.setText(mStep.getDescription());

        String thumbnailUrl;
        String videoUrl = RecipeUtils.getVideoUrl(mStep);
        if(videoUrl == null) {
            thumbnailUrl = mStep.getThumbnailUrl();
            if (thumbnailUrl != null && thumbnailUrl.contains(RecipeUtils.VIDEO_EXT)) {
                Picasso.with(mContext)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.video)
                        .error(R.drawable.video)
                        .into(mPlaceholderImage);
            }
        }

        if(videoUrl != null) {
            setPlayerVisibility(mSimpleExoPlayerView,true);

            mp4VideoUri = Uri.parse(videoUrl);
            mPlaceholderImage.setVisibility(View.GONE);
            initializePlayer(mp4VideoUri);
//            ImageButton fullScreenButton = rootView.findViewById(R.id.exo_fullscreen_button);
//            fullScreenButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    initFullscreenDialog();
//                    openFullscreenDialog();
//                }
//            });
        } else {
            mPlaceholderImage.setVisibility(View.VISIBLE);
            setPlayerVisibility(mSimpleExoPlayerView,false);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateCurrentPlayerPosition();
        outState.putParcelable(STEP_SAVED_DATA,mStep);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putLong(PLAYER_POSITION,mPlayerPosition);
    }

    private void updateCurrentPlayerPosition() {
        if (mSimpleExoPlayer != null) {
            mPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
        }
    }

    private void setPlayerVisibility(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void initializePlayer(Uri mp4VideoUri){
        initializeMediaSession();

        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());

        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        mSimpleExoPlayer.addListener(this);

        String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                userAgent);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(mp4VideoUri);
        // Prepare the player with the source.
        mSimpleExoPlayer.prepare(videoSource);

        if (mContext.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            hideUI();
            mSimpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

    private void hideUI() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
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

//        PlayerControlView pcw = new PlayerControlView(mContext);
//        pcw.pla

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
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
        });

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

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

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mSimpleExoPlayer != null) {
                initializePlayer(mp4VideoUri);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mSimpleExoPlayer != null){
            updateCurrentPlayerPosition();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mSimpleExoPlayer != null){
            mSimpleExoPlayer.setPlayWhenReady(false);
            mSimpleExoPlayer.seekTo(mPlayerPosition);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mSimpleExoPlayer != null){
            updateCurrentPlayerPosition();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    // ExoPlayer Event Listeners

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
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }
}

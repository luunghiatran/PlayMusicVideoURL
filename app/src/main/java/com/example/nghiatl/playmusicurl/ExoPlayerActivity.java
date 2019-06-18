package com.example.nghiatl.playmusicurl;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerActivity extends AppCompatActivity {

    private TextView mTextViewSongName, mTextView_playTime, mTextView_endTime;
    private SeekBar mSeekBar_play;
    private Button mButton_play, mButton_pause;
    private PlayerView mPlayerView_video;

    private int mLengthOfAudio;
    SimpleExoPlayer mExoPlayer;
    String mUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

    private final Handler mPlayHandler = new Handler();
    private final Runnable r = new Runnable()
    {
        @Override
        public void run() {
            updateSeekProgress();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        findViews();
        initVars();
        setEvents();
    }

    private void findViews() {
        mTextViewSongName = (TextView)findViewById(R.id.textView_songName);
        mTextView_playTime =(TextView)findViewById(R.id.textView_playTime);
        mTextView_endTime =(TextView)findViewById(R.id.textView_endTime);
        mSeekBar_play = (SeekBar)findViewById(R.id.seekBar_play);
        mButton_play = (Button)findViewById(R.id.button_play);
        mButton_pause = (Button)findViewById(R.id.button_pause);
        mPlayerView_video = findViewById(R.id.player_view);
    }

    private void initVars() {
        // ===========INIT MEDIA PLAYER ==============
        try {
            Uri mp4VideoUri = Uri.parse(mUrl);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this); // Creating the player
            mPlayerView_video.setPlayer(mExoPlayer); //Attaching the player to a view

            // Preparing the player
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "yourApplicationName"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEvents() {
        mButton_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClick();
            }
        });
        mButton_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseClick();
            }
        });

        mSeekBar_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return seekBarTouch((SeekBar) v);
            }
        });

        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                showToast(error.getMessage());
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }


    private void mediaPlayerBufferUpdate(MediaPlayer mediaPlayer, int percent) {
        mSeekBar_play.setSecondaryProgress(percent);
    }

    private boolean seekBarTouch(SeekBar v) {
        /*if (mMediaPlayer.isPlaying())
        {
            SeekBar tmpSeekBar = v;
            mMediaPlayer.seekTo((mLengthOfAudio / 100) * tmpSeekBar.getProgress() );
        }*/
        return false;
    }

    private void pauseClick() {
        updateSeekProgress();
    }

    private void playClick() {
        updateSeekProgress();
    }

    private void updateSeekProgress()
    {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

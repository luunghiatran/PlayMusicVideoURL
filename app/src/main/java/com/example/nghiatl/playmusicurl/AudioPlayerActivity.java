package com.example.nghiatl.playmusicurl;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity {

    private TextView mTextViewSongName, mTextView_playTime, mTextView_endTime;
    private SeekBar mSeekBar_play;
    private Button mButton_play, mButton_pause;

    private int mLengthOfAudio;
    private MediaPlayer mMediaPlayer;
    //String mUrl = "https://www.mfiles.co.uk/mp3-downloads/haydn-symphony88-1.mp3";
    String mUrl = "https://static.oneway.vn/Song_Dung_Muc_Dich_Ngay_1.mp3";

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
        setContentView(R.layout.activity_audio_player);

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
    }

    private void initVars() {
        // ===========INIT MEDIA PLAYER ==============
        // ========== Play via file ========
        /*mMediaPlayer = MediaPlayer.create(this, R.raw.way_back_home);
        mMediaPlayer.start();*/


        // ========== Play Via URI =========
        /*Uri myUri = ....; // initialize Uri here
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(getApplicationContext(), myUri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();*/


        // ======= Play via URL ==========
        try {
            mMediaPlayer = new MediaPlayer();
            //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mMediaPlayer.setDataSource(mUrl);

            mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            //mMediaPlayer.start();
            mLengthOfAudio = mMediaPlayer.getDuration();

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

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mediaPlayerBufferUpdate(mp, percent);
            }
        });
    }


    private void mediaPlayerBufferUpdate(MediaPlayer mediaPlayer, int percent) {
        mSeekBar_play.setSecondaryProgress(percent);
    }

    private boolean seekBarTouch(SeekBar v) {
        if (mMediaPlayer.isPlaying())
        {
            SeekBar tmpSeekBar = v;
            mMediaPlayer.seekTo((mLengthOfAudio / 100) * tmpSeekBar.getProgress() );
        }
        return false;
    }

    private void pauseClick() {
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        updateSeekProgress();
    }

    private void playClick() {
        if (!mMediaPlayer.isPlaying()) {
            try {

                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mUrl);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

                mLengthOfAudio = mMediaPlayer.getDuration();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        updateSeekProgress();
    }

    private void updateSeekProgress()
    {
        if (mMediaPlayer.isPlaying())
        {
            mSeekBar_play.setProgress((int)(((float) mMediaPlayer.getCurrentPosition() / mLengthOfAudio) * 100));
            mPlayHandler.postDelayed(r, 1000);

            mTextView_endTime.setText(mLengthOfAudio +"");
            mTextView_playTime.setText(mMediaPlayer.getCurrentPosition()+"");
        }
    }
}

package com.example.nghiatl.playmusicurl;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class ExoAudioActivity extends AppCompatActivity {

    private TextView mTextViewSongName, mTextView_playTime, mTextView_endTime;
    private SeekBar mSeekBar_play;
    private Button mButton_play, mButton_pause;

    private int mLengthOfAudio;
    SimpleExoPlayer player;
    String mUrl = "https://www.mfiles.co.uk/mp3-downloads/haydn-symphony88-1.mp3";

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
        setContentView(R.layout.activity_exo_audio);


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
        initializePlayer();
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                this,
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //playerView.setPlayer(player);

        //player.setPlayWhenReady(playWhenReady);
        //player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(mUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
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

        player.addListener(new Player.EventListener() {
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
                mButton_play.setText(error.getMessage());
                Log.e("Nghia", error.getMessage());
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

        player.setPlayWhenReady(false);
    }

    private void playClick() {
        updateSeekProgress();

        player.setPlayWhenReady(true);
    }

    private void updateSeekProgress()
    {

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public static boolean writeStream(InputStream input, OutputStream output){
        try {
            // Chuyển byte từ Input => Output
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            input.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

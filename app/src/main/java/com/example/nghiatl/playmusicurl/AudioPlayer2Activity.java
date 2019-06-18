package com.example.nghiatl.playmusicurl;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AudioPlayer2Activity extends AppCompatActivity {

    private TextView mTextViewSongName, mTextView_playTime, mTextView_endTime;
    private SeekBar mSeekBar_play;
    private Button mButton_play;


    private MediaPlayer mMediaPlayer;
    String mUrl = "https://www.mfiles.co.uk/mp3-downloads/haydn-symphony88-1.mp3";
    private int mLengthOfAudio;
    private int mResumePosition;


    private Handler updateSeekBarHandler = new Handler();
    private Runnable r = new Runnable()
    {
        @Override
        public void run() {
            updateSeekBarProgress();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autio_player_2);

        findViews();
        initPlayer();
    }

    private void findViews() {
        mTextViewSongName = (TextView)findViewById(R.id.textView_songName);
        mTextView_playTime =(TextView)findViewById(R.id.textView_playTime);
        mTextView_endTime =(TextView)findViewById(R.id.textView_endTime);
        mSeekBar_play = (SeekBar)findViewById(R.id.seekBar_play);
        mButton_play = (Button)findViewById(R.id.button_playPause);
    }

    private String formatTime(int millis) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }



    private void initPlayer() {
        mButton_play.setEnabled(false);

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //mMediaPlayer.start();
                    mButton_play.setEnabled(true);
                    mLengthOfAudio = mp.getDuration();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("nghia", String.format("error(%s, %s)", what, extra));
                    mButton_play.setText(String.format("error(%s, %s)", what, extra));
                    return false;
                }
            });
            mMediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mSeekBar_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return seekBarTouch((SeekBar) v);
            }
        });
    }


    private boolean seekBarTouch(SeekBar seekBar) {
        mResumePosition = (mMediaPlayer.getDuration() / 100) * seekBar.getProgress();
        updateUIResumePosition(mResumePosition);

        if (mMediaPlayer.isPlaying())
            mMediaPlayer.seekTo(mResumePosition);

        return false;
    }

    public void onPlayPauseClick(View view) {
        if (mMediaPlayer.isPlaying()) {
            // Pause
            mMediaPlayer.pause();
            mResumePosition = mMediaPlayer.getCurrentPosition();
        } else {
            // Start / Resume
            mMediaPlayer.seekTo(mResumePosition);
            mMediaPlayer.start();
        }

        updateSeekBarProgress();
    }

    public void onNextClick(View view) {
        mMediaPlayer = MediaPlayer.create(this, R.raw.way_back_home);
        mLengthOfAudio = mMediaPlayer.getDuration();

        mResumePosition = 0;
        updateUIResumePosition(mResumePosition);
    }

    private void updateUIResumePosition(int resumePosition) {
        mSeekBar_play.setProgress((int)(((float) resumePosition / mLengthOfAudio) * 100));

        mTextView_playTime.setText(formatTime(resumePosition));
        mTextView_endTime.setText(formatTime(mLengthOfAudio) +"");
    }


    private void updateSeekBarProgress() {
        mResumePosition = mMediaPlayer.getCurrentPosition();
        updateUIResumePosition(mResumePosition);


        if (mMediaPlayer.isPlaying())
        {
            updateSeekBarHandler.postDelayed(r, 1000);
        }
    }


}

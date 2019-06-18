package com.example.nghiatl.playmusicurl;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    private TextView mTextViewSongName, mTextView_playTime, mTextView_endTime;
    private SeekBar mSeekBar_play;
    private Button mButton_play, mButton_pause;
    private VideoView videoView;

    //String mUrl = "http://gslb.miaopai.com/stream/oxX3t3Vm5XPHKUeTS-zbXA__.mp4";
    //String mUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    String mUrl = "http://oneway.vn/wp-content/uploads/the-bible/Tap-5-Sinh-Ton.mp4";
    private int mLengthOfAudio;

    private final Handler handler = new Handler();
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
        setContentView(R.layout.activity_video_player);


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
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void initVars() {
        // // ======= Play via URL ==========
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Uri video = Uri.parse(mUrl);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // mp.setLooping(true);
                mLengthOfAudio = mp.getDuration();
                // videoView.start();
            }
        });
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

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        mediaPlayerBufferUpdate(mp, percent);
                    }
                });
            }
        });
    }


    private void mediaPlayerBufferUpdate(MediaPlayer mediaPlayer, int percent) {
        Log.e(TAG, "Loading: " + percent);
        mSeekBar_play.setSecondaryProgress(percent);
    }

    private boolean seekBarTouch(SeekBar v) {
        if (videoView.isPlaying())
        {
            SeekBar tmpSeekBar = v;
            videoView.seekTo((mLengthOfAudio / 100) * tmpSeekBar.getProgress() );
        }
        return false;
    }

    private void pauseClick() {
        if (videoView.isPlaying())
            videoView.pause();
        updateSeekProgress();
    }

    private void playClick() {
        if (!videoView.isPlaying())
            videoView.start();
        updateSeekProgress();
    }

    private void updateSeekProgress()
    {
        if (videoView.isPlaying())
        {
            mSeekBar_play.setProgress((int)(((float)videoView.getCurrentPosition() / mLengthOfAudio) * 100));
            handler.postDelayed(r, 1000);

            mTextView_endTime.setText(mLengthOfAudio +"");
            mTextView_playTime.setText(videoView.getCurrentPosition()+"");
        }
    }

}

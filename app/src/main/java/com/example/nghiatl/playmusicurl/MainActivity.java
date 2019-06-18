package com.example.nghiatl.playmusicurl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nghiatl.playmusicurl.ExoPlayerYoutube.ExoPlayerYoutubeActivity;
import com.example.nghiatl.playmusicurl.YoutubePlayer.YoutubeActivity;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goVideoPlayer(View view) {
        startActivity(new Intent(this, VideoPlayerActivity.class));
    }

    public void goAudioPlayer(View view) {
        startActivity(new Intent(this, AudioPlayerActivity.class));
    }

    public void goAudioPlayer2(View view) {
        startActivity(new Intent(this, AudioPlayer2Activity.class));
    }

    public void goExoPlayer(View view) {
        startActivity(new Intent(this, ExoPlayerActivity.class));
    }

    public void goExoPlayerYoutube(View view) {
        startActivity(new Intent(this, ExoPlayerYoutubeActivity.class));
    }

    public void goYoutubePlayer(View view) {
        startActivity(new Intent(this, YoutubeActivity.class));
    }

    public void goExoAudio(View view) {
        startActivity(new Intent(this, ExoAudioActivity.class));
    }


    public void goExoStreamAudio(View view) {
        startActivity(new Intent(this, ExoStreamAudioActivity.class));
    }
}

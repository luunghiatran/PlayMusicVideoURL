package com.example.nghiatl.playmusicurl.YoutubePlayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nghiatl.playmusicurl.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        initYouTubePlayer();
    }

    private void initYouTubePlayer() {
        final YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_youTubePlayer);

        /*youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                        if (!wasRestored) {
                            // Can not Play if other View below
                            youTubePlayer.cueVideo("EUFRDX9eTso");  //Video Id
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.i("Detail", "Failed: $youTubeInitializationResult");
                    }

        });*/


        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeFailureRecoveryActivity(YoutubeActivity.this) {
            @Override
            protected YouTubePlayer.Provider getYouTubePlayerProvider() {
                return youTubePlayerFragment;
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    // Can not Play if other View below
                    youTubePlayer.cueVideo("EUFRDX9eTso");  //Video Id
                }
            }
        });
    }
}

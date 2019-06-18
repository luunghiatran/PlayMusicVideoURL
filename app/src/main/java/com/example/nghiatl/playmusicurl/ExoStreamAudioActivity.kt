package com.example.nghiatl.playmusicurl

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_exo_stream_audio.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.util.Util
import android.annotation.SuppressLint
import android.view.View
import com.google.android.exoplayer2.*



// https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
class ExoStreamAudioActivity : AppCompatActivity() {

    private var mPlayer: SimpleExoPlayer? = null
    private var mPlayWhenReady: Boolean = true
    private var mCurrentWindow: Int = 0
    private var mPlaybackPosition: Long = 0
    var mStreamURL = "http://210.211.116.233:8001/;"

    //#region Circle Life
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_stream_audio)

        button_playPause.setOnClickListener {
            mPlayer?.playWhenReady = isPlaying() != true
        }
    }

    private fun isPlaying(): Boolean {
        return (mPlayer != null
                && mPlayer?.playbackState != Player.STATE_ENDED
                && mPlayer?.playbackState != Player.STATE_IDLE
                && mPlayer?.playWhenReady == true)
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }
    //#endregion


    private fun initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                this,
                DefaultRenderersFactory(this),
                DefaultTrackSelector(), DefaultLoadControl())
        playerView.player = mPlayer

        mPlayer?.playWhenReady = mPlayWhenReady
        mPlayer?.seekTo(mCurrentWindow, mPlaybackPosition)

        val uri = Uri.parse(mStreamURL)
        val mediaSource = buildMediaSource(uri)
        mPlayer?.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }



    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer?.currentPosition ?: 0
            mCurrentWindow = mPlayer?.currentWindowIndex ?: 0
            mPlayWhenReady = mPlayer?.playWhenReady ?: true

            mPlayer?.release()
            mPlayer = null
        }
    }
}
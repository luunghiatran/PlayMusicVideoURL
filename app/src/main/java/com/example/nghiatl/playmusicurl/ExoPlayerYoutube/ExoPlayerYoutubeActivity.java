package com.example.nghiatl.playmusicurl.ExoPlayerYoutube;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.example.nghiatl.playmusicurl.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

/*import com.commit451.youtubeextractor.YouTubeExtraction;
import com.commit451.youtubeextractor.YouTubeExtractor;*/

public class ExoPlayerYoutubeActivity extends AppCompatActivity {

    // Replace video id with your video Id
    private String YOUTUBE_VIDEO_ID = "EUFRDX9eTso";
    //private String YOUTUBE_VIDEO_ID = "9d8wWcJLnFI";
    private String BASE_URL = "https://youtu.be/";
    private String mYoutubeLink = BASE_URL + YOUTUBE_VIDEO_ID;

    private TextView textViewError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_youtube);

        textViewError = findViewById(R.id.textView_error);

        extractYoutubeUrl();
        //extractYoutubeUrl2();
    }

    /*private void extractYoutubeUrl2() {
        YouTubeExtractor extractor = new YouTubeExtractor.Builder().build();
        extractor.extract(YOUTUBE_VIDEO_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<YouTubeExtraction>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(YouTubeExtraction youTubeExtraction) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        textViewError.setText(e.getMessage());
                    }
                });
    }*/

    private void extractYoutubeUrl() {
        YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                // get high solution url

                if (sparseArray != null) {
                    YtFile maxHeight = ExoPlayerManager.getMaxHeight(sparseArray);
                    Log.e("playing_video", maxHeight.getUrl());
                    playVideo(maxHeight.getUrl());
                } else {
                    Log.e("Nghia", "No File Response");

                    String[] links = extractLinks(mYoutubeLink);
                    Log.e("Nghia", links[0]);
                }
            }
        };
        mExtractor.extract(mYoutubeLink, true, true);
    }



    private void playVideo(String downloadUrl) {
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);

        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(this).playStream(downloadUrl);
    }



    protected String[] extractLinks(String result) {
        //The title of the downloaded file
        String title = "bla bla bla";
        // An array of strings that will hold the links (in case of error, it will hold 1 string)
        String[] temper = new String[1];

        try{
            // Extract the "url_encoded_fmt_stream_map" attr from the flash object
            Pattern p = Pattern.compile("url_encoded_fmt_stream_map(.*?);");
            Matcher m = p.matcher(result);
            List<String> matches = new ArrayList<String>();
            while(m.find()){
                matches.add(m.group().replace("url_encoded_fmt_stream_map=", "").replace(";", ""));
            }
            String[] streamMap = null;
            List<String> links = new ArrayList<String>();

            if(matches.get(0) != null &&  matches.get(0) != ""){
                // Split the "url_encoded_fmt_stream_map" into an array of strings that hold the link values
                streamMap = matches.get(0).split("%2C");
                for (int i = 0; i < streamMap.length; i++){
                    String url = "";
                    String sig = "";

                    //Using regex, we will get the video url.
                    Pattern p2 = Pattern.compile("url%3D(.*?)%26");
                    Matcher m2 = p2.matcher(streamMap[i]);
                    List<String> matches2 = new ArrayList<String>();
                    while(m2.find()){
                        // We don't need the "url=...&" part.
                        matches2.add(m2.group().substring(6, m2.group().length() - 3));
                    }
                    url = matches2.get(0);

                    //Using regex again, we will get the video signature.
                    p2 = Pattern.compile("sig%3D(.*?)%26");
                    m2 = p2.matcher(streamMap[i]);
                    matches2 = new ArrayList<String>();
                    while(m2.find()){
                        // We don't need the "sig=...&" part.
                        matches2.add(m2.group().substring(6, m2.group().length() - 3));
                    }
                    sig = matches2.get(0);

                    //Now lets add a link to our list. Link = url + sig + title.
                    links.add(URLDecoder.decode(URLDecoder.decode(url + "&signature=", "UTF-8") + sig + "%26title%3D" + URLDecoder.decode(title, "UTF-8"), "UTF-8"));
                }
            }
            else{
                links.add("No download Links");
            }
            //Now lets make temper point to a array that holds the list items (aka links).
            temper = links.toArray(new String[links.size()]);
        }
        catch(Exception ex){
            temper[0] = ex.getMessage();
        }
        // That's it! temper has direct download links from youtube!
        return temper;
    }
}



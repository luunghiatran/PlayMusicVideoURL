package com.example.nghiatl.playmusicurl.ExoPlayerYoutube;

public interface CallBacks {
    void callbackObserver(Object obj);

    public interface playerCallBack {
        void onItemClickOnItem(Integer albumId);

        void onPlayingEnd();
    }
}

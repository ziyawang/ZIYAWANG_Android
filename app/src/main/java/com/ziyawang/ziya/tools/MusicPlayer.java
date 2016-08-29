package com.ziyawang.ziya.tools;

/**
 * Created by 牛海丰 on 2016/7/27.
 */

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class MusicPlayer {
    private final static String TAG = "MusicPlayer";
    private static MediaPlayer mMediaPlayer;
    private Context mContext;

    public MusicPlayer(Context context){
        mContext = context;
    }

    public String playMicFile(File file){
        if (file!=null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            mMediaPlayer = MediaPlayer.create(mContext, uri);

            int duration = mMediaPlayer.getDuration();

            String s = MyTimeFormat.changeTimeMS(duration);
            Log.e(TAG + duration  , s ) ;
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    //TODO:finish
                    Log.i(TAG, "Finish");
                }
            });
            return s ;
        }
        return "" ;
    }

    public void stopPlayer(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
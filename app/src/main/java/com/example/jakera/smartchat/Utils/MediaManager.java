package com.example.jakera.smartchat.Utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by jakera on 18-2-2.
 */

public class MediaManager {
    private static MediaPlayer mMediaPlayer;
    private static boolean isPause;

    public static void playSound(String filePath,
                                 MediaPlayer.OnCompletionListener onCompletionListener){
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            //一定要加入这句才能够正常地录音
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void pause(){
        if (mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            isPause = true;
        }
    }
    public static void resume(){
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }
    public static void release(){
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}

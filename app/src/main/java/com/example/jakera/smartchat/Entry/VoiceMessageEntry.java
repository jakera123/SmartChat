package com.example.jakera.smartchat.Entry;

import android.graphics.Bitmap;

/**
 * Created by jakera on 18-2-2.
 */

public class VoiceMessageEntry extends BaseMessageEntry{

    float time;
    String filePath;
    public VoiceMessageEntry(float time, String filePath) {
        super();
        this.time = time;
        this.filePath = filePath;
    }
    public float getTime() {
        return time;
    }
    public void setTime(float time) {
        this.time = time;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

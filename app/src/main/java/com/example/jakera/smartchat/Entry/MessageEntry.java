package com.example.jakera.smartchat.Entry;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by jakera on 18-1-31.
 */

public class MessageEntry {

   private Bitmap portrait;
   private String title;
   private String content;
   private String time;

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getPortrait() {

        return portrait;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}

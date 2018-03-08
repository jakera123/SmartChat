package com.example.jakera.smartchat.Entry;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by jakera on 18-1-31.
 */

public class MessageEntry {

    private String username;
    private String nickname;
   private String content;
   private String time;




    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}

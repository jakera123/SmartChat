package com.example.jakera.smartchat.Entry;

import android.graphics.Bitmap;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatMessageEntry {

    public final static int SENDMESSAGE=0;
    public final static int RECEIVEMESSAGE=1;

    private Bitmap portrait;
    private String content;
    private int viewType=1;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

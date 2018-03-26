package com.example.jakera.smartchat.Entry;

import android.graphics.Bitmap;

/**
 * Created by jakera on 18-2-2.
 */

public class BaseMessageEntry {
    public final static int SENDMESSAGE=0;
    public final static int RECEIVEMESSAGE=1;
    private String userName;
    private int viewType=1;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

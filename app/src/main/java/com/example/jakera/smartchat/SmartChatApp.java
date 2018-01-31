package com.example.jakera.smartchat;

import android.app.Application;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by jakera on 18-1-24.
 */

public class SmartChatApp extends Application {
    private final String APPID="5a686b03";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("SmartChat","i am onCreate....");
        //讯飞语音初始化工作
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"="+APPID);
    }
}

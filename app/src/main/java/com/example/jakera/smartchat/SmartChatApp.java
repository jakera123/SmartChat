package com.example.jakera.smartchat;

import android.app.Application;
import android.util.Log;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Utils.SpeechSynthesizerUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.youdao.sdk.app.YouDaoApplication;

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
        //讯飞文本生成初始化工作
        SpeechSynthesizerUtil.getInstance().init(this);

        //有道智云初始化工作
        YouDaoApplication.init(this, SmartChatConstant.YOUDAOAPIKEY);



    }
}

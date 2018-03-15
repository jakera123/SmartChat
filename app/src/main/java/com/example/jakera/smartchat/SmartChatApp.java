package com.example.jakera.smartchat;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Utils.SpeechSynthesizerUtil;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.youdao.sdk.app.YouDaoApplication;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by jakera on 18-1-24.
 */

public class SmartChatApp extends Application {
    private final String APPID="5a686b03";
    private String TAG = "SmartChatApp";

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //讯飞语音初始化工作
                SpeechUtility.createUtility(SmartChatApp.this, SpeechConstant.APPID + "=" + APPID);
                //讯飞文本生成初始化工作
                SpeechSynthesizerUtil.getInstance().init(SmartChatApp.this);

                //有道智云初始化工作
                YouDaoApplication.init(SmartChatApp.this, SmartChatConstant.YOUDAOAPIKEY);


                //极光推送
                JPushInterface.setDebugMode(true);
                JPushInterface.init(SmartChatApp.this);
                //极光即时通信
                JMessageClient.setDebugMode(true);
                JMessageClient.init(SmartChatApp.this);

                Intent intent = new Intent(SmartChatApp.this, SmartChatService.class);
                startService(intent);

                final FFmpeg fFmpeg = FFmpeg.getInstance(SmartChatApp.this);
                try {
                    fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                        @Override
                        public void onFailure() {
                            Log.i(TAG, "onFailure,FFmpeg加载失败");
                        }

                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "onFailure,FFmpeg加载成功");
                        }

                        @Override
                        public void onStart() {
                            Log.i(TAG, "onFailure,FFmpeg加载开始");
                        }

                        @Override
                        public void onFinish() {
                            Log.i(TAG, "onFailure,FFmpeg加载完成");
                        }
                    });
                } catch (FFmpegNotSupportedException e) {
                    e.printStackTrace();
                }


            }
        }).start();

        Log.i("SmartChat", "i am onCreate....");



    }
}

package com.example.jakera.smartchat.Utils;

/**
 * Created by jakera on 18-2-6.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 描述：语音播放工具类 *
 * 使用：直接用类名调用得到对实例，然后传入文本直接可以播放文本
 */
public class SpeechSynthesizerUtil {
    private static SpeechSynthesizerUtil audioUtils;
    private SpeechSynthesizer mySynthesizer;

    public SpeechSynthesizerUtil() {
    }

    /**
     * 描述:单例 *
     */
    public static SpeechSynthesizerUtil getInstance() {
        if (audioUtils == null) {
            synchronized (SpeechSynthesizerUtil.class) {
                if (audioUtils == null) {
                    audioUtils = new SpeechSynthesizerUtil();
                }
            }
        }
        return audioUtils;
    }

    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("mySynthesiezer:", "InitListener init() code = " + code);
        }
    };

    /**
     * 描述:初始化语音配置 *
     */
    public void init(Context context) {
        //处理语音合成关键类
        mySynthesizer = SpeechSynthesizer.createSynthesizer(context, myInitListener);
        //设置发音人
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置音调
        mySynthesizer.setParameter(SpeechConstant.PITCH, "50");
        //设置音量
        mySynthesizer.setParameter(SpeechConstant.VOLUME, "50");
    }

    /**
     * 描述:根据传入的文本转换音频并播放 *
     */
    public void speakText(String content) {
        int code = mySynthesizer.startSpeaking(content, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
            }

            @Override
            public void onSpeakPaused() {
            }

            @Override
            public void onSpeakResumed() {
            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {
            }

            @Override
            public void onCompleted(SpeechError speechError) {
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
            }
        });
    }
}

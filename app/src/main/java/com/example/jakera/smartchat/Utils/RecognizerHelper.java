package com.example.jakera.smartchat.Utils;

import android.os.Environment;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by jakera on 18-1-25.
 */

public class RecognizerHelper {
    private SpeechRecognizer mSpeechRecognizer;
    private RecognizerDialog mRecognizerDialog;
    private RecognizerDialogListener mRListener;

    private String TAG="RecognizerHelper";


    public RecognizerHelper(SpeechRecognizer speechRecognizer,RecognizerDialog recognizerDialog) {
        this.mSpeechRecognizer = speechRecognizer;
        this.mRecognizerDialog=recognizerDialog;
    }

    public void setListener(RecognizerDialogListener listener){
        this.mRecognizerDialog.setListener(listener);
    }



    public void startSpeechRecognizer(){
        setIatParam("0001");
        mRecognizerDialog.show();
    }


    private void setIatParam(String filename){
        mRecognizerDialog.setParameter(SpeechConstant.PARAMS,null);

        mRecognizerDialog.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);

        mRecognizerDialog.setParameter(SpeechConstant.RESULT_TYPE,"json");

        mRecognizerDialog.setParameter(SpeechConstant.LANGUAGE,"zh_ch");

        mRecognizerDialog.setParameter(SpeechConstant.ACCENT,"mandarin");

        mRecognizerDialog.setParameter(SpeechConstant.VAD_BOS,"4000");

        mRecognizerDialog.setParameter(SpeechConstant.VAD_EOS,"2000");

        mRecognizerDialog.setParameter(SpeechConstant.ASR_PTT,"1");

        mRecognizerDialog.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");

        mRecognizerDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageState()+filename+".wav");
    }


    public static String parseIatResult(String json){
        StringBuffer ret=new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}

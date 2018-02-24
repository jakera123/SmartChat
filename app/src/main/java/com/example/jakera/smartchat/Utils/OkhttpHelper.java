package com.example.jakera.smartchat.Utils;


import android.util.Log;

import com.example.jakera.smartchat.SmartChatConstant;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jakera on 18-2-1.
 */

public class OkhttpHelper {

    private OkHttpClient client;


    private Request request;

    private String TAG="OkhttpHelper";

    private Callback callback;

    public OkhttpHelper() {
        client=new OkHttpClient();

    }

    public void setCallback(Callback callback){
        this.callback=callback;
    }

    public void postToTuLingRobot(String content,String userId){
        //请求体要作为临时变量，否则，第二次请求时add参数时，会加入第一次的数据，导入请求失败
        FormBody.Builder formBody;
        formBody=new FormBody.Builder();
        formBody.add("key",SmartChatConstant.TULINGROBOTAPIKEY);
        formBody.add("info",content);
        //用户id，可以关联上下文
        formBody.add("userid", userId);
        request=new Request.Builder()
                .url(SmartChatConstant.TULINGAPI)
                .post(formBody.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getByUrl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);

    }


    public String parseTuLingResult(String json){
        String result="不想理你";
        try {
            JSONObject jsonObject = new JSONObject(json);
            result=jsonObject.getString("text");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}

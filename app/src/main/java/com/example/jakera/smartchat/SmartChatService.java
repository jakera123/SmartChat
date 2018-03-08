package com.example.jakera.smartchat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by jakera on 18-3-7.
 */

public class SmartChatService extends Service {

    private String TAG = "SmartChatService";
    private SmartChatBinder binder = new SmartChatBinder();
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private List<String> messageList;
    private getMessageListener getMessage;

    public void setGetMessageListenr(getMessageListener listenr) {
        this.getMessage = listenr;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "我的服务，onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "我的服务，onCreate");
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        messageList = new ArrayList<>();
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "我的服务，onStartcommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "我的服务，onDestroy");
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    public void onEvent(NotificationClickEvent event) {
        Log.i(TAG, "通知栏点击" + event.getMessage().getContent().toJson());
        Intent intent = new Intent(this, ChatActivity.class);
        Bundle data = new Bundle();
        data.putString("username", event.getMessage().getFromUser().getUserName());
        Log.i(TAG, event.getMessage().getFromUser().getUserName());
        //在service启动activity必须加入这句
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("username", data);
        startActivity(intent);

    }

    //不同的Event接收不用的实体对象,同时在线
    public void onEvent(MessageEvent event) {
        if (!messageList.contains(event.getMessage().getFromUser().getUserName())) {
            messageList.add(event.getMessage().getFromUser().getUserName());
        } else {
            messageList.remove(event.getMessage().getFromUser().getUserName());
            messageList.add(0, event.getMessage().getFromUser().getUserName());
        }
        for (int i = 0; i < messageList.size(); i++) {
            Log.i(TAG, "消息列表：i=" + i + "," + messageList.get(i));
        }
        if (getMessage != null) {
            getMessage.getMessageList(messageList);
        }
        //       Log.i(TAG,"收到信息"+event.getResponseCode()+","+event.getMessage().getStatus().toString()+","+event.getMessage().haveRead());
//        if (!event.getMessage().haveRead()){
//            Intent intent=new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    public class SmartChatBinder extends Binder {
        public SmartChatService getService() {
            //返回当前对象，可以客户端调用Service公共方法
            return SmartChatService.this;
        }
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public interface getMessageListener {
        void getMessageList(List<String> messageList);
    }


}

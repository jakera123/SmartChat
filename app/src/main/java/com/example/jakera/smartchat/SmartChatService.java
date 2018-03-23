package com.example.jakera.smartchat;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.example.jakera.smartchat.Activity.ChatActivity;
import com.example.jakera.smartchat.Activity.MainActivity;
import com.example.jakera.smartchat.Entry.BaseMessageEntry;
import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.Entry.TextMessageEntry;
import com.example.jakera.smartchat.Entry.VoiceMessageEntry;
import com.example.jakera.smartchat.Utils.MySQLiteOpenHelper;
import com.example.jakera.smartchat.Utils.TimeUtil;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by jakera on 18-3-7.
 *
 * 接收到聊天信息先是在内存维护一个数组，退出时写入数据库，在创建时先从数据库里读取数据。
 *调试时要注意之前的数据是否一直存在数据库里
 *
 */

public class SmartChatService extends Service {

    private String TAG = "SmartChatService";
    private SmartChatBinder binder = new SmartChatBinder();
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private List<MessageEntry> messageList = new ArrayList<>();
    private getMessageListener getMessage;
    public boolean isLoadMessageListFromDB = false;

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
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "我的服务，onStartcommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        saveMessageListToDB();
        Log.i(TAG, "我的服务，onDestroy");
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.i(TAG, "我的服务，stop");
        return super.stopService(name);


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
        String username = event.getMessage().getFromUser().getUserName();

        for (MessageEntry messageEntry : messageList) {
            if (messageEntry.getUsername().equals(username)) {
                messageList.remove(messageEntry);
            }
        }
        MessageEntry receiver = new MessageEntry();
        receiver.setUsername(username);
        receiver.setTime(TimeUtil.stampToDate(event.getMessage().getCreateTime() + ""));
        Log.i(TAG, "时间:" + TimeUtil.stampToDate(event.getMessage().getCreateTime() + ""));
        if (event.getMessage().getContent() instanceof TextContent) {
            receiver.setContent(((TextContent) event.getMessage().getContent()).getText());
        } else if (event.getMessage().getContent() instanceof VoiceContent) {
            receiver.setContent("收到一条语音消息");
        }
        messageList.add(0, receiver);
        if (getMessage != null) {
            getMessage.getMessageList(messageList);
        }

        String sql = "create table if not exists " + event.getMessage().getFromUser().getUserName() + " (type integer,username text,content text,RecOrSend integer,VoiceTime real,VoicePath text)";
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.execSQL(sql);
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        if (event.getMessage().getContent() instanceof VoiceContent) {
            contentValues.put("type", MySQLiteOpenHelper.MessageVoiceType);
            contentValues.put("username", event.getMessage().getFromUser().getUserName());
            contentValues.put("RecOrSend", BaseMessageEntry.RECEIVEMESSAGE);
            contentValues.put("VoicePath", ((VoiceContent) event.getMessage().getContent()).getLocalPath());
            contentValues.put("VoiceTime", ((VoiceContent) event.getMessage().getContent()).getDuration());
        } else if (event.getMessage().getContent() instanceof TextContent) {
            contentValues.put("type", MySQLiteOpenHelper.MessageTextType);
            contentValues.put("username", event.getMessage().getFromUser().getUserName());
            contentValues.put("RecOrSend", BaseMessageEntry.RECEIVEMESSAGE);
            contentValues.put("content", ((TextContent) event.getMessage().getContent()).getText());
        }
        db.insertOrThrow(event.getMessage().getFromUser().getUserName(), null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    //子线程模式
    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        final String fromUsername = event.getFromUsername();
        String appkey = event.getfromUserAppKey();

        switch (event.getType()) {
            case invite_received://收到好友邀请
                AlertDialog dialogReceived = new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.friend_invite_received))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_tip))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactManager.declineInvitation(fromUsername, null, "", null);
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContactManager.acceptInvitation(fromUsername, null, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            ContactManager.getFriendList(new GetUserInfoListCallback() {
                                                @Override
                                                public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                                                    if (0 == responseCode) {
                                                        //获取好友列表成功
//                                                        friendsListRecyclerAdapter.setDatas(userInfoList);
//                                                        datas = userInfoList;
//                                                        friendsListRecyclerAdapter.notifyDataSetChanged();
                                                    } else {
                                                        //获取好友列表失败
                                                        Log.i(TAG, "获取好友列表失败");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                dialog.dismiss();
                            }
                        }).create();
                //在dialog  show方法之前添加如下代码，表示该dialog是一个系统的dialog**
                dialogReceived.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialogReceived.show();
                break;
            case invite_accepted://对方接收了你的好友邀请
                AlertDialog dialogAccept = new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_accept))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogAccept.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialogAccept.show();
                break;
            case invite_declined://对方拒绝了你的好友邀请
                AlertDialog dialogReject = new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon)//设置标题的图片
                        .setTitle(getString(R.string.add_friends))//设置对话框的标题
                        .setMessage(fromUsername + getString(R.string.friend_invite_reject))//设置对话框的内容
                        //设置对话框的按钮
                        .setNegativeButton(getString(R.string.reject), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialogReject.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialogReject.show();
                break;
            case contact_deleted://对方将你从好友中删除
                //...
                Log.i(TAG, "对方将你从好友中删除");
                break;
            default:
                break;
        }
    }

    public class SmartChatBinder extends Binder {
        public SmartChatService getService() {
            //返回当前对象，可以客户端调用Service公共方法
            return SmartChatService.this;
        }
    }

    public List<MessageEntry> getMessageList() {
        return messageList;
    }

    public interface getMessageListener {
        void getMessageList(List<MessageEntry> messageList);
    }

    //username content time
    public void saveMessageListToDB() {
        Log.i(TAG, "正在保存数据");
        isLoadMessageListFromDB = true;
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.delete(MySQLiteOpenHelper.TABLEMESSAGELIST, null, null);
        for (MessageEntry entry : messageList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", entry.getUsername());
            contentValues.put("content", entry.getContent());
            contentValues.put("time", entry.getTime());
            Log.i(TAG, entry.getUsername());
            db.insertOrThrow(MySQLiteOpenHelper.TABLEMESSAGELIST, null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    //防止在未登陆时无法初始化数据库而报错
    public void initSQlite() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
    }

    //全局只需调用一次，将数据加载到内存
    public void getMessageFromDB() {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        String sql = "select * from " + MySQLiteOpenHelper.TABLEMESSAGELIST;
        StringBuffer sb = new StringBuffer();
        Cursor cursor = db.rawQuery(sql, null);
        //防止多次读入数据
        messageList.clear();
        while (cursor.moveToNext()) {
            MessageEntry entry = new MessageEntry();
            entry.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            entry.setTime(cursor.getString(cursor.getColumnIndex("time")));
            entry.setContent(cursor.getString(cursor.getColumnIndex("content")));
            messageList.add(entry);
        }

        if (getMessage != null) {
            getMessage.getMessageList(messageList);
        }
        Log.i(TAG, "getMessageFromDB:" + sb.toString());
        db.close();
    }

}

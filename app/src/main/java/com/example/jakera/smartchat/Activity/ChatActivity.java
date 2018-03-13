package com.example.jakera.smartchat.Activity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jakera.smartchat.Adapter.ChatRecyclerViewAdapter;
import com.example.jakera.smartchat.Entry.BaseMessageEntry;
import com.example.jakera.smartchat.Entry.MessageEntry;
import com.example.jakera.smartchat.Entry.TextMessageEntry;
import com.example.jakera.smartchat.Entry.VoiceMessageEntry;
import com.example.jakera.smartchat.Fragment.MessageListFragment;
import com.example.jakera.smartchat.Interface.ItemClickListener;
import com.example.jakera.smartchat.R;
import com.example.jakera.smartchat.SmartChatConstant;
import com.example.jakera.smartchat.SmartChatService;
import com.example.jakera.smartchat.Utils.MediaManager;
import com.example.jakera.smartchat.Utils.MySQLiteOpenHelper;
import com.example.jakera.smartchat.Utils.OkhttpHelper;
import com.example.jakera.smartchat.Utils.RecognizerHelper;
import com.example.jakera.smartchat.Utils.SpeechSynthesizerUtil;
import com.example.jakera.smartchat.Utils.TranslateUtil;
import com.example.jakera.smartchat.Views.AudioRecorderButton;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.android.tasks.GetEventNotificationTaskMng;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by jakera on 18-2-1.
 */

public class ChatActivity extends AppCompatActivity implements Callback, ItemClickListener, RecognizerHelper.getVoiceToTextResult {

    private String TAG="ChatActivity";

    private RecyclerView recyclerView;
    //防止在不同线程
    private List<BaseMessageEntry> datas;
    private ChatRecyclerViewAdapter adapter;

    private TextView tv_send;
    private EditText et_input_text;

    private OkhttpHelper okhttpHelper;

    private ImageButton btn_voice_chat, btn_btn_select_language;

    private AudioRecorderButton mAudioRecorderButton;

    private boolean isVoiceMode=false;

    private boolean isChinese = true;

    private String friendUsername;
    private UserInfo friendUserInfo;
    private Bitmap frindPortrait;

    private Conversation conversation;

    private String receiver_text;


    private TextView tv_title_bar_center;
    private ImageView iv_title_bar_back;

    private RecognizerHelper recognizerHelper;
    private SpeechRecognizer speechRecognizer;

    private int clickPosition;

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(Color.BLACK);
        getSupportActionBar().hide();


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("username");
        friendUsername = bundle.getString("username");
        setContentView(R.layout.activity_chat);

        mAudioRecorderButton=(AudioRecorderButton)findViewById(R.id.id_recorder_button);
        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                VoiceMessageEntry voiceMessageEntry=new VoiceMessageEntry(seconds,filePath);
                Log.i(TAG,filePath);
                voiceMessageEntry.setUserName(JMessageClient.getMyInfo().getUserName());
                voiceMessageEntry.setViewType(BaseMessageEntry.SENDMESSAGE);
                try {
                    Message voiceMessage = JMessageClient.createSingleVoiceMessage(friendUsername, null, new File(filePath), (int) seconds);
                    voiceMessage.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(TAG, "发送语音成功");
                            }
                        }
                    });
                    JMessageClient.sendMessage(voiceMessage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                datas.add(voiceMessageEntry);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size()-1);
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new ChatRecyclerViewAdapter();
        adapter.setOnItemClickListener(this);

        datas=new ArrayList<>();

        okhttpHelper=new OkhttpHelper();
        okhttpHelper.setCallback(this);


        et_input_text=(EditText)findViewById(R.id.et_input_text);
        tv_send=(TextView)findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextMessageEntry messageEntry = new TextMessageEntry();
                messageEntry.setUserName(JMessageClient.getMyInfo().getUserName());
                // messageEntry.setPortrait(BitmapFactory.decodeResource(getResources(),R.mipmap.icon));
                messageEntry.setContent(et_input_text.getText().toString());
                if (friendUsername.equals(SmartChatConstant.APPNAME)) {
                    okhttpHelper.postToTuLingRobot(et_input_text.getText().toString(), "123456");
                }
                messageEntry.setViewType(TextMessageEntry.SENDMESSAGE);
                datas.add(messageEntry);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size()-1);


                // JMessageClient.createSingleTextMessage("mary",null,"你好啊");
                MessageContent content = new TextContent(et_input_text.getText().toString());
                //创建一条消息
                Message message = conversation.createSendMessage(content);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i(TAG, "发送结果: i=" + i + ",s=" + s);
                    }
                });
                MessageSendingOptions options = new MessageSendingOptions();
                options.setRetainOffline(false);
                //发送消息
                JMessageClient.sendMessage(message);


                et_input_text.setText("");


            }
        });


        btn_voice_chat=(ImageButton)findViewById(R.id.btn_voice_chat);
        btn_voice_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceMode){
                    et_input_text.setVisibility(View.VISIBLE);
                    mAudioRecorderButton.setVisibility(View.GONE);
                    isVoiceMode=!isVoiceMode;
                }else {
                    et_input_text.setVisibility(View.GONE);
                    mAudioRecorderButton.setVisibility(View.VISIBLE);
                    isVoiceMode=!isVoiceMode;
                    //关闭弹出的键盘
                    closeKeyboard();
                }

            }
        });

        btn_btn_select_language = (ImageButton) findViewById(R.id.btn_select_language);
        btn_btn_select_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChinese) {
                    isChinese = false;
                    btn_btn_select_language.setBackground(getResources().getDrawable(R.drawable.english));
                } else {
                    isChinese = true;
                    btn_btn_select_language.setBackground(getResources().getDrawable(R.drawable.chinese));
                }
            }
        });



        adapter.setDatas(datas);
        recyclerView.setAdapter(adapter);
        init();

    }

    public void init() {

        //创建跨应用会话
        conversation = Conversation.createSingleConversation(friendUsername, null);
        tv_title_bar_center = (TextView) findViewById(R.id.tv_title_bar_center);
        tv_title_bar_center.setText(friendUsername);
        iv_title_bar_back = (ImageView) findViewById(R.id.iv_title_bar_back);
        iv_title_bar_back.setVisibility(View.VISIBLE);
        iv_title_bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        JMessageClient.getUserInfo(friendUsername, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                friendUserInfo = userInfo;
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        frindPortrait = bitmap;
                    }
                });
            }
        });
        speechRecognizer = SpeechRecognizer.createRecognizer(this, null);
        recognizerHelper = new RecognizerHelper(speechRecognizer);
        recognizerHelper.setVoiceToTextListener(this);

        String sql = "create table if not exists " + friendUsername + " (type integer,username text,content text,RecOrSend integer,VoiceTime real,VoicePath text)";
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this);
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        String sql2 = "select * from " + friendUsername;
        Cursor cursor = db.rawQuery(sql2, null);
        while (cursor.moveToNext()) {
            int MessageType = cursor.getInt(cursor.getColumnIndex("type"));
            if (MessageType == MySQLiteOpenHelper.MessageTextType) {
                TextMessageEntry messageEntry = new TextMessageEntry();
                messageEntry.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                messageEntry.setContent(cursor.getString(cursor.getColumnIndex("content")));
                messageEntry.setViewType(cursor.getInt(cursor.getColumnIndex("RecOrSend")));
                datas.add(messageEntry);
            } else if (MessageType == MySQLiteOpenHelper.MessageVoiceType) {
                float seconds = cursor.getFloat(cursor.getColumnIndex("VoiceTime"));
                String filePath = cursor.getString(cursor.getColumnIndex("VoicePath"));
                VoiceMessageEntry voiceMessageEntry = new VoiceMessageEntry(seconds, filePath);
                Log.i(TAG, filePath);
                voiceMessageEntry.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                voiceMessageEntry.setViewType(cursor.getInt(cursor.getColumnIndex("RecOrSend")));
                datas.add(voiceMessageEntry);
            }
        }
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(datas.size() - 1);
        db.execSQL(sql);
        db.close();

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String answer=response.body().string();
        TextMessageEntry messageEntry=new TextMessageEntry();
        messageEntry.setUserName(friendUsername);
        messageEntry.setContent(okhttpHelper.parseTuLingResult(answer));
        messageEntry.setViewType(TextMessageEntry.RECEIVEMESSAGE);
        datas.add(messageEntry);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(datas.size()-1);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.delete(friendUsername, null, null);
        for (BaseMessageEntry entry : datas) {
            ContentValues contentValues = new ContentValues();
            if (entry instanceof VoiceMessageEntry) {
                contentValues.put("type", MySQLiteOpenHelper.MessageVoiceType);
                contentValues.put("username", entry.getUserName());
                contentValues.put("RecOrSend", entry.getViewType());
                contentValues.put("VoicePath", ((VoiceMessageEntry) entry).getFilePath());
                contentValues.put("VoiceTime", ((VoiceMessageEntry) entry).getTime());
            } else if (entry instanceof TextMessageEntry) {
                contentValues.put("type", MySQLiteOpenHelper.MessageTextType);
                contentValues.put("username", entry.getUserName());
                contentValues.put("RecOrSend", entry.getViewType());
                contentValues.put("content", ((TextMessageEntry) entry).getContent());
            }
            db.insertOrThrow(friendUsername, null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    @Override
    public void OnItemClick(View v, final int position) {
        if(datas.get(position) instanceof VoiceMessageEntry){
            MediaManager.playSound(((VoiceMessageEntry) datas.get(position)).getFilePath(),null);
            recognizerHelper.recognizerFromAmr(((VoiceMessageEntry) datas.get(position)).getFilePath());
            clickPosition = position;

        } else if (datas.get(position) instanceof TextMessageEntry) {
            String fromLanguage, toLanguage;
            if (isChinese) {
                fromLanguage = "英文";
                toLanguage = "中文";
            } else {
                fromLanguage = "中文";
                toLanguage = "英文";
            }
            String content = ((TextMessageEntry) datas.get(position)).getContent();
            TranslateUtil.translate(fromLanguage, toLanguage, content, new TranslateListener() {
                @Override
                public void onError(TranslateErrorCode translateErrorCode, String s) {

                }

                @Override
                public void onResult(Translate translate, String s, String s1) {
                    if (translate.getTranslations().size() > 0) {
                        ((TextMessageEntry) datas.get(position)).setContent(translate.getTranslations().get(0));
                        SpeechSynthesizerUtil.getInstance().speakText(translate.getTranslations().get(0));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
            });
        }
    }

    //不同的Event接收不用的实体对象,同时在线
    public void onEvent(MessageEvent event) {
        final Message msg = event.getMessage();
        UserInfo freind = msg.getFromUser();
        String username_receiver = freind.getUserName();
        Log.i(TAG, "onEvent:接到事件");

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                receiver_text = textContent.getText();
                if (username_receiver.equals(friendUsername)) {

                    //这里添加同一消息，需要在同一个线程之中，否则会出现，接收到的消息，需要点一下才能够更新recyclerview的显示,不能够正常地显示
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextMessageEntry messageEntry = new TextMessageEntry();

                            messageEntry.setUserName(friendUsername);
                            messageEntry.setContent(receiver_text);
                            messageEntry.setViewType(TextMessageEntry.RECEIVEMESSAGE);
                            datas.add(messageEntry);
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(datas.size() - 1);
                        }
                    });

                }
                //textContent.getText();
                //Log.i(TAG, username_receiver + ":" + textContent.getText());
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
//                voiceContent.getLocalPath();//语音文件本地地址
//                voiceContent.getDuration();//语音文件时长
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                final VoiceMessageEntry voiceMessageEntry = new VoiceMessageEntry(voiceContent.getDuration(), voiceContent.getLocalPath());
                Log.i(TAG, "接到语音信息" + voiceContent.getLocalPath());
                voiceMessageEntry.setUserName(friendUsername);
                voiceMessageEntry.setViewType(BaseMessageEntry.RECEIVEMESSAGE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        datas.add(voiceMessageEntry);
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(datas.size() - 1);
                    }
                });
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                    case group_info_updated://since 2.2.1
                        //群信息变更事件
                        break;
                }
                break;
        }
    }


//    /**
//     * 类似MessageEvent事件的接收，上层在需要的地方增加OfflineMessageEvent事件的接收
//     * 即可实现离线消息的接收。
//     **/
//    public void onEvent(OfflineMessageEvent event) {
//        //获取事件发生的会话对象
//        Log.i(TAG, "收到消息");
//        Conversation conversation = event.getConversation();
//        List<Message> newMessageList = event.getOfflineMessageList();//获取此次离线期间会话收到的新消息列表
//        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到%d条来自%s的离线消息。\n", newMessageList.size(), conversation.getTargetId()));
//        for (int i = 0; i < newMessageList.size(); i++) {
//            //  {"text":"你好","extras":{}}
////            Log.i(TAG,"i="+i+","+newMessageList.get(i).getContent().toJson());
////            Log.i(TAG,"i="+i+","+newMessageList.get(i).getContent().);
//
//        }
//
//
//    }
//
//
//    /**
//     * 如果在JMessageClient.init时启用了消息漫游功能，则每当一个会话的漫游消息同步完成时
//     * sdk会发送此事件通知上层。
//     **/
//    public void onEvent(ConversationRefreshEvent event) {
//        Log.i(TAG, "收到消息");
//        //获取事件发生的会话对象
//        Conversation conversation = event.getConversation();
//        //获取事件发生的原因，对于漫游完成触发的事件，此处的reason应该是
//        //MSG_ROAMING_COMPLETE
//        ConversationRefreshEvent.Reason reason = event.getReason();
//        System.out.println(String.format(Locale.SIMPLIFIED_CHINESE, "收到ConversationRefreshEvent事件,待刷新的会话是%s.\n", conversation.getTargetId()));
//        System.out.println("事件发生的原因 : " + reason);
//    }


    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void getRecognizeResult(String result) {
        if (result.length() <= 1) {
            return;
        }
        VoiceMessageEntry entry = (VoiceMessageEntry) datas.get(clickPosition);
        final TextMessageEntry messageEntry = new TextMessageEntry();
        messageEntry.setUserName(entry.getUserName());
        messageEntry.setContent(result);
        messageEntry.setViewType(entry.getViewType());
        datas.add(clickPosition + 1, messageEntry);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(datas.size() - 1);
    }
}

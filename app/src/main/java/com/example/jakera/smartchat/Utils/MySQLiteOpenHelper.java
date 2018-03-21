package com.example.jakera.smartchat.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jakera.smartchat.SmartChatApp;
import com.example.jakera.smartchat.SmartChatConstant;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by jakera on 18-3-7.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    //TODO:这里用户名要缓存到本地，否则会出现
    private static final String DB_NAME = SmartChatApp.USERNAME + ".db";
    public static final String TABLEMESSAGELIST = "MessageList";
    public static final int MessageTextType = 0;
    public static final int MessageVoiceType = 1;

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLEMESSAGELIST + " (username text, content text,time text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLEMESSAGELIST;
        db.execSQL(sql);
        onCreate(db);
    }
}

package com.example.jakera.smartchat.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by jakera on 18-3-7.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = JMessageClient.getMyInfo().getUserName() + ".db";
    public static final String TABLEMESSAGELIST = "MessageList";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLEMESSAGELIST + " (Id integer primary key, FriendsUserName text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLEMESSAGELIST;
        db.execSQL(sql);
        onCreate(db);
    }
}

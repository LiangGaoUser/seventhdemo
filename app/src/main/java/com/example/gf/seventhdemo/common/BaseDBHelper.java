package com.example.gf.seventhdemo.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_T_MEMO = "create table t_memo("
            +"id integer primary key autoincrement,"
            +"memo_createtime text,"
            +"memo_title text,"
            +"memo_content text)";

    private Context context;

    public BaseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_T_MEMO);   //创建数据库表t_memo
        Log.d("msg:","Create succeed！");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

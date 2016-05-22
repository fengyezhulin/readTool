package com.buaa.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/21.
 */
public class DBUtil extends SQLiteOpenHelper {

    public DBUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBUtil(Context context) {
        super(context,"read.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //文本表，用来存放文本信息
        String sql = "create table txt(" +
                "       id         integer      primary key," +
                "       full_path   text," +
                "       now_page    integer," +
                "       over_flag   integer)";
        db.execSQL(sql);

        //page表，用来存放单个文本的内容
        sql = "create table page(" +
                "       id      integer         primary key," +
                "       txt_id  integer," +
                "       page_num    integer," +
                "       content     text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

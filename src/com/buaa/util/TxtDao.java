package com.buaa.util;

import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/21.
 */
public class TxtDao {

    public static void insertTxtData(String fullPath) {
        //在插入之前先判断当前数据库中是否存在记录，如果不存在，插入数据
        String sql = "select id from txt where full_path=?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{fullPath});
        if(!c.moveToFirst()) {
            sql = "insert into txt (full_path, now_page, over_flag) values(?, 1, 0)";
            Globals.util.getWritableDatabase().execSQL(sql, new Object[] {fullPath});
        }
        c.close();
    }

    public static Map<String, Object> findTxtByFullPath(String fullPath) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select id, now_page, over_flag from txt where full_path=?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{fullPath});
        c.moveToFirst();
        map.put("txtid", c.getInt(0));
        map.put("nowPage", c.getInt(1));
        map.put("overFlag", c.getInt(2));
        c.close();
        return map;
    }

    public static void updateNowPage(int nowPage, String fullPath) {
        String sql = "update txt set now_page=? where full_path=?";
        Globals.util.getWritableDatabase().execSQL(sql, new Object[]{nowPage, fullPath});
    }

    public static void updateTxtOverFlag(String fullPath) {
        String sql = "update txt set over_flag=1 where full_path=?";
        Globals.util.getWritableDatabase().execSQL(sql, new Object[]{fullPath});
    }
}

package com.buaa.util;

import android.database.Cursor;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/21.
 */
public class PageDao {

    public static void insertPageData(Map<String, Object> map) {
        String sql = "insert into page(txt_id, page_num, content) values(?,?,?)";
        Globals.util.getWritableDatabase().execSQL(sql, new Object[]{map.get("txtid"), map.get("pagenum"), map.get("content")});
    }

    public static String findContent(int txtid, int pagenum) {
        String sql = "select content from page where txt_id=? and page_num=?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{txtid+"",pagenum+""});
        c.moveToFirst();
        String content = c.getString(0);
        c.close();
        return content;
    }

    public static int getPageCount(int txtid) {
        String sql = "select count(*) from page where txt_id=?";
        Cursor c = Globals.util.getReadableDatabase().rawQuery(sql, new String[]{txtid + ""});
        c.moveToFirst();
        int pagenum = c.getInt(0);
        c.close();
        return pagenum;
    }
}

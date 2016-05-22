package com.buaa.util;

import android.app.Activity;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Globals {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEGITH;

    //每行显示20个字符
    public static int LINE_CHAR_COUNT = 20;
    //字符间距
    public static int CHAR_SEP = 2;
    //行间距
    public static int LINE_SEP = 2;
    //页边距
    public static int PAGE_SEP = 20;

    //字符大小
    public static int CHAR_SIZE;
    //行数
    public static int LINE_COUNT;

    public static final String END_FLAG = "LINE_END_FLAG";

    //数据库连接类
    public static DBUtil util;
    public static void init(Activity a) {
        util = new DBUtil(a);
        SCREEN_WIDTH = a.getWindowManager().getDefaultDisplay().getWidth();
        SCREEN_HEGITH = a.getWindowManager().getDefaultDisplay().getHeight();

        CHAR_SIZE = (SCREEN_WIDTH - PAGE_SEP * 2 - (LINE_CHAR_COUNT-1) * CHAR_SEP) / LINE_CHAR_COUNT;

        LINE_COUNT = SCREEN_HEGITH * 4 / 5 / (CHAR_SIZE + LINE_SEP);
    }
}

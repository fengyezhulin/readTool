package com.buaa.activity;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.widget.TextView;

import com.buaa.reader.R;
import com.buaa.util.Globals;
import com.buaa.util.PageDao;
import com.buaa.util.TxtDao;
import com.buaa.view.MyView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    //文本信息
    private Map<String, Object> txtMap;
    //文件详细内容
    private Map<String, Object> pageMap = new HashMap<String, Object>();

    private StringBuffer sb = new StringBuffer();

    //当前读到的行数
    private int lineCount;
    //总页数
    private int pagenum = 1;
    //当前页数
    private int nowPage = 1;

    private String fullPath;

    private MyView contentView;
    private TextView pageView;

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.init(this);
        setContentView(R.layout.activity_main);

        contentView = (MyView) findViewById(R.id.content_view);
        pageView = (TextView) findViewById(R.id.page_view);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0) {
                    pageView.setText("正在分页，已经分解" + (pagenum-1) + "页");
                }else if(msg.what==1) {
                    pageView.setText("分页结束：" + nowPage + "/" + pagenum);
                }
            }
        };

       fullPath = "file:///android_asset/xiyou.txt";
      
//        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/xiyou.txt";
        TxtDao.insertTxtData(fullPath);

        txtMap = TxtDao.findTxtByFullPath(fullPath);

        if ((Integer) txtMap.get("overFlag") == 0) {
            //需要分页，第一次访问

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fullPath), "GBK"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            while (line.length() > Globals.LINE_CHAR_COUNT) {
                                String str = line.substring(0, Globals.LINE_CHAR_COUNT);
                                line = line.substring(Globals.LINE_CHAR_COUNT);
                                addLine(str);
                            }
                            //还剩一部分数据，也算做一行。
                            addLine(line);
                        }
                        br.close();
                        handler.sendEmptyMessage(1);
                        TxtDao.updateTxtOverFlag(fullPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        } else {
            //直接访问到上一次访问的页数
            pagenum = PageDao.getPageCount((Integer)txtMap.get("txtid"));
            nowPage = (Integer)txtMap.get("nowPage");
            contentView.changeData();

        }

    }

    @Override
    protected void onDestroy() {
        TxtDao.updateNowPage(nowPage, fullPath);
        super.onDestroy();
    }

    public void addLine(String str) {
        sb.append(str);
        sb.append(Globals.END_FLAG);
        lineCount++;
        if (lineCount == Globals.LINE_COUNT) {
            pageMap.put("txtid", txtMap.get("txtid"));
            pageMap.put("pagenum", pagenum++);
            pageMap.put("content", sb.toString());
            PageDao.insertPageData(pageMap);

            if(pagenum==10) {
                contentView.changeData();
            }

            handler.sendEmptyMessage(0);
            sb = new StringBuffer();
            lineCount = 0;
        }
    }

    public Map<String, Object> getTxtMap() {
        return txtMap;
    }

    public void setTxtMap(Map<String, Object> txtMap) {
        this.txtMap = txtMap;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}

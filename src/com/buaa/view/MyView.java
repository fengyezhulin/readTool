package com.buaa.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.buaa.activity.MainActivity;
import com.buaa.util.Globals;
import com.buaa.util.PageDao;

/**
 * Created by Administrator on 2016/4/21.
 */
public class MyView extends View {

    private String content;
    private MainActivity a;
    private float startX;
    private float nowX;
    private String preContent;
    private String nextContent;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = (MainActivity)context;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    nowX = event.getX();
                    postInvalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs(nowX - startX) >= 50) {
                        //上一页
                        if (nowX > startX) {
                            if (a.getNowPage() > 1) {
                                a.setNowPage(a.getNowPage() - 1);
                                changeData();
                            }
                        }

                        //下一页
                        if (nowX < startX) {
                            if (a.getNowPage() < a.getPagenum()) {
                                a.setNowPage(a.getNowPage() + 1);
                                changeData();
                            }
                        }
                    }
                    startX = 0;
                    nowX = 0;
                }
                return false;
            }
        });
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void changeData() {
        content = PageDao.findContent((Integer)a.getTxtMap().get("txtid"), a.getNowPage());
        //调用onDraw方法


        if(a.getNowPage()>1) {
            preContent = PageDao.findContent((Integer)a.getTxtMap().get("txtid"), a.getNowPage()-1);
        }else {
            preContent = null;
        }

        if(a.getNowPage()<a.getPagenum()) {
            nextContent = PageDao.findContent((Integer)a.getTxtMap().get("txtid"), a.getNowPage()+1);
        }else {
            nextContent = null;
        }

        a.getHandler().sendEmptyMessage(1);
        super.postInvalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(Globals.CHAR_SIZE);
        if(content!=null) {
            String[] allValues = content.split(Globals.END_FLAG);
            for(int i=0;i< allValues.length;i++) {
                for(int j=0;j<allValues[i].length();j++) {
                    if(j + i * Globals.LINE_CHAR_COUNT < content.length()) {
                        canvas.drawText(String.valueOf(allValues[i].charAt(j)),
                                j * (Globals.CHAR_SIZE + Globals.CHAR_SEP) + Globals.PAGE_SEP + (nowX-startX),
                                (i+1) * (Globals.CHAR_SIZE + Globals.LINE_SEP),paint);
                    }
                }
            }

            if(nowX>startX && preContent!=null) {
                allValues = preContent.split(Globals.END_FLAG);
                for(int i=0;i< allValues.length;i++) {
                    for(int j=0;j<allValues[i].length();j++) {
                        if(j + i * Globals.LINE_CHAR_COUNT < content.length()) {
                            canvas.drawText(String.valueOf(allValues[i].charAt(j)),
                                    j * (Globals.CHAR_SIZE + Globals.CHAR_SEP) + Globals.PAGE_SEP + (nowX-startX) - Globals.SCREEN_WIDTH,
                                    (i+1) * (Globals.CHAR_SIZE + Globals.LINE_SEP),paint);
                        }
                    }
                }
            }

            if(nowX<startX && nextContent!=null) {
                allValues = nextContent.split(Globals.END_FLAG);
                for(int i=0;i< allValues.length;i++) {
                    for(int j=0;j<allValues[i].length();j++) {
                        if(j + i * Globals.LINE_CHAR_COUNT < content.length()) {
                            canvas.drawText(String.valueOf(allValues[i].charAt(j)),
                                    j * (Globals.CHAR_SIZE + Globals.CHAR_SEP) + Globals.PAGE_SEP + (nowX-startX) + Globals.SCREEN_WIDTH,
                                    (i+1) * (Globals.CHAR_SIZE + Globals.LINE_SEP),paint);
                        }
                    }
                }
            }
        }
    }
}

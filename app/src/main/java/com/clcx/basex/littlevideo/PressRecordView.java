package com.clcx.basex.littlevideo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ljc123 on 2018/1/15.
 */

public class PressRecordView extends View implements Runnable {
    private Paint mPaint;
    private static final int MAX_COUNT = 8;//时间限制
    private static final int INTERVEL = 20;//动画频率，每隔50毫秒走一次
    private static final int PROGRESSBAR_WIDTH = 15;//边界进度条宽度
    private static final int INIT_DELTA = 50;//初始的大小
    private int count;
    private OnRecordListener onRecordListener;
    private float delta = 360.0f / (float) (MAX_COUNT * 1000 / INTERVEL);
    private float countRadius;

    private boolean isPress = false;

    public PressRecordView(Context context) {
        super(context);
        init();
    }

    public PressRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PressRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPress) {
            mPaint.setColor(Color.GRAY);
            canvas.drawArc(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), -90, countRadius, true, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawArc(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), -90, countRadius, true, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 -
                    PROGRESSBAR_WIDTH, mPaint);
        } else {
            mPaint.setColor(Color.GRAY);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2 -
                    INIT_DELTA, mPaint);
        }
    }

    private boolean isStart = true;

    public void pStop() {
        isStart = false;
        count = 0;
        countRadius = 0;
        postInvalidate();
    }

    public void pStart() {
        isStart = true;
        countRadius = 0;
        count = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isPress = true;
            if (this.onRecordListener != null) {
                this.onRecordListener.onStart(this);
            }
            pStart();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isPress = false;
            if (this.onRecordListener != null) {
                this.onRecordListener.onStop();
            }
            pStop();
        }

        return true;
    }

    @Override
    public void run() {
        while (isStart) {
            count++;
            countRadius += delta;
            if (count >= MAX_COUNT * 1000 / INTERVEL) {
                pStop();
                if (this.onRecordListener != null) {
                    this.onRecordListener.onTimeUp();
                }
            }
            postInvalidate();

            try {
                Thread.sleep(INTERVEL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    public interface OnRecordListener {
        void onTimeUp();//计时到期

        void onStart(PressRecordView v);//按下按钮

        void onStop();//松开按钮
    }
}

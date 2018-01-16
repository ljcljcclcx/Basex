package com.clcx.basex.littlevideo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.clcx.basex.utils.LogCLCXUtils;

/**
 * Created by ljc123 on 2018/1/15.
 */

public class BothWayProgressBar extends View implements Runnable {
    private Paint mPaint;
    private static final int MAX_COUNT = 5;//时间限制
    private static final int INTERVEL = 20;//动画频率，每隔50毫秒走一次
    private int count;
    private boolean isStart = true;
    private PListener mPListener;

    private float left, right;//左右的X坐标

    public BothWayProgressBar(Context context) {
        super(context);
        init();
    }

    public BothWayProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BothWayProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        count = 0;
    }

    public void pStop() {
        isStart = false;
        count = 0;
        left = 0;
        right = getMeasuredWidth();
        postInvalidate();
    }

    public void pStart() {
        isStart = true;
    }

    public void setPListener(PListener mPListener) {
        this.mPListener = mPListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        left = 0;
        right = getMeasuredWidth();
        delta = ((float) getMeasuredWidth() / 2.0f) / ((float) MAX_COUNT * 1000.0f / (float) INTERVEL);
    }

    private float delta;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(new Rect((int) left, 0, (int) right, getMeasuredHeight()), mPaint);
    }

    @Override
    public void run() {
        while (isStart) {
            count++;
            left += delta;
            right -= delta;
            if (count >= MAX_COUNT * 1000 / INTERVEL) {
                pStop();
                if (this.mPListener != null) {
                    this.mPListener.onEnd();
                }
            }
            postInvalidate();
            try {
                Thread.sleep(INTERVEL);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public interface PListener {
        void onEnd();//计时到期
    }
}

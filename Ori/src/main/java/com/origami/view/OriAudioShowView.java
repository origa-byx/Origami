package com.origami.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

/**
 * @by: origami
 * @date: 2024/7/24 12:04
 * @info:
 **/
public class OriAudioShowView extends View {
    private static final int SIZE_W = 5;
    private static final int SIZE_H = 20;
    private final float[][] tranLineDates = new float[SIZE_W][SIZE_H];

    Path path = new Path();
    Paint mPaint;
    int wStep;
    int canDrawHeight;
    float conVar;
    float dw;

    public OriAudioShowView(Context context) {
        this(context,null);
    }

    public OriAudioShowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OriAudioShowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        setConVar(0.3f);
    }

    public void setConVar(float conVar) {
        this.conVar = conVar;
    }

    public void write(byte[] data){
        int l = data.length / SIZE_H;
        byte[] bytes = new byte[l];
        int f = 0;
        for (int i = 0; i < SIZE_W; i++) {
            System.arraycopy(data, f, bytes, 0, l);
            f += l;
            write(bytes, i);
        }
        invalidate();
    }

    public void write(byte[] data, int pos){
        float[] tranDates = tranLineDates[pos];
        if(data.length >= tranDates.length){
            int sc = data.length / tranDates.length;
            for (int i = 0; i < tranDates.length; i++) {
                int add = 0;
                for (int j = 0; j < sc; j++) {
                    if(i + j >= data.length){
                        continue;
                    }
                    add += data[i + j];
                }
                tranDates[i] = (float) Math.abs((float) add / sc / 127.0f) * (i % 2 == 0? 1 : -1);
            }
        }else {
            int sc = tranDates.length / data.length;
            for (int i = 0; i < tranDates.length; i++) {
                for (int j = 0; j < sc; j++) {
                    int index = i * sc + j;
                    if(index >= data.length){
                        tranDates[index] = Math.abs(data[data.length - 1] / 127.0f) * (index % 2 == 0? 1 : -1);
                    }else {
                        tranDates[index] = Math.abs(data[index] / 127.0f) * (index % 2 == 0? 1 : -1);
                    }
                }
            }
        }
//        invalidate();
    }


    private int pt, paddingLeft;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        wStep = (getWidth() - getPaddingLeft() - getPaddingStart() - getPaddingEnd() - getPaddingRight()) / (SIZE_H);
        dw = wStep * this.conVar;
        canDrawHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        pt = getPaddingTop() + canDrawHeight;
        paddingLeft = getPaddingLeft() + getPaddingStart();
        for (int i = 0; i < tranLineDates.length; i++) {
            switch (i){
                case 0:
                    mPaint.setColor(Color.WHITE); break;
                case 1:
                    mPaint.setColor(Color.RED); break;
                case 2:
                    mPaint.setColor(Color.GREEN); break;
                case 3:
                    mPaint.setColor(Color.YELLOW); break;
                case 4:
                    mPaint.setColor(Color.LTGRAY); break;
            }
            drawOneLine(canvas, tranLineDates[i]);
        }
    }

    private void drawOneLine(Canvas canvas, float[] tranDates){
        path.reset();
        float k = 0;
        int x = paddingLeft;
        float y = pt + tranDates[0] * canDrawHeight;
        path.moveTo(x, y);
        for (int i = 1; i < tranDates.length; i++) {
            int x_prev = x;
            float y_prev = y;
            float k_prev = k;
            x = paddingLeft + wStep * i;
            y = pt + tranDates[i] * canDrawHeight;
            if (i == tranDates.length - 1) {
                k = (y - y_prev) / (float) wStep * 2;
            } else {
                k = (pt + tranDates[i + 1] * canDrawHeight - y_prev) / (float) wStep * 2;
            }
            path.cubicTo(
                    x_prev + dw, y_prev + k_prev * dw,
                    x - dw,  y - k * dw,
                    x, y);
        }
        canvas.drawPath(path, mPaint);
    }

}

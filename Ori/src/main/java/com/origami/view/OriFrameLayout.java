package com.origami.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * @by: origami
 * @date: {2021-08-27}
 * @info:
 **/
public class OriFrameLayout extends FrameLayout {

    TouchHandler dispatchTouchHandler,onInterceptTouchHandler,onTouchHandler;

    public OriFrameLayout(Context context) {
        super(context);
    }

    public OriFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OriFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(dispatchTouchHandler != null){
            Boolean aBoolean = dispatchTouchHandler.handlerTouchEvent(ev);
            if(aBoolean != null){ return aBoolean; }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(onInterceptTouchHandler != null){
            Boolean aBoolean = onInterceptTouchHandler.handlerTouchEvent(ev);
            if(aBoolean != null){ return aBoolean; }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onTouchHandler != null){
            Boolean aBoolean = onTouchHandler.handlerTouchEvent(event);
            if(aBoolean != null){ return aBoolean; }
        }
        return super.onTouchEvent(event);
    }

    public void setDispatchTouchHandler(TouchHandler dispatchTouchHandler) {
        this.dispatchTouchHandler = dispatchTouchHandler;
    }

    public void setOnInterceptTouchHandler(TouchHandler onInterceptTouchHandler) {
        this.onInterceptTouchHandler = onInterceptTouchHandler;
    }

    public void setOnTouchHandler(TouchHandler onTouchHandler) {
        this.onTouchHandler = onTouchHandler;
    }

}

package com.origami.origami.base.base_utils;


import android.view.Gravity;

import com.origami.origami.base.EventConfig;
import com.origami.origami.base.OriEventBus;
import com.origami.utils.Dp2px;
import com.origami.utils.UiThreadUtil;
import com.origami.window.GlobalWindowUtil;

/**
 * @by: origami
 * @date: {2021/4/29}
 * @info:
 **/
public class ToastMsg {

    /**
     * 队列模式
     */
    public final static int DEF = 0;

    /**
     * 抢断模式
     */
    public final static int STEAL = 1;


    public String msg;

    public Boolean showIcon;

    public int showType;

    public long showTime;

    public ToastMsg(String msg){
        this(msg,DEF);
    }

    public ToastMsg(String msg, int showType){
        this(msg,showType,2000);
    }

    public ToastMsg(String msg, int showType, long showTime){
        this(msg,showType,showTime,true);
    }

    public ToastMsg(String msg, int showType, long showTime, boolean showIcon) {
        this.msg = msg;
        this.showIcon = showIcon;
        this.showType = showType;
        this.showTime = showTime;
    }

    public static void show_msg(String msg){
        new ToastMsg(msg).show();
    }

    public static void show_msg(String msg, long time){
        new ToastMsg(msg, DEF, time).show();
    }

    public static void show_msg(String msg, Boolean boo){ new ToastMsg(msg, DEF, 2000 , boo).show(); }

    public static void show_msg(String msg, Boolean boo, long time){ new ToastMsg(msg, DEF, time , boo).show(); }

    public void show(){
//        if(!UiThreadUtil.isInit()){
            OriEventBus.triggerEvent(EventConfig.SHOW_TOAST,this);
//            return;
//        }

//        GlobalWindowUtil.builder()
//                .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
//                .setLocation(0, Dp2px.dp2px(50))
//                .setCanTouch(false)
//                .build()
//                .showToast(this);
    }
}

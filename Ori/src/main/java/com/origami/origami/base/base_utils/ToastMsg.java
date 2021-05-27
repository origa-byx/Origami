package com.origami.origami.base.base_utils;


import com.origami.origami.base.EventConfig;
import com.origami.origami.base.OriEventBus;

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

    public boolean showIcon;

    public int showType;

    public long showTime;

    public ToastMsg(String msg){
        this(msg,DEF);
    }

    public ToastMsg(String msg, int showType){
        this(msg,showType,1000);
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

    public void show(){
        OriEventBus.triggerEvent(EventConfig.SHOW_TOAST,this);
    }

    public static void show_msg(String msg){
        new ToastMsg(msg).show();
    }

    public static void show_msg(String msg, long time){
        new ToastMsg(msg, DEF, time).show();
    }
}

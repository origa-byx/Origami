package com.origami.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;


/**
 * @by: origami
 * @date: {2021-08-30}
 * @info:
 **/
public class UiThreadUtil {
    public synchronized static void init(Application context){
        if(instance == null){ instance = new UiThreadUtil(context); }
    }
    public static boolean isInit(){ return instance != null; }
    public synchronized static UiThreadUtil getInstance(){
        if(instance == null){ throw new RuntimeException("you must invoke init method at first"); }
        return instance;
    }
    private static UiThreadUtil instance;
    private final Handler uiHandler;
    private UiThreadUtil(Context context){
        uiHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.obj instanceof Runnable){ ((Runnable) msg.obj).run(); }
            }
        };
    }

    public void runOnUiThread(Runnable runnable){
        Message message = new Message();
        message.obj = runnable;
        uiHandler.sendMessage(message);
    }

    /**
     * 使用如 {@link Handler#sendMessageDelayed(Message, long)}
     *          等一系列延时消息时，可能会存在内存泄漏的风险！必要时手动 remove
     * @return Handler
     */
    public Handler getUiHandler(){
        return uiHandler;
    }

    public void runOnUiThread(Activity activity, Runnable runnable){
        activity.runOnUiThread(runnable);
    }


}

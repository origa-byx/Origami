package com.origami.origami.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.origami.log.OriLog;
import com.origami.origami.BuildConfig;
import com.origami.utils.UiThreadUtil;

import java.net.DatagramSocket;

/**
 * @by: origami
 * @date: {2021/5/10}
 * @info:
 **/
public class App extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        UiThreadUtil.init(this);
        OriLog.init(this, BuildConfig.DEBUG);
    }

}

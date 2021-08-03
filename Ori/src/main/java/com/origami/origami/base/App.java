package com.origami.origami.base;

import android.app.Application;
import android.os.Build;

import com.origami.log.OriLog;
import com.origami.origami.BuildConfig;

import java.net.DatagramSocket;

/**
 * @by: origami
 * @date: {2021/5/10}
 * @info:
 **/
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OriLog.init(this, BuildConfig.DEBUG);
    }
}

package com.safone.origami.base;

import android.app.Application;

import java.net.DatagramSocket;

/**
 * @by: origami
 * @date: {2021/5/10}
 * @info:
 **/
public class App extends Application {

    public static DatagramSocket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

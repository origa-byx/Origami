package com.ori.origami;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

/**
 * @by: origami
 * @date: {2021-10-21}
 * @info:
 **/
public class NativeOriPlay {

    static {
        System.loadLibrary("ori_hyn");
    }

    //C++ 存放类对象地址
    @SuppressWarnings("unused")
    private long native_obj_ptr;

    public NativeOriPlay(Surface surface) {
        setNativeWindow(surface, 30);
    }

    /**
     * 绑定将要渲染的 Surface
     * @param surface surface
     * @param av_max   解码，帧队列最大容量(越大内存消耗越大)
     */
    private native void setNativeWindow(Surface surface, int av_max);

    /**
     * 将会自动播放
     * @param url
     */
    public native void setUrl(String url);

    public native boolean isPlay();

    public native void play();

    public native void stop();

    public native void release();

}

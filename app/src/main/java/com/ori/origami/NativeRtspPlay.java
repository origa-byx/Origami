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
public class NativeRtspPlay {

    static {
        System.loadLibrary("ori_rtsp");
    }

    //C++ 存放类对象地址
    private long native_obj_ptr;

    public NativeRtspPlay(Surface surface) {
        setNativeWindow(surface);
    }

    private native void setNativeWindow(Surface surface);

    public native void setUrl(String rtsp_url);

    public native boolean isPlay();

    public native void play();

    public native void stop();

    public native void release();

}

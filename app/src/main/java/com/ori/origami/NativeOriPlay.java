package com.ori.origami;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.origami.utils.Ori;

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
    private Context context;

    public NativeOriPlay(Surface surface, Context context) {
        this.context = context;
        setNativeWindow(surface, 120, true, true);
    }

    public NativeOriPlay(Surface surface, Context context, boolean enableVideo, boolean enableAudio) {
        this.context = context;
        setNativeWindow(surface, 120, enableVideo, enableAudio);
    }
    public boolean d(String a){return false;}
    int i = 0;
    public int doOtherByOneFrame(int[] pxs, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(pxs, width, height, Bitmap.Config.ARGB_8888);
        String gc = Ori.saveBitmap(context, bitmap, "gc", "bm_" + i + ".jpg");
        i++;
        Log.e("ORI", String.format("size: %s, w: %s, h: %s, path: %s", pxs.length, width, height, gc));
        return 0;
    }

    public NativeOriPlay g(){return null;}

    /**
     * 绑定将要渲染的 Surface
     * @param surface surface
     * @param av_max   解码，帧队列最大容量(越大内存消耗越大)
     */
    private native void setNativeWindow(Surface surface, int av_max, boolean Dv, boolean Da);

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

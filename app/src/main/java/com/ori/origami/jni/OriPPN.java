package com.ori.origami.jni;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * @by: origami
 * @date: {2023/7/19}
 * @info:
 **/
public class OriPPN {

    static {
        System.loadLibrary("ori_ppn");
    }

    private OriPPN() {}

    private long oriPpn_obj;

    public static OriPPN instance(String path, int w, int h){
        OriPPN oriPPN = new OriPPN();
        oriPPN.init(path, w, h);
        return oriPPN;
    }

    private native void init(String path, int w, int h);

    public native void write(byte[] yuv420);

    public native void writeYUV420(byte[] y, byte[] u, byte[] v);

    public native void writeNV21(byte[] y, byte[] uv);

    public native void end();

}

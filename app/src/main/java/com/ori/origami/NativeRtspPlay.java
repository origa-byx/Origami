package com.ori.origami;

/**
 * @by: origami
 * @date: {2021-10-21}
 * @info:
 **/
public class NativeRtspPlay {

    static {
        System.loadLibrary("oriRtsp");
    }

    public native void setUrl(String rtsp_url);
}

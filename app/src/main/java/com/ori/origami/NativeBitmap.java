package com.ori.origami;

import android.graphics.Bitmap;

/**
 * @by: origami
 * @date: {2021-06-25}
 * @info:
 **/
public class NativeBitmap {

    static {
        System.loadLibrary("ori_bitmap");
    }

    public static native int testBitmap(Bitmap bitmap);

}

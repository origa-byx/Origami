package com.ori.origami;

/**
 * @by: origami
 * @date: {2021-06-01}
 * @info:
 **/
public class Test {

    static {
        System.loadLibrary("origami");
    }

    public static native int nativeTest(byte[] var_in, int var_in_length, byte[] out_a, byte[] out_b, boolean[] out_c ,boolean[] out_d , boolean[] out_bid);
}

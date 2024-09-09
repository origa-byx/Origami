package com.ori.origami.jni;

import android.util.Log;

import com.origami.origami.base.utils.OriAdapter;
import com.origami.utils.Ori;

/**
 * @by: origami
 * @date: 2024/7/16 9:36
 * @info:
 **/
public class OriAmr {

    static {
        System.loadLibrary("ori_amr");
    }
    private long cxxObj;

    public static void test(){
        OriAmr oriAmr = new OriAmr();
        String data = "F444418C0CEFB3ACE834C0003AD421ED3FF59090060206B9408AA5A44A521272D488A2419E300A002C4622E4C931961D2B064375EC16C22232EEABFC96";
        oriAmr.decode(Ori.HexStringToByteArray(data));
    }

    public OriAmr() {
        init();
    }

    public void callBack(byte[] buffer){
        Log.e("TAG", Ori.ByteArrayToHexString(buffer));
    }

    private native void init();
    public native void decode(byte[] data);
    public native void realase();

}

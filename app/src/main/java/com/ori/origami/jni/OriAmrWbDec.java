package com.ori.origami.jni;

import android.content.Context;
import android.media.AudioFormat;
import android.util.Log;

import androidx.core.util.Consumer;

import com.ori.origami.AudioPlay;
import com.origami.utils.Ori;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @by: origami
 * @date: 2024/7/16 16:06
 * @info:
 **/
public class OriAmrWbDec {
    static {
        System.loadLibrary("ori_amr_wb_dec");
    }
    AudioPlay audioPlay;
    private long cxxObj;
    private Consumer<byte[]> next;
    public void callBack(byte[] buffer){
        if(next != null) next.accept(buffer);
//        Log.e("TAG", String.format("[%s] %s", buffer.length, Ori.ByteArrayToHexString(buffer)));
//        audioPlay.write(buffer, 0, buffer.length);
    }

    public OriAmrWbDec() {
        init();
        audioPlay = new AudioPlay(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioPlay.startPlay();
    }

    public OriAmrWbDec(Consumer<byte[]> next) {
        init();
        this.next = next;
//        audioPlay = new AudioPlay(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        audioPlay.startPlay();
    }

    public static int toInt_s(byte[] bytes, int from , int num){
        int re = 0;
        for (int i = (num - 1); i >= 0; i--) {
            re = re | (bytes[from + i] & 0xff) << (8 * i);
        }
        return re;
    }

    public static void test(Context context, Consumer<byte[]> next){
        byte[] bytes = new byte[12];
        OriAmrWbDec oriAmr = new OriAmrWbDec(next);
        try (InputStream open = context.getAssets().open("AMR-WB-16000-1-true.t")) {
//            int i = 0;
            long stamp = 0;
            long s = System.currentTimeMillis();
            do {
                int read = open.read(bytes);
                if (read == 12) {
                    int intS = toInt_s(bytes, 0, 4);
                    long stamp_now = toInt_s(bytes, 4, 8);
                    byte[] body = new byte[intS];
                    int re = open.read(body);
                    if(stamp != 0){
                        try {
                            long a = stamp_now - stamp - (System.currentTimeMillis() - s);
//                            Log.e("TAG", "a: " + a);
                            TimeUnit.MILLISECONDS.sleep(Math.max(a, 0));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    stamp = stamp_now;
                    s = System.currentTimeMillis();
                    oriAmr.decode(body, 0);
                    if(re < intS) break;
                }
            }while (true);
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
       oriAmr.exit();
    }

    private native void init();
    private native void decode(byte[] data, int bfi);
    private native void exit();

}

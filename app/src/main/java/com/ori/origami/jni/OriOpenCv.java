package com.ori.origami.jni;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.core.util.Consumer;

/**
 * @by: origami
 * @date: {2023/2/24}
 * @info:
 **/
public class OriOpenCv {

    public interface Consumer2<I, II>{
        void run(I i, II ii);
    }

    private final Consumer2<Integer, Bitmap> cz_change_run;
    private final Consumer<Bitmap> end;
    private final Bitmap bitmap;
    private final int[] pxs;
    private final int w;
    private final int h;

    public OriOpenCv(Bitmap bitmap, Consumer2<Integer, Bitmap> cz_change_run, Consumer<Bitmap> end) {
        this.cz_change_run = cz_change_run;
        this.w = bitmap.getWidth();
        this.h = bitmap.getHeight();
        this.bitmap = bitmap;
        this.pxs = new int[w * h];
        this.end = end;
        bitmap.getPixels(pxs, 0, w, 0, 0, w, h);
    }

    static {
        System.loadLibrary("oricv");
    }

    public native void doCv(Bitmap bitmap);

    public void bitmapChange(int cz_name){
        cz_change_run.run(cz_name, bitmap);
    }

    public void doCvNext(int x, int y, int width, int height){
        Log.e("ORI", String.format("ret-> x: %s, y: %s, width: %s, height: %s",
                x, y, width, height));
        if(width < 0){
            width = w;height = h;
            x = 0;y = 0;
        }
        Bitmap bitmap_end = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap_end.setPixels(pxs, w * y + x,  w,0, 0, width, height);
        end.accept(bitmap_end);
    }

}

package com.ori.origami;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.origami.utils.Ori;

import java.io.InputStream;

/**
 * @by: origami
 * @date: {2021-06-25}
 * @info:
 **/
public class NativeBitmap {

    static {
        System.loadLibrary("ori_bitmap");
    }

    public static native int testBitmap(Bitmap bitmap, int radius);


    public static void ps(String path, Context context){
        Log.e("ORI","PS");
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inMutable = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
        int[] px = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(px, 0, bitmap.getWidth(), 0 ,0 , bitmap.getWidth(), bitmap.getHeight());
        int a = px[0];
        Log.e("ORI","ps_>:" + a);
        for (int i = 0; i < px.length; i++) {
            if(px[i] == a){ px[i] = Color.TRANSPARENT; }
        }
        bitmap.setPixels(px, 0, bitmap.getWidth(), 0 ,0 , bitmap.getWidth(), bitmap.getHeight());
        String path1 = Ori.saveBitmapWithAppNamePath(bitmap, context, true, Bitmap.CompressFormat.PNG);
        Log.e("ORI","PSPS:" + path1);
    }

}

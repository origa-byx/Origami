package com.origami.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;

import com.origami.log.OriLog;
import com.origami.log.OriLogBean;
import com.origami.origami.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @by: origami
 * @date: {2021-06-07}
 * @info:
 **/
public class Ori {

    /**
     * md5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 震动
     * @param context
     * @param time  震动时长
     */
    public static void vibrator(Context context, long time){
        Vibrator vibrator = getVibrator(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(time);
        }
    }

    /**
     * 获取震动控制类
     * @param context
     * @return  震动类
     */
    public static Vibrator getVibrator(Context context){
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 获取渐变色 Drawable
     * @param colors 渐变颜色组
     * @return 渐变色
     */
    public static GradientDrawable getGradientDrawable(int[] colors){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColors(colors);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        drawable.setSize(50,50);
        return drawable;
    }

    /**
     * selector Drawable
     * @param drawable_ok
     * @param status
     * @param drawable_notOk
     * @return
     */
    public static StateListDrawable getStateListDrawable(Drawable drawable_ok, @AttrRes int status, Drawable drawable_notOk){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{ status }, drawable_ok);
        drawable.addState(new int[]{ -status }, drawable_notOk);
        return drawable;
    }

    /**
     * 大概就是生成默认的selector drawable 给 一个按钮
     * @param context
     * @param color_out
     * @param color_in
     * @param okIsHasBor
     * @return
     */
    public static StateListDrawable getDefSelectorBackgroundButtonDrawable(Context context, int color_out, int color_in, boolean okIsHasBor){
        Drawable drawable = context.getResources().getDrawable(R.drawable._ori_back_blue_per);
        Drawable drawable_anti = context.getResources().getDrawable(R.drawable._ori_back_blue_nor);
        if(drawable instanceof LayerDrawable){
            Drawable drawable_son1 = ((LayerDrawable) drawable).getDrawable(0);
            Drawable drawable_son2 = ((LayerDrawable) drawable).getDrawable(1);
            if(drawable_son1 instanceof GradientDrawable){
                ((GradientDrawable) drawable_son1).setColor(color_out);
            }
            if(drawable_son2 instanceof GradientDrawable){
                ((GradientDrawable) drawable_son2).setColor(color_in);
            }
        }
        if(drawable_anti instanceof GradientDrawable){
            ((GradientDrawable) drawable_anti).setColor(color_out);
        }
        if(okIsHasBor){
            return getStateListDrawable(drawable_anti, android.R.attr.state_pressed, drawable);
        }else {
            return getStateListDrawable(drawable, android.R.attr.state_pressed, drawable_anti);
        }
    }

    /**
     * 配合{@link #getDefSelectorBackgroundButtonDrawable(Context, int, int, boolean)} }
     *      用来生成颜色的
     * @param color_nor
     * @param color_per
     * @param okIsHasBor
     * @return
     */
    public static ColorStateList getSelectorColorStateList(int color_nor, int color_per, boolean okIsHasBor){
        ColorDrawable drawable_nor = new ColorDrawable();
        ColorDrawable drawable_per = new ColorDrawable();
        drawable_nor.setColor(color_nor);
        drawable_per.setColor(color_per);
        int[] colors = new int[2];
        if(okIsHasBor){
            colors[1] = color_nor; colors[0] = color_per;
        }else {
            colors[0] = color_nor; colors[1] = color_per;
        }
        return new ColorStateList(new int[][]{{ android.R.attr.state_pressed }, {}}, colors);
    }

    /**
     * 生成随机字符串 带时间
     * @param length    时间附加的随机字符长度
     * @return
     */
    public static String getRandomString(@IntRange(from = 6) int length){
        String ran = "abcdefghijkmlnopqrstuvwxyz123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        Calendar date = Calendar.getInstance();
        builder.append(date.get(Calendar.YEAR))
                .append(date.get(Calendar.MONTH) < 9 ? "0" + (date.get(Calendar.MONTH) + 1) : (date.get(Calendar.MONTH) + 1))
                .append(date.get(Calendar.DATE) < 10 ? "0" + date.get(Calendar.DATE) : date.get(Calendar.DATE));
        for (int i = 0; i < length; i++) {
            builder.append(ran.charAt(random.nextInt(ran.length())));
        }
        return builder.toString();
    }


    /**
     * 产生随机文件名，对于可能会有大量文件的情况下，防止单一文件夹内文件过多导致的读取缓慢生成随机文件夹
     * @param obj  任意对象进行随机文件夹的生成  -> ran charAt -> obj.hashCode() % ran.length()
     * @return
     */
    public static String getRandomFileString(Object obj, @IntRange(from = 6) int length){
        String ran = "abcdefghijkmlnopqrstuvwxyz123456789";
        StringBuilder builder = new StringBuilder();
        int index = obj.hashCode() % ran.length();
        builder.append(ran.charAt(index)).append(File.separator).append(getRandomString(length));
        return builder.toString();
    }

    /**
     * 保存位图
     * @param path 例如: "test/image/head"
     * @return null ：保存失败
     */
    public static String saveBitmap(Bitmap mBitmap, String path) {
        return saveBitmap(mBitmap, path, null);
    }
    public static String saveBitmap(Bitmap mBitmap, String path, String fileName) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + path + File.separator;
        } else {
            Log.e("ORI", "saveBitmap : sdcard not mounted");
            return null;
        }
        if(TextUtils.isEmpty(fileName)){
            savePath += (getRandomString(8) + ".jpg");
        }else {
            savePath += fileName;
        }
        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                if(filePic.getParentFile() != null) {
                    filePic.getParentFile().mkdirs();
                }
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic, false);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("ORI", "saveBitmap : " + e.getMessage());
            return null;
        }
        return savePath;
    }

    /**
     * 保存位图  默认根目录下以app名命名的image文件夹下
     * @param isRandomFile  是否生成随机文件夹防止单一文件夹类子节点过多
     * @return null ：保存失败
     */
    public static String saveBitmapWithAppNamePath(Bitmap mBitmap, Context context , boolean isRandomFile) {
        String path;
        int labelRes = context.getApplicationInfo().labelRes;
        if(labelRes == 0){
            path = "ori" + File.separator + "image";
        } else {
            path =  context.getResources().getString(labelRes) + File.separator + "image";
        }
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getPath() + File.separator + path + File.separator;
        } else {
            Log.e("ORI", "saveBitmap : sdcard not mounted");
            return null;
        }
        if(isRandomFile){
            savePath += (getRandomFileString(mBitmap, 8) + ".jpg");
        }else {
            savePath += (getRandomString(8) + ".jpg");
        }

        try {
            filePic = new File(savePath);
            if (!filePic.exists()) {
                if(filePic.getParentFile() != null) {
                    filePic.getParentFile().mkdirs();
                }
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e("ORI", "saveBitmap : " + e.getMessage());
            return null;
        }
        return savePath;
    }

    /**
     * 获取统一保存路径
     * @param context
     * @return
     */
    public static String getSaveFilePath(Context context){
        int labelRes = context.getApplicationInfo().labelRes;
        if(labelRes == 0){
            return Environment.getExternalStorageDirectory().getPath() + File.separator + "ori" + File.separator;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + File.separator + context.getResources().getString(labelRes) + File.separator;
        }
    }


    /**
     * bitmap转化位base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap, int... quality) {
        int qualityValue;
        if(quality != null && quality.length > 0){
            qualityValue = quality[0];
        }else {
            qualityValue = 100;
        }
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, qualityValue, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static Bitmap base64ToBitmap(String base64Data) {
        try {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) { }
        return null;
    }

    /**
     * 检测服务是否正在运行
     * @param mContext
     * @param className  -> {@link Class#getName()}
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 转换成16进制字符串
     * @param bytes
     * @return
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 16进制字符串转换为字节数组
     * @param s
     * @return
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    //同步文件
    public void syncFile(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[] { path }, null, null);
    }

    public static void d(String msg, Bitmap... bitmaps){
        OriLog.getInstance().log_print(OriLogBean.d(msg, bitmaps));
    }
    public static void v(String msg, Bitmap... bitmaps){
        OriLog.getInstance().log_print(OriLogBean.v(msg, bitmaps));
    }
    public static void w(String msg, Bitmap... bitmaps){
        OriLog.getInstance().log_print(OriLogBean.w(msg, bitmaps));
    }
    public static void e(String msg, Throwable throwable, Bitmap... bitmaps){
        OriLog.getInstance().log_print(OriLogBean.e(msg, throwable, bitmaps));
    }
    public static void e(Throwable throwable){
        OriLog.getInstance().log_print(OriLogBean.e(throwable));
    }

}

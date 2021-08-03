package com.origami.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.IntRange;

import com.origami.log.OriLog;
import com.origami.log.OriLogBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
     * 保存位图
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
     *
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
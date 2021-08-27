package com.origami.log;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.origami.utils.Ori;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @by: origami
 * @date: {2021-07-22}
 * @info:
 **/
public class OriLogBean {

    private static final String[] safeArr = new String[]{"&","<",">"};
    private static final String[] replaceArr = new String[]{"&amp;","&lt;","&gt;"};

    public static final String BASE_HTML = "<style>" +
            "body{padding:20px}" +
            "div{color:#000000;text-shadow: 1px 1px 5px #ffffff;border-radius:5px;word-break:break-all}" +
            "img {width: 30%;height: auto;float:left;z-index:-1;margin-right:5px}" +
            ".d{background-color:#eeeeee}" +
            ".v{background-color:#0aa858}" +
            ".e{background-color:#f4433c;}" +
            ".w{background-color:#ffbc32;}" +
            ".t{ width:20%;margin-right:5px;text-align:center;float:left;padding:5px 0px;margin-bottom:3px; }" +
            ".m{ width:78%;float:left;padding:5px 10px;margin-bottom:3px;box-sizing: border-box; }" +
            "</style>\n";

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    //d v e w
    private final String level;
    public String msg;
    public Throwable throwable;

    private OriLogBean(String level, String msg, Throwable throwable) {
        this.level = level;
        this.msg = toSafeString(msg);
        this.throwable = throwable;
    }

    private static String toSafeString(String msg){
        for (int i = 0; i < safeArr.length; i++) {
            msg = msg.replaceAll(safeArr[i], replaceArr[i]);
        }
        return msg;
    }

    public static OriLogBean d(String msg, Bitmap... bitmaps){
        return new OriLogBean("d", msg + getBitmapLogString(bitmaps), null);
    }
    public static OriLogBean v(String msg, Bitmap... bitmaps){
        return new OriLogBean("v", msg + getBitmapLogString(bitmaps), null);
    }
    public static OriLogBean w(String msg, Bitmap... bitmaps){
        return new OriLogBean("w", msg + getBitmapLogString(bitmaps), null);
    }
    public static OriLogBean e(String msg, Throwable throwable, Bitmap... bitmaps){
        return new OriLogBean("e", msg + "::" + getThrowableMsg(throwable) + getBitmapLogString(bitmaps), throwable);
    }
    public static OriLogBean e(Throwable throwable){
        return new OriLogBean("e", getThrowableMsg(throwable), throwable);
    }

    private static String getThrowableMsg(Throwable throwable){
        return throwable == null? "" : throwable.getMessage();
    }

    private static String getBitmapLogString(Bitmap... bitmaps){
        if(bitmaps != null && bitmaps.length > 0){
            StringBuilder builder = new StringBuilder("<img src=\"data:image/jpeg;base64,");
            for (Bitmap bitmap : bitmaps) {
                builder.append(Ori.bitmapToBase64(bitmap, 10));
            }
            builder.append("\"/>");
            return builder.toString();
        }
        return "";
    }

    public String toHtml(){
        StringBuilder placeString = new StringBuilder(msg);
        if(throwable != null){
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            if(stackTrace.length > 0 && stackTrace[0] != null){
                placeString.append(String.format("<font color=\"#3b50ce\">(%s&lt;%s&gt;:%s)</font>"
                        ,stackTrace[0].getClassName()
                        ,stackTrace[0].getMethodName()
                        ,stackTrace[0].getLineNumber()
                ));
            }
        }
        return String.format(
                "<div class=\"t %s\">%s</div><div class=\"m %s\">%s</div>",
                level, dateFormat.format(new Date()), level, placeString
        );
    }

}

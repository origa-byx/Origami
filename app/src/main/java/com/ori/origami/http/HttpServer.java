package com.ori.origami.http;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fi.iki.elonen.NanoHTTPD;

/**
 * @by: origami
 * @date: {2022/3/9}
 * @info:
 **/
public class HttpServer extends Service {

    MNanoHTTPD mNanoHTTPD;

    static class MNanoHTTPD extends NanoHTTPD{

        private final Map<String, Map<Object, Object>> sessionMap = new HashMap<>();

        public MNanoHTTPD(int port) {
            super(port);
        }

        public MNanoHTTPD(String hostname, int port) {
            super(hostname, port);
        }

//        http://192.168.0.110/api/getFile?path=pixiv,illust_96637220_20200308_083900.png
        @Override
        public Response serve(IHTTPSession session) {
            String uri = session.getUri();
            String sessionId = session.getCookies().read("sessionId");
            if(sessionId == null) {
                sessionId = getRandomString(8);
                session.getCookies().set("sessionId", sessionId, 1);
                sessionMap.put(sessionId, new HashMap<>());
            }
            if(uri.matches("/api/pixiv.*")){
                int index = 0;
                if(!uri.equals("/api/pixiv")) {
                    String[] split = uri.split("/api/pixiv/");
                    try {
                        index = Integer.parseInt(split[split.length - 1]);
                    } catch (NumberFormatException ignored) { }
                }
                Log.e("ORI", "index->" + index);
                Response response = getPixiv(index);
                if(response != null) return response;
            }else if(uri.matches("/api/image/.+/.+") && session.getMethod() == Method.GET){
                String[] split = uri.split("/api/image/");
                Response response = getFile(split[split.length - 1]);
                if(response != null) return response;
            } else if(uri.matches("/api/audio/.+/.+") && session.getMethod() == Method.GET){
                String[] split = uri.split("/api/audio/");
                Response response = getAudio(split[split.length - 1]);
                if(response != null) return response;
            }
            return super.serve(session);
        }

        //http://192.168.0.110:9853/api/pixiv/%s
        public static final String HTML =
                "<a href=\"http://192.168.0.110:9853/api/image/pixiv/%s\">" +
                "<image src=\"http://192.168.0.110:9853/api/image/pixiv/%s\" style=\"margin:auto; width:30%%; height:auto ; float:left\"/>" +
                "</a>";
        private Response getPixiv(int index) {
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .getPath() + File.separator + "pixiv";
            File file = new File(directory);
            if(file.exists() && file.isDirectory()){
                File[] files = file.listFiles();
                if(files != null) {
                    StringBuilder builder = new StringBuilder("<html>");
                    Log.e("ORI", "files-> " + files.length);
                    int start = Math.max(30 * index, 0);
                    int end = Math.min(30 * (index + 1), files.length - 1);
                    for (;start < end; start++){
                        if(files[start] != null) {
                            String[] split = files[start].getPath().split("pixiv/");
                            String s = split[split.length - 1];
                            String html = String.format(HTML, s, s);
                            builder.append(html);
                        }
                    }
                    builder.append("</html>");
//                    File src = files[Math.min(Math.max(0, index), files.length - 1)];
//                    if(src != null) {
//                        String[] split = src.getPath().split("pixiv/");
//                        String html = String.format(HTML, (index + 1), split[split.length - 1]);
//                        Log.e("ORI", "html-> " + html);
                        byte[] bytes = builder.toString().getBytes(StandardCharsets.UTF_8);
                        return newFixedLengthResponse(Response.Status.OK, "text/html", new ByteArrayInputStream(bytes), bytes.length);
//                    }
                }
            }
            return null;
        }

        /**
         * 生成随机字符串 带时间
         * @param length    时间附加的随机字符长度
         * @return
         */
        public static String getRandomString(int length){
            String ran = "abcdefghijkmlnopqrstuvwxyz0123456789";
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

        private Response getAudio(String pathAdd){
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + pathAdd;
            Log.e("ORI", path);
            File file = new File(path);
            if(!file.exists() || file.isDirectory()) {
                Log.e("ORI", "exists:" + file.exists() + "  isDir:" + file.isDirectory());
                return null;
            }
            try {
                InputStream inputStream = new FileInputStream(file);
                String mimeType = String.format("video/%s", "mp4");
                Log.e("ORI", "return");
                return newFixedLengthResponse(Response.Status.OK, mimeType, inputStream, file.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("ORI", e.getMessage());
            }
            return null;
        }

        private Response getFile(String pathAdd) {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pathname = directory.getPath() + File.separator + pathAdd;
            Log.e("ORI", pathname);
            File file = new File(pathname);
            if(!file.exists() || file.isDirectory()) {
                Log.e("ORI", "exists:" + file.exists() + "  isDir:" + file.isDirectory());
                return null;
            }
            try {
                InputStream inputStream = new FileInputStream(file);
                String mimeType = String.format("image/%s", "jpeg");
                Log.e("ORI", "return");
                return newFixedLengthResponse(Response.Status.OK, mimeType, inputStream, file.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("ORI", e.getMessage());
                return null;
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mNanoHTTPD = new MNanoHTTPD(9853);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mNanoHTTPD != null){
            Log.e("ORI", "start");
            try {
                mNanoHTTPD.start(20000);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ORI", e.getMessage());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

}

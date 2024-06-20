package com.origami.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.origami.App;
import com.origami.origami.R;
import com.origami.utils.Ori;
import com.origami.window.NotificationChannelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @by: origami
 * @date: {2021-09-29}
 * @info:  前台服务  更新进度
 * 下载完成自动执行安装，完成时点击前台服务亦可安装
 * 继承此类  并在 清单文件 中注册 Service
 *    启动服务时 eg：
 *         Intent intent = new Intent({@link Context}, @{@link Class<? extends AppUpdate_service>});
 *         intent.putExtra({@link #PARAM_URL}, "your download_url");
 *         startService(intent);
 **/
public abstract class AppUpdate_service extends Service {

    public static final String PARAM_URL = "param_url";
    private static final String TAG = "AppUpdate_service";

    /**
     * @return app icon
     */
    @DrawableRes
    public abstract int getAppIcon();

    /**
     * @return app name
     */
    public abstract String getAppName(Context context);

    /**
     * @return  你的 FileProvider的 authority 值
     */
    public abstract String getFileProviderAuthority(Context context);

    private final String click_action = App.APP_ID + ".AppUpdate_service.click";
    private final int NOTIFY_ID = 7;

    String downUrl;
    RemoteViews remoteViews;
    NotificationManager manager;
    Notification notification;
    File fileAkp;
    boolean isReally = false;
    ActionHandler actionHandler = new ActionHandler();

    class ActionHandler extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(click_action) && isReally && fileAkp != null){
                install(fileAkp, context);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        Ori.d("DOWN", "GO");
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        remoteViews = new RemoteViews(this.getPackageName(), R.layout._notify_app_update);
        remoteViews.setTextViewText(R.id.notify_a_update_app_name, getAppName(App.appContext));
        remoteViews.setImageViewResource(R.id.notify_a_update_app_icon, getAppIcon());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews);
        }else {
            builder.setContent(remoteViews);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(click_action);
        registerReceiver(actionHandler, intentFilter);
        Intent intent = new Intent(click_action);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
//        remoteViews.setOnClickPendingIntent(,pendingIntent);
        manager = (NotificationManager) this.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
            NotificationChannelUtil.createNotificationChannel(manager, "ori_app_update","APP更新-下载进度",
                    NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId("ori_app_update");
        }
        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(getAppIcon());
        notification = builder.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){ downUrl = intent.getStringExtra(PARAM_URL); }else { stopSelf(); }
        if(TextUtils.isEmpty(downUrl)){
            Ori.e(TAG, new Exception("缺失下载地址参数-> " + PARAM_URL));
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        startForeground(NOTIFY_ID, notification);
        FileC fileC = new FileC() {
            @Override
            public void onOk(File file) {
                fileAkp = file;
                isReally = true;
                remoteViews.setTextViewText(R.id.notify_a_update_text, "下载完成");
                remoteViews.setInt(R.id.notify_a_update_progress, "setProgress", 100);
                manager.notify(NOTIFY_ID, notification);
                install(fileAkp, App.appContext);
            }

            @Override
            public void onP(float p) {
                int endProgress = (int) (p * 100);
                remoteViews.setTextViewText(R.id.notify_a_update_text, endProgress + "%");
                remoteViews.setInt(R.id.notify_a_update_progress, "setProgress", endProgress);
                manager.notify(NOTIFY_ID, notification);
            }

            @Override
            public void onE(Exception e) {
                Ori.e(TAG,"下载出错" , e);
            }
        };
        down(fileC);
        return super.onStartCommand(intent, flags, startId);
    }


    private void down(FileC fileC){
        OkHttpClient builder = new OkHttpClient.Builder().build();
        Retrofit build = new Retrofit.Builder().client(builder).baseUrl("https://dl.hdslb.com").build();
        AppUpdateApi appUpdateApi = build.create(AppUpdateApi.class);
        Call<ResponseBody> responseBodyCall = appUpdateApi.downFile(downUrl);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                String name = getCacheDir().getAbsoluteFile() + File.separator + "base.apk";
                Ori.d("DOWN", "文件地址： " + name);
                File file = new File(name);
                if(!file.exists()){
                    if(file.getParentFile() != null && !file.getParentFile().exists()){
                        if (!file.getParentFile().mkdirs()) {
                            fileC.onE(new IOException("mkdirs failed"));
                            return;
                        }
                    }
                    try {
                        if (!file.createNewFile()) {
                            fileC.onE(new IOException("createNewFile failed"));
                            return;
                        }
                    } catch (IOException e) {
                        fileC.onE(e);
                    }
                }
                try (ResponseBody body = response.body();
                     FileOutputStream stream = new FileOutputStream(file))
                {
                    if(body == null) {
                        fileC.onE(new IOException("body is null"));
                        return;
                    }
                    InputStream inputStream = body.byteStream();
                    long total = body.contentLength();
                    Ori.d("DOWN", "总大小： " + total);
                    byte[] buffer = new byte[1024 * 512];
                    int len;
                    int current = 0;
                    float last = 0;
                    while ((len = inputStream.read(buffer)) != -1){
                        stream.write(buffer, 0, len);
                        current += len;
                        if(total != 0){
                            float p = (float) (current) / total;
                            Ori.d("DOWN", "当前读取大小： " + len + " current: " + current + " p: " + p);
                            if(p - last > 0.01){
                                last = p;
                                fileC.onP(p);
                            }
                        }
                    }
                    stream.flush();
                    fileC.onOk(file);
                } catch (IOException e) {
                    fileC.onE(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                fileC.onE(new IOException("" + t.getMessage()));
            }
        });
    }

    public void install(File apkFile, Context mContext) {
        if (mContext == null) { return; }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//android 版本大于7之后需要增加 FileProvider
            // 这里必须用 addFlags  不然会覆盖之前的Flags
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, getFileProviderAuthority(App.appContext), apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

}

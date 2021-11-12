package com.origami;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.origami.log.OriLog;
import com.origami.origami.BuildConfig;
import com.origami.utils.BaseErrorHandler;
import com.origami.utils.UiThreadUtil;

/**
 * @by: origami
 * @date: {2021/5/10}
 * @info:
 *  build.gradle need add -> dataBinding{ enabled = true }
 *  your Application extends {@link App}
 *  派蒙，最好的伙伴！
 * ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠒⠛⠲⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠀⠀⡀⠀⢀⠀⢻⡄⣠⠶⣆⠀⣸⣀⣀⠀⠀⡀⠀⢀⠀⢀⠀⠀
 * ⠀⠀⠀⠁⠀⠀⢀⡠⠬⠛⢓⣏⠉⣾⣉⣀⠉⢹⡀⠀⠀⠀⠀⠀⠀⠀
 * ⠀⠀⠐⠀⢀⡖⠋⣠⠴⠛⠙⢹⠞⢳⢀⣨⡵⠚⠀⠀⠀⠐⠀⠀⠂⠀
 * ⠀⠀⠀⣰⠋⡠⠎⠁⣀⠤⠒⠚⠛⠙⠒⠳⠤⣄⡀⠀⠠⠀⠀⠄⠀⠠
 * ⠀⠀⠀⠘⠐⢼⠖⠋⠀⠀⢀⠀⠀⠀⠀⠀⠀⠘⣌⡒⠲⢹⠀⠀⠀⠀
 * ⠀⠀⠈⠀⡸⠁⠀⠀⠀⠀⡆⠀⠀⠐⠀⠢⣄⠀⡽⡙⡲⠑⠒⠒⡒⠁
 * ⢀⡠⠴⠚⠀⠀⠀⠀⠀⣕⠝⣄⡀⢀⠀⠀⡇⠵⢍⠚⢾⡀⢠⠖⠁⠀
 * ⠈⠦⣄⣀⠀⡔⠀⠀⢁⡞⠀⠉⠲⣄⡀⢲⢼⠀⢀⠳⡄⠁⠀⢣⠀⠀
 * ⠀⠀⣠⠃⢐⠄⠀⠀⠴⠅⠠⡊⡢⠀⠉⠉⠁⠀⢆⠕⠹⡀⠀⠈⡆⠀
 * ⠀⠠⡇⠀⡸⠀⠀⠀⠨⡅⠀⠒⠈⠀⢄⠠⠠⠔⠀⠀⠀⢻⠀⠀⢣⠀
 * ⠀⢸⠅⠀⡕⠀⠀⠀⠀⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⡏⠀⠀⢸⠀
 * ⠀⠈⡇⠀⣣⠀⠀⠈⠀⠸⡦⠴⠲⢚⢚⠙⠝⠙⠍⠝⣱⠏⢠⠀⢸⠅
 * ⠀⠀⠙⣆⠘⣄⠀⠠⣄⠀⠹⣌⠌⠀⠂⠐⢈⠄⡁⢌⠳⣺⠏⢀⡞⠀
 * ⠀⠀⠀⠀⠙⠺⠛⣲⠜⠟⡓⡚⣏⣔⡀⡌⣀⢂⣔⠴⠋⢏⠒⠁⠀⠀
 *
 **/
public class App extends Application {

    public static String APP_ID;
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        APP_ID = getApplicationInfo().packageName;
        appContext = this;
        //主线程消息事件循环
        UiThreadUtil.init(this);
        //本地日志
        OriLog.init(this, BuildConfig.DEBUG);
    }

}

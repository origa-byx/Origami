package com.ori.origami;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.origami.origami.base.act.AnnotationActivity;
import com.origami.origami.base.act.AnnotationActivityManager;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.toast.ToastMsg;
import com.origami.window.GlobalWindowUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @by: origami
 * @date: {2021-09-01}
 * @info:
 *          测试结论：
 *              此 act 启动模式 singleInstance  启动这个 act 的 act启动模式为 singleTop
 *              拦截正常的返回键操作 改写为
 *                      Intent intent = new Intent(this, TestActivity.class); //TestActivity为 singleTop
 *                      startActivity(intent);
 *
 *          实现启动一个类似于单例的活动，返回后不销毁依然存在，
 *          可以做个返回后再原来的启动界面显示一个悬浮球之类的代表一个挂起任务，类似于QQ的视频流act返回后侧边栏的小书签
 *
 *          销毁时自己想办法  一般通过{@link AnnotationActivityManager} 这种act管理类来进行{@link #finish()}
 **/
@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_test)
public class Test2Act extends AnnotationActivity {

    Timer timer;
    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e("ORI", "Test2Act: 我还活着-: " + ((System.currentTimeMillis() % 2) == 0?"=3=":"^v^"));
            }
        },1000,2000);
        GlobalWindowUtil.builder()
                .setCanTouch(false)
                .build().showToast(new ToastMsg("开始了开始了"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            Log.e("ORI","Test2Act: 返回");
//            moveTaskToBack(true);
//            return true;

            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        getWindow().getDecorView().findViewById(android.R.id.content)
                .setBackgroundColor(getResources().getColor(R.color._ori_green));
    }

    @SuppressWarnings("unused")
    @BClick(R.id.uii)
    public void onClick_jni(){
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e("ORI","Test2Act: 啊我死了T^T");
        if(timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
    }

}

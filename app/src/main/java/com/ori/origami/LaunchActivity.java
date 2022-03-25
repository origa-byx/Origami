package com.ori.origami;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ori.origami.databinding.ActivityLaunchBinding;
import com.ori.origami.http.HttpServer;
import com.ori.origami.jni.NativeOriTranscoding;
import com.origami.activity.OriImageActivity;
import com.origami.activity.OriImageSelect;
import com.origami.origami.base.act.OriBaseActivity;
import com.origami.origami.base.callback.RequestPermissionNext;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.toast.OriToast;

import com.origami.utils.Dp2px;
import com.origami.utils.Ori;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_launch)
public class LaunchActivity extends OriBaseActivity<ActivityLaunchBinding> {

    NativeRtspPlay nativeRtspPlay, nativeRtspPlay2;

//    private int txt = 0;
//    private int dp = Dp2px.dp2px(10);
//    ValueAnimator animator = new ValueAnimator();
//    {
//        animator.setFloatValues(0, 1);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.setDuration(500);
//        animator.addUpdateListener(anVal -> {
//            float val = (float) anVal.getAnimatedValue();
////            float sc = 1 + (1 - val) * 2;
////            mViews.top.setScaleX(sc);
////            mViews.top.setScaleY(sc);
//            mViews.top.setAlpha(val);
////            float x = dp * (1 - val);
////            int i = ((int) ((1 - val) * 100)) % 2 == 0 ? -1 : 1;
////            mViews.top.setTranslationX(x * i);
////            mViews.top.setTranslationY(x * i);
//        });
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                mViews.top.setText(String.valueOf(txt));
//            }
//        });
//    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
//        mViews.surface.setVisibility(View.GONE);
//        mViews.surface2.setVisibility(View.GONE);

//        Intent intent = new Intent(this, HttpServer.class);
//        startService(intent);

//        new Timer().schedule(new TimerTask() {
//            final Random random = new Random();
//            @Override
//            public void run() {
//                runOnUiThread(()->{
//                    txt = random.nextInt(99);
//                    if(animator.isRunning()) animator.cancel();
//                    animator.start();
//                });
//            }
//        }, 2000, 1000);
    }

    @BClick(R.id.top)
    public void top_c(){
        mViews.surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.e("ORI","surfaceCreated1");
                open(1);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                nativeRtspPlay.release();
            }
        });
        open(1);
    }
    @BClick(R.id.bot)
    public void bot_c(){
        mViews.surface2.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.e("ORI","surfaceCreated2");
                open(2);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                nativeRtspPlay2.release();
            }
        });
        open(2);
    }

    public void open(int index){
        if(index == 1){
            if(nativeRtspPlay == null) {
                nativeRtspPlay = new NativeRtspPlay(mViews.surface.getHolder().getSurface());
                new Thread(() -> nativeRtspPlay.setUrl("http://61.128.208.26:6080/rtp/50010800491320000101_50010800491320000101.live.flv")).start();
            }else {
                nativeRtspPlay.release();
                nativeRtspPlay = null;
            }
        }else {
            if(nativeRtspPlay2 == null) {
                nativeRtspPlay2 = new NativeRtspPlay(mViews.surface2.getHolder().getSurface());
                new Thread(() -> nativeRtspPlay2.setUrl("http://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4")).start();
            }else {
                nativeRtspPlay2.release();
                nativeRtspPlay2 = null;
            }
        }
    }


//    @BClick(R.id.surface)
    public void cs1(){
        if(nativeRtspPlay != null){
            if(nativeRtspPlay.isPlay()){
                nativeRtspPlay.stop();
            }else {
                nativeRtspPlay.play();
            }
        }
    }

    @BClick(R.id.surface2)
    public void cs12(){
        if(nativeRtspPlay2 != null){
            if(nativeRtspPlay2.isPlay()){
                nativeRtspPlay2.stop();
            }else {
                nativeRtspPlay2.play();
            }
        }
    }

//    @BClick(R.id.cl)
    public void onClick(){
//        OriToast.show(Ori.getRandomString(1), true);
//        String saveFilePath = Ori.getSaveFilePath(this);
//        String outPath = saveFilePath + "out.alaw";
//        File file = new File(outPath);
//        if(!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        NativeOriTranscoding nativeOriTranscoding = new NativeOriTranscoding(saveFilePath + "test.pcm", outPath);
//        nativeOriTranscoding.initTranscoding();
////        ToastMsg.show_msg(Ori.getRandomString(8), true, 2000);
//        if(true){return;}
        checkPermissionAndThen(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RequestPermissionNext() {
            @Override
            public void next() {
//                Intent intent = new Intent(LaunchActivity.this, Test_recycler.class);
//                startActivity(intent);
                OriImageSelect.builder()
                        .setSelectNum(5)
                        .setRowShowNum(3)
                        .setRequestCode(123)
                        .setCanPre(true)
                        .build(LaunchActivity.this);
            }

            @Override
            public void failed() {
                OriToast.show("拒绝了权限", false);
            }
        });
    }

    private void goNext(){
        Intent intent = new Intent(LaunchActivity.this, TestActivity.class);
        LaunchActivity.this.startActivity(intent);
        LaunchActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == Activity.RESULT_OK && data != null){
            String[] paths = data.getStringArrayExtra(OriImageSelect.RESULT_KEY);
            if(paths == null || paths.length != 1){ return; }
            doB(paths[0]);
        }
    }

    private void doB(InputStream stream){
        doB(BitmapFactory.decodeStream(stream));
    }

    private void doB(String path){
        doB(BitmapFactory.decodeFile(path));
    }

    private void doB(Bitmap bitmap){
        Editable text = mViews.mub.getText();
        int radius;
        if(text != null && !TextUtils.isEmpty(text.toString())){
            int val;
            try {
                val = Integer.parseInt(text.toString());
            }catch (NumberFormatException e){
                mViews.mub.setText(String.valueOf(50));
                val = 50;
            }
            radius = val;
        }else {
            radius = 50;
        }
        new Thread(() -> {
            OriToast.show("等待处理...", false, false);
            int i = NativeBitmap.testBitmap(bitmap, radius);
            OriToast.show("处理完成->" + i, true, false);
            getWindow().getDecorView().postDelayed(() -> OriImageActivity.startThisAct(LaunchActivity.this, bitmap, true),1000);
        }).start();
    }
}

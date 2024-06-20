package com.ori.origami;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ori.origami.databinding.ActivityLaunchBinding;
import com.origami.activity.OriImageActivity;
import com.origami.activity.OriImageSelect;
import com.origami.origami.base.act.OriBaseActivity;
import com.origami.origami.base.callback.RequestPermissionNext;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.toast.OriToast;
import com.origami.utils.Ori;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import vtb.mashiro.kanon.base.CameraHelper;

@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_launch)
public class LaunchActivity extends OriBaseActivity<ActivityLaunchBinding> {

    NativeOriPlay nativeOriPlay, nativeOriPlay2;

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            checkPermissionAndThen(new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RequestPermissionNext() {
//                @Override
//                public void next() {
//                    top_c();
//                }
//
//                @Override
//                public void failed() {
//                    Log.e("ORI", "权限啊");
//                }
//            });
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        OriToast.show("new intent", true, false);
    }
    CameraHelper cameraHelper = new CameraHelper();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraHelper.closeCamera();
    }

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
        checkPermissionAndThen(new String[]{Manifest.permission.CAMERA}, new RequestPermissionNext() {
            @Override
            public void next() {
                if(false) {
                    cameraHelper.openCamera(LaunchActivity.this, "0", mViews.surface.getHolder().getSurface());
                    UsbManager systemService = (UsbManager) getSystemService(USB_SERVICE);
                    HashMap<String, UsbDevice> deviceList = systemService.getDeviceList();
                    UsbAccessory[] accessoryList = systemService.getAccessoryList();
                    int a, b;
                    if (deviceList == null) a = -1;
                    else a = deviceList.size();
                    if (accessoryList == null) b = -1;
                    else b = accessoryList.length;
                    OriToast.show(String.format("a : %s   b: %s", a, b), true, false);
                }
                mViews.surface.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder holder) {
                        Log.e("ORI", "surface--Created1");
                    }

                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                        Log.e("ORI", "surface--Changed1");
                    }

                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                        if(nativeOriPlay != null){
                            nativeOriPlay.release();
                            nativeOriPlay = null;
                        }
                    }
                });
                mViews.surface2.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder holder) {
                        Log.e("ORI","surface--Created2");
                    }

                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                        Log.e("ORI", "surface--Changed2");
                    }

                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                        if(nativeOriPlay2 != null){
                            nativeOriPlay2.release();
                            nativeOriPlay2 = null;
                        }
                    }
                });
            }

            @Override
            public void failed() {finish();}
        });
    }

    @BClick(R.id.top)
    public void top_c(){
        if(true){
            cameraHelper.end();
            return;
        }
        UsbManager systemService = (UsbManager) getSystemService(USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = systemService.getDeviceList();
        UsbAccessory[] accessoryList = systemService.getAccessoryList();
        int a,b;
        if(deviceList == null) a = -1; else a = deviceList.size();
        if(accessoryList == null) b = -1; else b = accessoryList.length;
        OriToast.show(String.format("a : %s   b: %s", a, b), true, false);
        open(1);
    }
    @BClick(R.id.bot)
    public void bot_c(){
        open(1);
    }

    public void open(int index){
        if(index == 1){
            if(nativeOriPlay == null) {
                nativeOriPlay = new NativeOriPlay(mViews.surface.getHolder().getSurface(), this,
                        true, false);
                final String url =
                        Ori.getSaveFilePath(this, Environment.DIRECTORY_MOVIES) + "test.mp4";
//                String url = "rtsp://admin:a1234567@192.168.0.112:554/h264/ch1/sub/av_stream";
//                    String url = "http://192.168.0.61:8000/www/ED3_Mura720.wmv";
                nativeOriPlay.setUrl(url);
            } else {
                nativeOriPlay.release();
                nativeOriPlay = null;
            }
        }else {
            if(nativeOriPlay2 == null) {
                nativeOriPlay2 = new NativeOriPlay(mViews.surface2.getHolder().getSurface(), this);
//                "http://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4"
                //rtp://239.77.0.86:5146 http://vjs.zencdn.net/v/oceans.mp4
                nativeOriPlay2.setUrl("rtp://239.77.0.86:5146");
            }else {
                nativeOriPlay2.release();
                nativeOriPlay2 = null;
            }
        }
    }


    @BClick(R.id.surface)
    public void cs1(){
        if(nativeOriPlay != null){
            if(nativeOriPlay.isPlay()){
                nativeOriPlay.stop();
            }else {
                nativeOriPlay.play();
            }
        }
    }

    @BClick(R.id.surface2)
    public void cs12(){
        if(nativeOriPlay2 != null){
            if(nativeOriPlay2.isPlay()){
                nativeOriPlay2.stop();
            }else {
                nativeOriPlay2.play();
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
//        Editable text = mViews.mub.getText();
//        int radius;
//        if(text != null && !TextUtils.isEmpty(text.toString())){
//            int val;
//            try {
//                val = Integer.parseInt(text.toString());
//            }catch (NumberFormatException e){
//                mViews.mub.setText(String.valueOf(50));
//                val = 50;
//            }
//            radius = val;
//        }else {
//            radius = 50;
//        }
//        new Thread(() -> {
//            OriToast.show("等待处理...", false, false);
//            int i = NativeBitmap.testBitmap(bitmap, radius);
//            OriToast.show("处理完成->" + i, true, false);
//            getWindow().getDecorView().postDelayed(() -> OriImageActivity.startThisAct(LaunchActivity.this, bitmap, true),1000);
//        }).start();
    }
}

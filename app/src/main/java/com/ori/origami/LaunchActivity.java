package com.ori.origami;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.origami.activity.OriImageActivity;
import com.origami.activity.OriImageSelect;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.RequestPermissionNext;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.annotation.BView;
import com.origami.origami.base.base_utils.ToastMsg;

import java.io.IOException;
import java.io.InputStream;

@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_launch)
public class LaunchActivity extends AnnotationActivity {

    NativeRtspPlay nativeRtspPlay;

    @BView(R.id.mub)
    EditText editText;

    @BView(R.id.surface)
    SurfaceView surfaceView;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                nativeRtspPlay = new NativeRtspPlay();
                nativeRtspPlay.setNativeWindow(holder.getSurface());
                new Thread(() -> {
                    nativeRtspPlay.setUrl("rtsp://admin:a1234567@192.168.0.112:554/h264/ch1/sub/av_stream");
                }).start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                nativeRtspPlay.release();
            }
        });
    }

    @BClick(R.id.cl)
    public void onClick(){
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
                ToastMsg.show_msg("拒绝了权限", false);
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
        Editable text = editText.getText();
        int radius;
        if(text != null && !TextUtils.isEmpty(text.toString())){
            int val;
            try {
                val = Integer.parseInt(text.toString());
            }catch (NumberFormatException e){
                editText.setText(String.valueOf(50));
                val = 50;
            }
            radius = val;
        }else {
            radius = 50;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ToastMsg.show_msg("等待处理...", false, 7000);
                int i = NativeBitmap.testBitmap(bitmap, radius);
                ToastMsg.show_msg("处理完成->" + i, true, 1000);
                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OriImageActivity.startThisAct(LaunchActivity.this, bitmap, false);
                    }
                },1000);
            }
        }).start();
    }
}

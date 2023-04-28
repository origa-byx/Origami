package com.ori.origami;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ori.origami.databinding.ActSameViewTestBinding;
import com.ori.origami.jni.OriOpenCv;
import com.origami.activity.OriImageSelect;
import com.origami.origami.base.act.OriBaseActivity;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.callback.RequestPermissionNext;
import com.origami.origami.base.toast.OriToast;
import com.origami.utils.Ori;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import vtb.mashiro.kanon.Aria;

/**
 * @by: origami
 * @date: {2022/5/26}
 * @info:
 **/
@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.act_same_view_test)
public class ActSameViewTest extends OriBaseActivity<ActSameViewTestBinding> {

//    TextView textView;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && textView != null){
//            textView.setText(Ori.getRandomString(6));
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        checkPermissionAndThen(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new RequestPermissionNext() {
            @Override
            public void next() {
            }

            @Override
            public void failed() {
                OriToast.show("拒绝了权限", false);
            }
        });
        mViews.sameLayout.setOnClickListener(v->{
            OriImageSelect.builder().setSelectNum(1)
                    .setCanPre(true)
                    .setRowShowNum(4)
                    .setRequestCode(1216)
                    .build(this);
        });
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        Aria build = Aria.androidBuilder()
//                .setLogMethod(args-> Log.e("TCP", args))
//                .addPostEvent(bean -> {
//                    Log.e("ORI", bean.getClass().getSimpleName() + gson.toJson(bean));
//                })
//                .setPort(3345).build();
//        build.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1216 && resultCode == Activity.RESULT_OK && data != null){
            String[] extra = data.getStringArrayExtra(OriImageSelect.RESULT_KEY);
            Log.e("ORI", String.format("%s -> %s", extra.length, extra[0]));
            Bitmap bitmap = BitmapFactory.decodeFile(extra[0]);
            mViews.sameLayout.setImageBitmap(bitmap);
            new Thread(()->{
                OriOpenCv oriOpenCv = new OriOpenCv(bitmap, (n, b)->{
                    runOnUiThread(()-> mViews.sameLayout.setImageBitmap(b));
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, b-> runOnUiThread(()-> mViews.sameLayout.setImageBitmap(b)));
                oriOpenCv.doCv(bitmap, 0.5);
            }).start();
        }
    }
}

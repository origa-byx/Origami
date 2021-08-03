package com.ori.origami;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.origami.log.OriLog;
import com.origami.log.OriLogBean;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.annotation.BView;


@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_test)
public class TestActivity extends AnnotationActivity {

//    String src_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_src.pdf";
//    String dest_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_dest.pdf";
    Context mContext;

    @BView(R.id.native_img)
    ImageView imageView;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        mContext = this;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        long current = System.currentTimeMillis();
        OriLog.getInstance().log_print(OriLogBean.d("任务开始", bitmap));
        int re = NativeBitmap.testBitmap(bitmap);
        OriLog.getInstance().log_print(OriLogBean.w(String.format("结果：%s -- 耗时：%s", re, System.currentTimeMillis() - current), bitmap));
        OriLog.getInstance().log_print(OriLogBean.v("任务结束"));
        imageView.setImageBitmap(bitmap);
    }

    @SuppressWarnings("unused")
    @BClick(R.id.jni_test)
    public void onClick_jni(){

    }

}
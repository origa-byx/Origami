package com.ori.origami;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.origami.activity.OriImageSelect;
import com.origami.origami.base.act.AnnotationActivity;
import com.origami.origami.base.annotation.BClick;
import com.origami.origami.base.annotation.BContentView;
import com.origami.origami.base.toast.OriToast;
import com.origami.window.NotificationUtil;
import com.origami.window.WindowUtil;


@SuppressLint("NonConstantResourceId")
@BContentView(R.layout.activity_test)
public class TestActivity extends AnnotationActivity {

//    String src_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_src.pdf";
//    String dest_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_dest.pdf";
//    Context mContext;

//    @BView(R.id.native_img)
//    ImageView imageView;

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
//        mContext = this;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
//        long current = System.currentTimeMillis();
//        OriLog.getInstance().log_print(OriLogBean.d("任务开始", bitmap));
//        int re = NativeBitmap.testBitmap(bitmap);
//        OriLog.getInstance().log_print(OriLogBean.w(String.format("结果：%s -- 耗时：%s", re, System.currentTimeMillis() - current), bitmap));
//        OriLog.getInstance().log_print(OriLogBean.v("任务结束"));
//        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == Activity.RESULT_OK && data != null){
            for (String s : data.getStringArrayExtra(OriImageSelect.RESULT_KEY)) {
                Log.e("ORI","path: " + s);
            }
        }
    }


    @SuppressWarnings("unused")
    @BClick(R.id.uii)
    public void onClick_jni(){
        WindowUtil.showMakeSure(this, "确认要启动这个活动吗？？？",
                new String[]{"确认", "取消"}, new WindowUtil.OnSelectListener() {
            @Override
            public void onSelect(String txt, int index) {
                if(index == 0){
                    OriToast.show("我就测试一下能不能成功我就测试一下能不能成功",true, false, Gravity.TOP);
                    Intent intent = new Intent(TestActivity.this, Test2Act.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                    startActivity(intent);
                }else {
                    NotificationUtil.builder(TestActivity.this, "com.origami.test", "测试")
                            .setIntent(Test2Act.class)
                            .setIconRes(R.mipmap.ic_launcher)
                            .setTitle("启动2Act")
                            .setContentText("点击此处来启动Test2Act\nclick-click\nclick")
                            .build().show();
                }
            }
        }, 0.3f);
//        OriImageSelect.builder()
//                .setCanPre(true)
//                .setSelectNum(2)
//                .setRowShowNum(3)
//                .setRequestCode(123).build(this);
    }

}
package com.origami.activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.origami.origami.R;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.OriTransfer;
import com.origami.origami.base.base_utils.ToastMsg;
import com.origami.view.OriImageDetailView;
import com.origami.window.ShowUtil;

import java.io.File;

/**
 * {@link OriImageActivity#startThisAct(Activity, Object)}
 */
public class OriImageActivity extends AnnotationActivity {

    private OriImageDetailView imageView;

    /**
     * 启动
     * @param activity
     * @param bitmap 路径 或者 bitmap
     */
    public static void startThisAct(Activity activity, final Object bitmap){
        Intent intent = new Intent(activity, OriImageActivity.class);
        OriTransfer.registerTransfer("ori_getBitmap", new OriTransfer.Transfer<Object>(OriTransfer.Simple) {
            @Override
            public Object getT() {
                return bitmap;
            }
        });
        activity.startActivity(intent);
    }

    /**
     * 启动带转场动画
     * @param activity
     * @param view      目标view ：一般为点击的imageView
     * @param bitmap    路径 或者 bitmap
     */
    public static void startThisAct(Activity activity, ImageView view, boolean canSave, final Object bitmap){
        Intent intent = new Intent(activity, OriImageActivity.class);
        OriTransfer.registerTransfer("ori_getBitmap", new OriTransfer.Transfer<Object>(OriTransfer.Simple) {
            @Override
            public Object getT() {
                return bitmap;
            }
        });
        intent.putExtra("saveFlag", canSave);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "image");
        activity.startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_ori_image;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        imageView = findViewById(R.id.ori_image);
        boolean save = getIntent().getBooleanExtra("saveFlag", true);
        Object bitmap = OriTransfer.getTransferValue("ori_getBitmap");
        if(bitmap instanceof  Bitmap){
            imageView.setImageBitmap((Bitmap) bitmap);
        }else {
            imageView.setImageBitmap(BitmapFactory.decodeFile((String) bitmap));
        }
        if(save) {
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ShowUtil.showSelect(OriImageActivity.this, new String[]{"保存"}, new ShowUtil.OnSelectListener() {
                        @Override
                        public void onSelect(String txt, int index) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                enterPictureInPictureMode();
                                return;
                            }
                            int labelRes = getApplication().getApplicationInfo().labelRes;
                            String path;
                            if (labelRes == 0) {
                                path = "ori" + File.separator + "image";
                            } else {
                                path = getResources().getString(labelRes) + File.separator + "image";
                            }
                            String savePath = imageView.saveBitmap(path, true);
                            if (savePath != null) {
                                ToastMsg.show_msg("保存成功：" + savePath, 2000);
                            } else {
                                ToastMsg toastMsg = new ToastMsg("保存失败");
                                toastMsg.showIcon = false;
                                toastMsg.showTime = 2500;
                                OriImageActivity.this.showToastMsg(toastMsg);
                            }
                        }
                    }, true);
                    return false;
                }
            });
        }

    }
}
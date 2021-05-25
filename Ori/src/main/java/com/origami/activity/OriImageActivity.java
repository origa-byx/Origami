package com.origami.activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.origami.origami.R;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.OriTransfer;
import com.origami.view.OriClipImageView;
import com.origami.window.ShowUtil;

import java.io.File;

/**
 * {@link OriImageActivity#startThisAct(Activity, Bitmap)}
 */
public class OriImageActivity extends AnnotationActivity<OriImagePresenter> {

    private OriClipImageView imageView;
    private Bitmap bitmap;

    public static void startThisAct(Activity activity, final Bitmap bitmap){
        Intent intent = new Intent(activity, OriImageActivity.class);
        OriTransfer.registerTransfer("getBitmap", new OriTransfer.Transfer<Bitmap>(OriTransfer.Simple) {
            @Override
            public Bitmap getT() {
                return bitmap;
            }
        });
        activity.startActivity(intent);
    }

    public static void startThisAct(Activity activity, ImageView view, final Bitmap bitmap){
        Intent intent = new Intent(activity, OriImageActivity.class);
        OriTransfer.registerTransfer("getBitmap", new OriTransfer.Transfer<Bitmap>(OriTransfer.Simple) {
            @Override
            public Bitmap getT() {
                return bitmap;
            }
        });
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "image");
        activity.startActivity(intent,optionsCompat.toBundle());
    }

    @Override
    public OriImagePresenter newPresenter() {
        return new OriImagePresenter(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_ori_image;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        imageView = findViewById(R.id.ori_image);
        bitmap = OriTransfer.getTransferValue("getBitmap");
        imageView.setImageBitmap(bitmap);
        imageView.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowUtil.showSelect(OriImageActivity.this, new String[]{"保存"}, new ShowUtil.OnSelectListener() {
                    @Override
                    public void onSelect(String txt, int index) {
                        int labelRes = getApplication().getApplicationInfo().labelRes;
                        String path;
                        if(labelRes == 0){
                            path = "ori" + File.separator + "image";
                        } else {
                            path =  getResources().getString(labelRes) + File.separator + "image";
                        }
                        imageView.saveBitmap(bitmap, path,true);
                    }
                },true);
                return true;
            }
        });
    }
}
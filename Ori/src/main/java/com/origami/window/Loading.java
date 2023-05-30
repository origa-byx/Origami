package com.origami.window;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorRes;
import androidx.databinding.DataBindingUtil;

import com.origami.origami.R;
import com.origami.origami.databinding.OriViewLoadingBinding;


/**
 * @by: origami
 * @date: {2022/1/27}
 * @info:
 **/
public class Loading {

    public static WindowUtil getLoading(Activity activity){
        return getLoading(activity, "waiting...");
    }


    public static WindowUtil getLoading(Activity activity, String tips){
        return getLoading(activity, R.drawable.ori_back_loading, R.color._ori_white, tips);
    }

    public static WindowUtil getLoading(Activity activity, @ColorRes int color, String tips){
        return getLoading(activity, 0, color, tips);
    }

    public static WindowUtil getLoading(Activity activity, int res, @ColorRes int color, String tips){
        WindowUtil windowUtil = WindowUtil.build(activity)
                .bindView(R.layout.ori_view_loading, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER);
        OriViewLoadingBinding bind = DataBindingUtil.bind(windowUtil.getBindView());
        if(bind == null)
            throw new IllegalStateException("cant get ViewLoadingBinding");
        if(res != 0) bind.vlLoadMain.setBackgroundResource(res);
        int colorT = activity.getResources().getColor(color);
        bind.vlLoading.setImageTintList(ColorStateList.valueOf(colorT));
        bind.vlText.setTextColor(colorT);
//        bind.vlText.setTextColor(activity.getResources().getColorStateList(color));
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_loading);
        animation.setInterpolator(new DecelerateInterpolator());
        bind.vlLoading.startAnimation(animation);
        bind.vlText.setText(tips);
        return windowUtil;
    }

}

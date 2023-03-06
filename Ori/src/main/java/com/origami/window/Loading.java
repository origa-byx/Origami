package com.origami.window;

import android.app.Activity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import androidx.databinding.DataBindingUtil;

import com.origami.origami.R;
import com.origami.origami.databinding.OriViewLoadingBinding;
import com.origami.utils.Dp2px;


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
        WindowUtil windowUtil = WindowUtil.build(activity)
                .bindView(R.layout.ori_view_loading, Dp2px.dp2px(100), Dp2px.dp2px(100))
                .setGravity(Gravity.CENTER);
        OriViewLoadingBinding bind = DataBindingUtil.bind(windowUtil.getBindView());
        if(bind == null)
            throw new IllegalStateException("cant get ViewLoadingBinding");
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_loading);
        animation.setInterpolator(new DecelerateInterpolator());
        bind.vlLoading.startAnimation(animation);
        bind.vlText.setText(tips);
        return windowUtil;
    }

}

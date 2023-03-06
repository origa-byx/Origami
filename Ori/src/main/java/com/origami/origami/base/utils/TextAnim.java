package com.origami.origami.base.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.core.util.Consumer;

/**
 * @by: origami
 * @date: {2022/3/1}
 * @info:
 **/
public class TextAnim {

    private CharSequence txt = "";
    private TextView textView;
    private final ValueAnimator animator = new ValueAnimator();

    public TextAnim(long duration) {
        this(duration, View::setAlpha);
    }

    public TextAnim(long duration, Consumer2<TextView, Float> update01) {
        animator.setFloatValues(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(anVal -> {
            if(textView != null) {
                float val = (float) anVal.getAnimatedValue();
                update01.accept(textView, val);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(textView != null)
                    textView.setText(txt);
            }
        });
    }

    public void setTxt(TextView textView, CharSequence txt){
        setTextView(textView);
        setTxt(txt);
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }

    public void setTxt(CharSequence txt){
        this.txt = txt;
        if(animator.isRunning()) animator.cancel();
        animator.start();
    }


}

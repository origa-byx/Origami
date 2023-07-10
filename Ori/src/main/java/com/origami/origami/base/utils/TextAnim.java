package com.origami.origami.base.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * @by: origami
 * @date: {2022/3/1}
 * @info:
 **/
public class TextAnim {

    private CharSequence txt = "";
    private TextView textView;
    ValueAnimator animator = new ValueAnimator();

    public TextAnim(long duration) {
        animator.setFloatValues(1f, -0.2f, 1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(duration);
        animator.addUpdateListener(anVal -> {
            if(textView != null) {
                float val = (float) anVal.getAnimatedValue();
                if(!changed && val <= 0){
                    textView.setText(txt);
                    changed = true;
                    val = 0;
                }
                textView.setAlpha(val);
//                float sc = 1 + (1 - val) * 2;
//                textView.setScaleX(sc);
//                textView.setScaleY(sc);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(textView != null)
                    textView.setText(txt);
            }

            @Override
            public void onAnimationStart(Animator animation) {

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


    private boolean changed = false;
    public void setTxt(CharSequence txt){
        this.txt = txt;
        if(animator.isRunning()) animator.cancel();
        changed = false;
        animator.start();
    }

}

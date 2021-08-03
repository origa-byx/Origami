package com.origami.window;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.origami.origami.R;
import com.origami.utils.Dp2px;
import com.origami.utils.SoftInputUtil;
import com.origami.utils.StatusUtils;

import java.lang.ref.WeakReference;

/**
 * @by: origami
 * @date: {2021-05-24}
 * @info:
 **/
public class WindowUtil {

    private final WeakReference<Activity> mActivity;
    private RelativeLayout rootLayout;
    private View bindView;

    private final int navH;
    private final WindowManager window;
    private boolean showFlag = false;
    private final WindowManager.LayoutParams paramsWindow = new WindowManager.LayoutParams();
    private RelativeLayout.LayoutParams params;

    private ValueAnimator showAnimator;

    private WindowUtil(Activity activity, int status){
        window = activity.getWindowManager();
        rootLayout = new RelativeLayout(activity){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && WindowUtil.this.isShowing()){
                    WindowUtil.this.dismiss();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };
        navH = StatusUtils.getNavigationBarHeight(activity);
        paramsWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        paramsWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
        paramsWindow.format = PixelFormat.TRANSPARENT;
        paramsWindow.flags = status |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        paramsWindow.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        paramsWindow.gravity = Gravity.CENTER;
        mActivity = new WeakReference<>(activity);
    }

    public static WindowUtil build(Activity activity){
        return new WindowUtil(activity, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    public static WindowUtil build(Activity activity, int window_layoutParams_status){
        return new WindowUtil(activity, window_layoutParams_status);
    }


    public WindowUtil bindView(int res, int width, int height, int... rule){
        Activity activity = mActivity.get();
        params = new RelativeLayout.LayoutParams(width, height);
        if(rule != null && rule.length > 0) {
            for (int i : rule) { params.addRule(i); }
        }
        if(activity == null){ return this; }
        this.bindView = LayoutInflater.from(activity).inflate(res, rootLayout, false);
        return this;
    }

    public WindowUtil bindView(View view, int width, int height, int... rule){
        params = new RelativeLayout.LayoutParams(width, height);
        if(rule != null && rule.length > 0) {
            for (int i : rule) { params.addRule(i); }
        }
        this.bindView = view;
        return this;
    }

    public WindowUtil bindDefCenterView(int res){
        Activity activity = mActivity.get();
        if(activity == null){ return this; }
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.bindView = LayoutInflater.from(activity).inflate(res, rootLayout, false);
        return this;
    }

    public WindowUtil bindDefCenterView(View view){
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.bindView = view;
        return this;
    }

    public WindowUtil setOnclickListener(View.OnClickListener listener, int... resId){
        if(bindView == null){ throw new RuntimeException("you must be bind the view at first"); }
        if(resId == null || resId.length == 0){ return this; }
        for (int id : resId) {
            bindView.findViewById(id).setOnClickListener(listener);
        }
        return this;
    }

    public View getViewById(int resId){
        if(bindView == null){ throw new RuntimeException("you must be bind the view at first"); }
        return bindView.findViewById(resId);
    }

    public WindowUtil setCanCancel(){
        bindView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //空处理，做捕获事件才能被分发到
            }
        });
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showFlag){
                    dismiss();
                }
            }
        });
        return this;
    }

    public void showByAnimator(){
        if(showFlag){ return; }
        Activity activity = mActivity.get();
        if(activity == null){ return; }
        activity.getWindow().getDecorView().setAlpha(0.6f);
        int translationY = Dp2px.dp2px(100);
        bindView.setTranslationY(translationY);
        bindView.setAlpha(0);
        params.bottomMargin = navH;
        rootLayout.addView(bindView,params);
        window.addView(rootLayout,paramsWindow);
        showFlag = true;
        if(showAnimator == null){
            showAnimator = ObjectAnimator.ofFloat(0,1f);
            showAnimator.setDuration(400);
            showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    bindView.setAlpha(value);
                    bindView.setTranslationY(translationY * (1 - value));
                }
            });
        }
        showAnimator.start();
    }

    public void show(){
        if(showFlag){ return; }
        Activity activity = mActivity.get();
        if(activity == null){ return; }
        activity.getWindow().getDecorView().setAlpha(0.6f);
        params.bottomMargin = navH;
        rootLayout.addView(bindView,params);
        window.addView(rootLayout,paramsWindow);
        showFlag = true;
    }

    public void dismiss(){
        if(!showFlag){ return; }
        if(showAnimator != null && showAnimator.isRunning()){ showAnimator.cancel(); }
        Activity activity = mActivity.get();
        if(activity == null){ return; }
        activity.getWindow().getDecorView().setAlpha(1f);
        rootLayout.removeAllViews();
        window.removeView(rootLayout);
        showFlag = false;
    }

    public View getRootView(){
        return rootLayout;
    }

    public View getBindView(){
        return bindView;
    }

    public boolean isShowing() {
        return showFlag;
    }

}

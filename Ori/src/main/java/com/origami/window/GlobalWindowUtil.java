package com.origami.window;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.origami.origami.R;
import com.origami.origami.base.AnnotationActivity;
import com.origami.origami.base.AnnotationActivityManager;
import com.origami.origami.base.App;
import com.origami.origami.base.base_utils.ToastMsg;
import com.origami.utils.UiThreadUtil;

/**
 * @by: origami
 * @date: {2021-08-30}
 * @info:
 * @deprecated 受限于权限和窗口绑定机制，此方式不能实现跨activity的 view组件 来保持view的显示
 **/
public class GlobalWindowUtil {

    private ValueAnimator sMsgAnimator;
    private View sToastView;
    private final int anTime = 1000;
    private Builder builder;
    private WindowManager windowManager;

    public GlobalWindowUtil(Builder builder) {
        this.builder = builder;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        public Builder() {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.TOP | Gravity.START;
            params.format = PixelFormat.TRANSPARENT;
            params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //26及以上必须使用TYPE_APPLICATION_OVERLAY   @deprecated TYPE_PHONE
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
        }

        public Builder setCanTouch(boolean canTouch){
            if(canTouch) {
                params.flags = params.flags |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            }else {
                params.flags = params.flags |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            }
            return this;
        }

        public Builder setGravity(int gravity){
            params.gravity = gravity;
            return this;
        }

        public Builder setLocation(int x, int y){
            params.x = x;
            params.y = y;
            return this;
        }

        public GlobalWindowUtil build(){
            return new GlobalWindowUtil(this);
        }

    }

    public void showToast(final ToastMsg msg){
        UiThreadUtil.getInstance().runOnUiThread(()->{
            showToastMsg(msg);
        });
    }

    private void showToastMsg(ToastMsg msg){
        if(TextUtils.isEmpty(msg.msg)){ return; }
        AnnotationActivity annotationActivity = AnnotationActivityManager.getActivityList().get(0);
        windowManager = (WindowManager) annotationActivity.getSystemService(Context.WINDOW_SERVICE);
        if(sToastView == null){
            ViewGroup contView = new FrameLayout(App.appContext);
            sToastView = LayoutInflater.from(App.appContext)
                    .inflate(R.layout._base_show_toast, contView,false);
            sToastView.setVisibility(View.GONE);
            contView.addView(sToastView);
            windowManager.addView(contView, builder.params);
        }
        ImageView iconView = sToastView.findViewById(R.id._base_show_toast_icon);
        if(msg.showIcon == null){
            iconView.setVisibility(View.INVISIBLE);
        }else {
            iconView.setVisibility(View.VISIBLE);
            if (msg.showIcon) {
                iconView.setImageResource(R.mipmap._toast_ok);
            } else {
                iconView.setImageResource(R.mipmap._toast_no);
            }
        }
        ((TextView) sToastView.findViewById(R.id._base_show_toast_msg)).setText(msg.msg);
//        sToastView.measure(0,0);
//        sToastView_H = sToastView.getMeasuredHeight();
        if(sMsgAnimator == null){
            sMsgAnimator = ObjectAnimator.ofFloat(0,1);
            sMsgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = Math.min((float) animation.getAnimatedValue() / anTime * (anTime + msg.showTime),1f);
                    if(value != 1f && sToastView.getTranslationY() != 0){
                        sToastView.setTranslationY(sToastView.getHeight() * (value - 1));
                    }
                }
            });
            sMsgAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sToastView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    sToastView.setVisibility(View.GONE);
                }
            });
        }
        if(sMsgAnimator.isRunning()){
//            通过队列实现--> 特殊需求基本才会用到保证每条消息都被完整显示
//            if(msg.showType == ToastMsg.DEF){//默认队列
//
//            }else {//抢占模式
//
//            }
            sMsgAnimator.cancel();
        }
        sMsgAnimator.setDuration(anTime + msg.showTime);
        sToastView.setVisibility(View.VISIBLE);
        sMsgAnimator.start();
    }

}

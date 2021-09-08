package com.origami.window;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;

import com.origami.origami.R;
import com.origami.utils.Dp2px;

import java.lang.ref.WeakReference;

/**
 * @by: origami
 * @date: {2021-08-30}
 * @info:
 **/
public class WindowUtil2 {

    private final WeakReference<Activity> mActivity;
    private View bindView;

    private final WindowManager window;
    private boolean showFlag = false;
    private final WindowManager.LayoutParams paramsWindow = new WindowManager.LayoutParams();

    private ValueAnimator showAnimator;

    private WindowUtil2(Activity activity, int status){
        window = activity.getWindowManager();
        paramsWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        paramsWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
        paramsWindow.format = PixelFormat.TRANSPARENT;
        paramsWindow.flags = status |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        paramsWindow.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        paramsWindow.gravity = Gravity.CENTER;
        paramsWindow.dimAmount = 0;
        mActivity = new WeakReference<>(activity);
    }

    public static WindowUtil2 build(Activity activity){
        return new WindowUtil2(activity, 0);
    }

    public static WindowUtil2 build(Activity activity, int window_layoutParams_flags){
        return new WindowUtil2(activity, window_layoutParams_flags);
    }

    /**
     * 绑定UI
     * @param res    布局资源
     * @param width  布局宽 例如 {@link WindowManager.LayoutParams#MATCH_PARENT}
     * @param height 布局高
     * @return
     */
    public WindowUtil2 bindView(@LayoutRes int res, int width, int height){
        Activity activity = mActivity.get();
        paramsWindow.width = width;
        paramsWindow.height = height;
        if(activity == null){ return this; }
        this.bindView = LayoutInflater.from(activity).inflate(res, null, false);
        return this;
    }

    /**
     * 看 {@link #bindView(int, int, int)}
     * @return
     */
    public WindowUtil2 bindView(View view, int width, int height){
        paramsWindow.width = width;
        paramsWindow.height = height;
        this.bindView = view;
        return this;
    }

    /**
     * 默认配置绑定布局
     * @param res
     * @return
     */
    public WindowUtil2 bindView(@LayoutRes int res){
        Activity activity = mActivity.get();
        if(activity == null){ return this; }
        this.bindView = LayoutInflater.from(activity).inflate(res, null, false);
        return this;
    }

    /**
     * 默认配置绑定view
     * @param view
     * @return
     */
    public WindowUtil2 bindView(View view){
        this.bindView = view;
        return this;
    }

    /**
     * 控制背景变暗（透明）程度
     * @param val 1.0不透明, 0.0完全透明
     * @return
     */
    public WindowUtil2 setBackDark(@FloatRange(from = 0, to = 1f) float val){
        paramsWindow.dimAmount = val;
        return this;
    }

    public WindowUtil2 setFullScreen(){
        paramsWindow.flags = paramsWindow.flags |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
        return this;
    }


    public WindowUtil2 setLocation(int gravity, int x, int y){
        paramsWindow.gravity = gravity;
        paramsWindow.x = x;
        paramsWindow.y = y;
        return this;
    }

    /**
     * 为布局内元素绑定事件
     * @param listener
     * @param resId
     * @return
     */
    public WindowUtil2 setOnclickListener(View.OnClickListener listener, int... resId){
        if(bindView == null){ throw new RuntimeException("you must be bind the view at first"); }
        if(resId == null || resId.length == 0){ return this; }
        for (int id : resId) {
            bindView.findViewById(id).setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 获取View
     * @param resId
     * @return
     */
    public View getViewById(int resId){
        if(bindView == null){ throw new RuntimeException("you must be bind the view at first"); }
        return bindView.findViewById(resId);
    }

    /**
     * 设置可以点击外部取消
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    public WindowUtil2 setCanCancel(){
        bindView.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                dismiss();
            }
            return false;
        });
        return this;
    }

    private void keyBack(){
        bindView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
            return false;
        });
        bindView.setFocusable(true);
        bindView.setFocusableInTouchMode(true);
        bindView.requestFocus();
    }

    public void showWithAnimator(){
        if(showFlag){ return; }
        int translationY = Dp2px.dp2px(100);
        bindView.setTranslationY(translationY);
        bindView.setAlpha(0);
        window.addView(bindView, paramsWindow);
        keyBack();
        showFlag = true;
        if(showAnimator == null){
            showAnimator = ValueAnimator.ofFloat(0,1f);
            showAnimator.setDuration(400);
            showAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                bindView.setAlpha(value);
                bindView.setTranslationY(translationY * (1f - value));
            });
        }
        showAnimator.start();
    }

    public void show(){
        if(showFlag){ return; }
        window.addView(bindView, paramsWindow);
        keyBack();
        showFlag = true;
    }

    public void dismiss(){
        if(!showFlag){ return; }
        if(showAnimator != null && showAnimator.isRunning()){ showAnimator.cancel(); }
        bindView.clearFocus();
        window.removeView(bindView);
        showFlag = false;
    }

    public View getBindView(){
        return bindView;
    }

    public boolean isShowing() {
        return showFlag;
    }

    //spd  static
    public interface OnSelectListener{
        /**
         * 选择事件
         * @param txt    选择的文本
         * @param index  选择的文本的数组下标
         */
        void onSelect(String txt, int index);
    }

    /**
     * 显示选项弹出框
     * @param activity  activity
     * @param texts   可选列表
     * @param selectListener  可选列表的点击事件
     * @param showCancelView  是否含有取消选项
     */
    public static void showSelect(Activity activity,
                                  String[] texts,
                                  final OnSelectListener selectListener,
                                  boolean showCancelView){
        if(activity == null || selectListener == null){ return; }
        View selectView = LayoutInflater.from(activity)
                .inflate(
                        R.layout._base_show_select,
                        activity.getWindow().getDecorView().findViewById(android.R.id.content),
                        false);
        LinearLayout showLayout = selectView.findViewById(R.id.select_list);
        WindowUtil2 windowUtil = WindowUtil2
                .build(activity)
                .bindView(selectView)
                .setLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0)
                .setBackDark(0.6f)
                .setCanCancel();
        View.OnClickListener listener = v -> {
            if(v instanceof  TextView){
                int index =(int) v.getTag();
                windowUtil.dismiss();
                selectListener.onSelect(texts[index], index);
            }
        };
        for (int i = 0; i < texts.length; i++) {
            TextView textView = new TextView(activity);
            textView.setTag(i);
            textView.setTextSize(16);
            textView.setBackground(activity.getDrawable(R.drawable._select_white_gray));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dp2px.dp2px(45));
            layoutParams.gravity = Gravity.CENTER;
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            textView.setText(texts[i]);
            textView.setOnClickListener(listener);
            showLayout.addView(textView, layoutParams);
        }
        TextView cancel_view = selectView.findViewById(R.id.select_cancel);
        if(showCancelView){
            cancel_view.setVisibility(View.VISIBLE);
            cancel_view.setOnClickListener(v -> windowUtil.dismiss());
        }else {
            cancel_view.setVisibility(View.GONE);
        }
        windowUtil.showWithAnimator();
    }


    /**
     * 显示选项确认框
     * @param activity  activity
     * @param msg       内容
     * @param text      eg->  { "确认","取消" } or { "确认" }
     * @param selectListener    点击事件
     */
    public static void showMakeSure(Activity activity,
                                    CharSequence msg, String[] text,
                                    final OnSelectListener selectListener){
        showMakeSure(activity, msg, text, selectListener, 0.3f);
    }

    /**
     * 显示选项确认框
     * @param activity  activity
     * @param msg       内容
     * @param text      eg->  { "确认","取消" } or { "确认" }
     * @param selectListener    点击事件
     * @param backDart      弹出后背景变暗度 0为不变暗
     */
    public static void showMakeSure(Activity activity,
                                    CharSequence msg, String[] text,
                                    final OnSelectListener selectListener,
                                    @FloatRange(from = 0, to = 1) float backDart){
        if(activity == null || text == null || text.length == 0 || selectListener == null){ return; }
        View makeSureView = LayoutInflater.from(activity)
                .inflate(
                        R.layout._base_show_makesure,
                        activity.getWindow().getDecorView().findViewById(android.R.id.content),
                        false);
        TextView okView = makeSureView.findViewById(R.id._base_makesure_ok);
        okView.setTag(0);
        okView.setText(text[0]);
        TextView cancelView = makeSureView.findViewById(R.id._base_makesure_cancel);
        if(text.length >= 2){
            cancelView.setVisibility(View.VISIBLE);
            cancelView.setText(text[1]);
            cancelView.setTag(1);
        }else {
            cancelView.setVisibility(View.INVISIBLE);
        }
        TextView textView = makeSureView.findViewById(R.id._base_makesure_text);
        textView.setText(msg);
        WindowUtil2 windowUtil2 = WindowUtil2.build(activity)
                .bindView(makeSureView)
                .setLocation(Gravity.CENTER, 0, 0)
                .setBackDark(backDart)
                .setCanCancel();
        View.OnClickListener listener = v -> {
            if(v instanceof TextView){
                int index =(int) v.getTag();
                windowUtil2.dismiss();
                selectListener.onSelect(text[index], index);
            }
        };
        okView.setOnClickListener(listener);
        cancelView.setOnClickListener(listener);
        windowUtil2.showWithAnimator();
    }


}

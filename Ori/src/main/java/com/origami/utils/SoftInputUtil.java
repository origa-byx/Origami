package com.origami.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author xiao gan
 * @date 2020/12/9 0009
 * @description: android:windowSoftInputMode="adjustResize|stateAlwaysHidden"
 **/
public class SoftInputUtil {

    private int softInputHeight = 0;
    private boolean softInputHeightChanged = false;

    private boolean isNavigationBarShow = false;
    private int navigationHeight = 0;

    private View currentView;
    private ISoftInputChanged listener;
    private boolean isSoftInputShowing = false;

    private final View.OnFocusChangeListener sFocusChangeListener;

    private SoftInputUtil() {
        sFocusChangeListener = (v, hasFocus) -> {
            if(hasFocus){
                currentView = v;
            }
        };
    }

    public static SoftInputUtil Build(){
        return new SoftInputUtil();
    }

    public interface ISoftInputChanged {
        /**
         * @param isSoftInputShow 键盘是否显示
         * @param softInputHeight 键盘高度
         * @param viewOffset 当前焦点view底部 - 键盘顶部 的差值
         */
        void onChanged(boolean isSoftInputShow, int softInputHeight, int viewOffset);
    }

    public SoftInputUtil attachSoftInput(View moveView){
        attachSoftInput(moveView, moveView.getRootView());
        return this;
    }

    public SoftInputUtil attachSoftInput(View moveView, View rootView) {
        attachSoftInput(rootView, (isSoftInputShow, softInputHeight, viewOffset) -> {
            if(isSoftInputShow && viewOffset > 0) {
                moveView.setTranslationY(-viewOffset);
            }else {
                moveView.setTranslationY(0);
            }
        });
        return this;
    }

    public SoftInputUtil addAttachView(final View... anyView){
        if(anyView != null && anyView.length > 0){
            for (View view : anyView) {
                view.setOnFocusChangeListener(sFocusChangeListener);
            }
        }
        return this;
    }

    public void setMyListener(ISoftInputChanged listener) {
        this.listener = listener;
    }

    /**
     * @param rootView  根view {@link View#getRootView()}
     * @param listener 监听器
     */
    private void attachSoftInput(View rootView, final ISoftInputChanged listener) {
        if (listener == null || rootView == null) return;
        this.listener = listener;
        navigationHeight = getNavigationBarHeight(rootView.getContext());
        //anyView为需要调整高度的View，理论上来说可以是任意的View
        this.currentView = null;

        rootView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if(currentView == null) return;
            //对于Activity来说，该高度即为屏幕高度
            int rootHeight = rootView.getHeight();
            Rect rect = new Rect();
            //获取当前可见部分，默认可见部分是除了状态栏和导航栏剩下的部分
            rootView.getWindowVisibleDisplayFrame(rect);
            if (rootHeight - rect.bottom == navigationHeight) {
                //如果可见部分底部与屏幕底部刚好相差导航栏的高度，则认为有导航栏
                isNavigationBarShow = true;
            } else if (rootHeight - rect.bottom == 0) {
                //如果可见部分底部与屏幕底部平齐，说明没有导航栏
                isNavigationBarShow = false;
            }
            //cal softInput height
            boolean isSoftInputShow = false;
            int softInputHeight = 0;
            //如果有导航栏，则要去除导航栏的高度
            int mutableHeight = isNavigationBarShow ? navigationHeight : 0;
            if (rootHeight - mutableHeight > rect.bottom) {
                //除去导航栏高度后，可见区域仍然小于屏幕高度，则说明键盘弹起了
                isSoftInputShow = true;
                //键盘高度
                softInputHeight = rootHeight - mutableHeight - rect.bottom;
                if (SoftInputUtil.this.softInputHeight != softInputHeight) {
                    softInputHeightChanged = true;
                    SoftInputUtil.this.softInputHeight = softInputHeight;
                } else {
                    softInputHeightChanged = false;
                }
            }

            //获取目标View的位置坐标
            int[] location = new int[2];
            currentView.getLocationOnScreen(location);

            //条件1减少不必要的回调，只关心前后发生变化的
            //条件2针对软键盘切换手写、拼音键等键盘高度发生变化
            if (isSoftInputShowing != isSoftInputShow || (isSoftInputShow && softInputHeightChanged)) {
                //第三个参数为该View需要调整的偏移量
                //此处的坐标都是相对屏幕左上角(0,0)为基准的
                this.listener.onChanged(isSoftInputShow, softInputHeight, location[1] + currentView.getHeight() - rect.bottom);
                isSoftInputShowing = isSoftInputShow;
            }
        });
    }

    public boolean isSoftInputShowing() {
        return isSoftInputShowing;
    }

    //***************STATIC METHOD******************

    public static int getNavigationBarHeight(Context context) {
        if (context == null)
            return 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void showSoftInput(View view) {
        if (view == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static void hideSoftInput(View view) {
        if (view == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

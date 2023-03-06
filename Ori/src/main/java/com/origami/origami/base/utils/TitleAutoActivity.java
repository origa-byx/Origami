package com.origami.origami.base.utils;

import android.view.View;
import android.view.ViewGroup;

import com.origami.origami.R;
import com.origami.origami.base.act.AnnotationActivity;
import com.origami.utils.Dp2px;
import com.origami.utils.StatusUtils;


/**
 * @by: origami
 * @date: {2022/1/21}
 * @info:   处理状态栏 标题 栏
 **/
public interface TitleAutoActivity {

    default BaseTitleView initStatusAndTitleBar(AnnotationActivity activity){
        Title title = activity.getClass().getAnnotation(Title.class);
        if(title == null) return null;
        StatusUtils.setLightStatusBar(activity, title.statusIsDark());
        BaseTitleView mTitleView = new BaseTitleView(activity);
        int dp50 = Dp2px.dp2px(title.heightDp());
        int statusBarHeight = StatusUtils.getStatusBarHeight(activity);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp50 + statusBarHeight);
        mTitleView.setLayoutParams(params);
        mTitleView.setPadding(0, statusBarHeight, 0, 0);
        mTitleView.mViews.vtbTitle.setText(title.value());
        mTitleView.setBackImage(true, v-> activity.finish());
        int res = title.backgroundRes() == 0 ? R.color._ori_blue : title.backgroundRes();
        mTitleView.setBackgroundResource(res);
//        StatusUtils.setStatusBarResource(activity, res);
        initTitleView(mTitleView);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.addView(mTitleView);
        decorView.findViewById(android.R.id.content)
                .setPadding(0, statusBarHeight + dp50, 0, 0);
        return mTitleView;
    }

    /**
     * 可重新添加 title 子 View 或其他操作
     * @param titleView titleView
     * @see BaseTitleView#addImage(int, View.OnClickListener)
     * @see BaseTitleView#addText(String, View.OnClickListener)
     * @see BaseTitleView#initArgs(int)
     * @see BaseTitleView#setbackColorRes(int)
     * @see BaseTitleView#setbackColor(int)
     */
    default void initTitleView(BaseTitleView titleView){
        //默认状态栏颜色（title也是这个默认背景），如果调用比如 BaseTitleView#setbackColorRes(int) 等
        //请自行更改状态栏颜色一致
    }

}

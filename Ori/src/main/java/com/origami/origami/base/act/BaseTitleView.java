package com.origami.origami.base.act;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.origami.origami.R;
import com.origami.origami.databinding.ViewTitleBarBinding;
import com.origami.utils.Dp2px;



/**
 * @by: origami
 * @date: {2022/1/21}
 * @info:
 **/
public class BaseTitleView extends FrameLayout {

    private int tintColor = Color.WHITE;

    private static final int PADDING10 = Dp2px.dp2px(10);
    Context context;
    ViewTitleBarBinding mViews;

    public BaseTitleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        mViews = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_title_bar, this, true);
        if(mViews == null)
            throw new RuntimeException("DataBindingUtil#bind return null");
        initArgs(tintColor);
    }

    public void initArgs(int tintColor){
        this.tintColor = tintColor;
        mViews.vtbBack.setImageTintList(ColorStateList.valueOf(tintColor));
        mViews.vtbTitle.setTextColor(tintColor);
    }

    public void setbackColorRes(@DrawableRes int res){
        mViews.getRoot().setBackgroundResource(res);
    }

    public void setbackColorDrawable(Drawable res){
        mViews.getRoot().setBackground(res);
    }

    public View getRootView(){
        return mViews.vtbMain;
    }

    public void setbackColor(int color){
        mViews.getRoot().setBackgroundColor(color);
    }

    public BaseTitleView setTitle(String title){
        mViews.vtbTitle.setText(title);
        return this;
    }

    public BaseTitleView setBackImage(boolean show, OnClickListener listener){
        mViews.vtbBack.setVisibility(show? VISIBLE : GONE);
        mViews.vtbBack.setOnClickListener(listener);
        return this;
    }

    public BaseTitleView addImage(@DrawableRes int res, OnClickListener listener){
        AppCompatImageView imageView = new AppCompatImageView(context);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if(params == null)
            params = new ViewGroup.LayoutParams(Dp2px.dp2px(50), ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageResource(res);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(PADDING10, PADDING10, PADDING10, PADDING10);
        imageView.setBackgroundDrawable(ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.select_tran_black, null));
        imageView.setImageTintList(ColorStateList.valueOf(tintColor));
        imageView.setOnClickListener(listener);
        mViews.vtbRightLayout.addView(imageView, 0);
        return this;
    }

    public BaseTitleView addText(String text, OnClickListener listener){
        TextView textView = new TextView(context);
        textView.setText(text);
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        if(params == null)
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(params);
        textView.setPadding(PADDING10, 0, PADDING10, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setTextColor(tintColor);
        textView.setOnClickListener(listener);
        mViews.vtbRightLayout.addView(textView, 0);
        return this;
    }

}

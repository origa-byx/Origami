package com.origami.origami.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @by: origami
 * @date: {2021-10-26}
 * @info:
 **/
public abstract class AnnActivity<V extends ViewDataBinding> extends AnnotationActivity {

    protected V bViews;

    @Override
    protected void initContentView(int resId) {
        bViews = DataBindingUtil.setContentView(this, resId);
    }

}

package com.origami.origami.base;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @by: origami
 * @date: {2021-10-26}
 * @info:
 **/
public abstract class AnnFragment<T extends AnnotationActivity, V extends ViewDataBinding> extends AnnotationFragment<T> {

    protected V bViews;

    @Override
    protected void initBindView(View view) {
        bViews = DataBindingUtil.bind(view);
    }

}

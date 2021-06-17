package com.origami.origami.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.origami.origami.base.base_utils.BasePresenter;

/**
 * @by: origami
 * @date: {2021-05-28}
 * @info:
 **/
public abstract class AnnotationMvpActivity<T extends BasePresenter<? extends AnnotationActivity>> extends AnnotationActivity {

    protected T mPresenter;

    public abstract T newPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = newPresenter();
    }

}

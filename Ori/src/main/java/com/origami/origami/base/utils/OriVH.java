package com.origami.origami.base.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * @by: origami
 * @date: {2021/12/22}
 * @info:
 **/
public class OriVH<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public @NonNull final T mViews;
    public final Object obj;

    public OriVH(@NonNull View itemView) {
        super(itemView);
        this.obj = null;
        mViews = Objects.requireNonNull(DataBindingUtil.bind(itemView));
    }

    public OriVH(@NonNull View itemView, Object obj) {
        super(itemView);
        this.obj = obj;
        mViews = Objects.requireNonNull(DataBindingUtil.bind(itemView));
    }

}

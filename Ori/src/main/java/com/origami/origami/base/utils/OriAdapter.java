package com.origami.origami.base.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.origami.origami.base.annotation.BContentView;

/**
 * @by: origami
 * @date: {2022/1/5}
 * @info:
 **/
public abstract class OriAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<OriVH<T>> {

    @NonNull
    @Override
    public OriVH<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BContentView contentView = this.getClass().getAnnotation(BContentView.class);
        if(contentView != null) {
            OriVH<T> tOriVH = new OriVH<>(LayoutInflater.from(parent.getContext())
                    .inflate(contentView.value(), parent, false), saveObjToVh());
            bindVHClick(tOriVH);
            return tOriVH;
        }
        throw new IllegalArgumentException("BContentView is missed");
    }

    public void bindVHClick(OriVH<T> vh){ }

    public Object saveObjToVh(){ return null; }

    @Override
    public abstract int getItemCount();

}

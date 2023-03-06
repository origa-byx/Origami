package com.origami.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @by: origami
 * @date: {2023/2/20}
 * @info:
 **/
public class OriMoveView extends FrameLayout {
    public OriMoveView(@NonNull Context context) {
        this(context, null, 0);
    }

    public OriMoveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OriMoveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}

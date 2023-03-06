package com.origami.origami.base.utils;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.origami.origami.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @by: origami
 * @date: {2022/1/21}
 * @info:
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    String value() default "";
    @DrawableRes int backgroundRes() default 0;
    boolean statusIsDark() default false;
    int heightDp() default 50;
}

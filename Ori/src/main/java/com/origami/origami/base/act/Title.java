package com.origami.origami.base.act;

import androidx.annotation.DrawableRes;

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
    boolean dark() default false; //默认白色状态栏字体
    int h() default 50;
}

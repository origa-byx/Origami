package com.origami.origami.base.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @by: origami
 * @date: {2021/12/17}
 * @info:
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindEvent {

    int MAIN_T = 0;
    int CURRENT_T = 1;
    int NEW_T = 2;

    int runAt() default MAIN_T;
}

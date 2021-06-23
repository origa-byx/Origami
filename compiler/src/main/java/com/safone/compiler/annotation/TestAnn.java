package com.safone.compiler.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @by: origami
 * @date: {2021-06-22}
 * @info:
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface TestAnn {
    String value() default "";
}

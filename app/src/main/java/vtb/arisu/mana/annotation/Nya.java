package vtb.arisu.mana.annotation;

import vtb.arisu.mana.bean.OFLType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @by: origami
 * @date: {2022/5/23}
 * @info: 有且仅有 int long String 参数时可用，数组集齐其他特殊情况需重写
 * vtb.mashiro.kanon.base.Bean#fromBytes(byte[]) 和 vtb.mashiro.kanon.base.Bean#toBytes() 自己处理
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Nya {
    int f() default 0;
    int l() default 4;

    /**
     * 注意 浮点型 有符号和无符号的解析对于bit位数来是不会导致错误的
     */
    boolean signed() default false;
}

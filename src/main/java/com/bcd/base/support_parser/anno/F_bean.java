package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于
 * 实体类字段
 * 接口类字段、必须定义{@link #implClassExpr()}属性、且子类配合使用{@link C_impl}标注
 * <p>
 * 反解析中
 * 值不能为null
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bean {

    /**
     * 当字段类型为接口类型时候、此属性才会生效
     * 用于指定实现类
     */
    String implClassExpr() default "";
}

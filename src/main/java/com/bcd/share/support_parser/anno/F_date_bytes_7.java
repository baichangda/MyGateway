package com.bcd.share.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用于如下类型
 * {@link java.util.Date}
 * int 此时代表时间戳秒
 * long 此时代表时间戳毫秒
 * {@link String} 此时使用{@link #stringFormat()}、{@link #stringZoneId()}格式化
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date_bytes_7 {

    /**
     * 用于表示原始值的时区
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String zoneId() default "+8";

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;

    /**
     * 当字段为string类型时候
     * 需要指定翻译成string的时区、格式
     * <p>
     * 时区
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String stringFormat() default "yyyyMMddHHmmss";

    String stringZoneId() default "+8";

}

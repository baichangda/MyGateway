package com.bcd.base.support_parser.anno;

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
 *
 *
 * 解析过程、分为两个步骤
 * 1、首先所有的输入数据源都会转换为时间戳毫秒(long类型)
 * 2、然后将时间戳转换为字段值
 *
 * 以下是步骤1不同{@link #mode()}的过程
 * {@link DateMode#bytes_yyMMddHHmmss} 协议定义6字节、分别代表 年月日时分秒
 * 1、首先读取源数据获取 年月日时分秒、对 年加上{@link #baseYear()}
 * 2、根据{@link #zoneId()}转换为{@link java.time.ZonedDateTime}
 * 3、{@link java.time.ZonedDateTime}转换为时间戳毫秒
 *
 * {@link DateMode#bytes_yyyyMMddHHmmss} 协议定义7字节、分别代表 年月日时分秒、年占用2字节
 * 1、首先读取源数据获取 年月日时分秒、读取年时候会使用{@link #order()}
 * 2、根据{@link #zoneId()}转换为{@link java.time.ZonedDateTime}
 * 3、{@link java.time.ZonedDateTime}转换为时间戳毫秒
 *
 * {@link DateMode#uint64_millisecond} 协议定义uint64、代表时间戳毫秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳毫秒(long类型)
 *
 * {@link DateMode#uint64_second} 协议定义uint64、代表时间戳秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(long类型)
 * 2、时间戳秒*1000得到时间戳毫秒
 *
 * {@link DateMode#uint32_second} 协议定义uint32、代表时间戳秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(long类型)
 * 2、时间戳秒*1000得到时间戳毫秒
 *
 * {@link DateMode#float64_millisecond} 协议定义float64、代表时间戳毫秒
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳毫秒(double类型)、转换数据类型为long类型
 *
 * {@link DateMode#float64_second} 协议定义float64、代表秒、精度为0.001
 * 1、读取源数据(会使用{@link #order()})、直接读出来为时间戳秒(double类型)
 * 2、时间戳秒*1000得到时间戳毫秒(double类型)、转换数据类型为long类型
 *
 * 步骤2过程
 * 根据步骤1得到的时间戳毫秒、针对不同数据类型进行转换
 * {@link java.util.Date} new Date(long)
 * long 无需转换
 * int (int)(long/1000)
 * {@link String} 先转换为{@link java.time.ZonedDateTime}、然后使用{@link #stringFormat()}、{@link #stringZoneId()}格式化成字符串
 *
 *
 * 反解析步骤与解析过程相反
 *
 * 反解析中
 * 值不能为null
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_date {
    /**
     * {@link DateMode#bytes_yyMMddHHmmss} 协议定义6字节、分别代表 年月日时分秒
     * {@link DateMode#bytes_yyyyMMddHHmmss} 协议定义7字节、分别代表 年月日时分秒、年占用2字节
     * {@link DateMode#uint64_millisecond} 协议定义uint64、代表时间戳毫秒
     * {@link DateMode#uint64_second} 协议定义uint64、代表时间戳秒
     * {@link DateMode#uint32_second} 协议定义uint32、代表时间戳秒
     * {@link DateMode#float64_millisecond} 协议定义float64、代表时间戳毫秒
     * {@link DateMode#float64_second} 协议定义float64、代表秒、精度为0.001、小数位代表毫秒
     */
    DateMode mode();

    /**
     * 如下模式时候
     * {@link DateMode#bytes_yyMMddHHmmss}
     * {@link DateMode#bytes_yyyyMMddHHmmss}
     * 用于表示原始值的时区
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String zoneId() default "+8";

    /**
     * 如下模式时候
     * {@link DateMode#bytes_yyMMddHHmmss}
     * 年份偏移量、结果年份=baseYear+原始值
     */
    int baseYear() default 2000;

    /**
     * 字节序模式
     * 如下模式时候才有用
     * {@link DateMode#bytes_yyyyMMddHHmmss} 此时只针对年才有大小端问题
     * {@link DateMode#uint64_millisecond}
     * {@link DateMode#uint64_second}
     * {@link DateMode#uint32_second}
     * {@link DateMode#float64_millisecond}
     * {@link DateMode#float64_second}
     */
    ByteOrder order() default ByteOrder.Default;

    /**
     * 当字段为string类型时候
     * 需要指定翻译成string的时区、格式
     *
     * 时区
     * 可以为时区偏移量、或者时区id、例如中国时区
     * 时区偏移量为 +8
     * 时区id为 Asia/Shanghai
     * 区别是时区id是考虑了夏令时的、优先使用时区偏移量、效率较高
     */
    String stringFormat() default "yyyyMMddHHmmss";
    String stringZoneId() default "+8";

}

package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_bit_skip {
    /**
     * 占用bit位
     *
     * 解析原理如下:
     * 有如下3字段、分别
     * field1、bit=1
     * field2、bit=8
     * field3、bit=6
     * 总共占bit位(1+8+6=15)、读取2字节即可、分别为 bit0-bit15、其中bit0-bit7属于第一字节、bit8-bit15属于第二字节
     * field1、取值bit0、第一字节
     * field2、取值bit1-bit8、跨第一第二字节
     * field3、取之bit9-bit14、第二字节
     * 可以看到此时bit15并未使用
     * 如果下一个字段field4、为非bit字段、则会忽略bit15、因为只有bit字段才会在意遗留的bit
     * 或者
     * field3{@link #end()}=true、此时即使下一个field4、bit=1、field4也不会使用遗留的bit15、而是取第三字节的bit0使用
     *
     * 注意:为了保证解析日志的清楚、针对bit的解析会尽量保证其分开、除非多个字段的bit解析有共用的bit字节、此时无法分开
     * 例如
     * field1、bit=8
     * field2、bit=16
     * 由于、每一个field都占满了字节(即8倍数)、此时、field1和field2不存在共用字节、此时在生成的字节码逻辑中
     * field1单独使用1个字节
     * field2单独使用2个字节
     *
     * 如果
     * field1、bit=9
     * field2、bit=15
     * 此时存在共用字节、无法分割、此时字节码逻辑
     * field1、field2会共用3个字节
     *
     */
    int len();


    /**
     * 表示当前字段bit解析结束时候、剩余多余的bit(不满1字节的)将被忽略
     * 用于连续字段的bit解析、但是下一个字段的bit不接着之前的字段bit解析
     */
    boolean end() default false;
}

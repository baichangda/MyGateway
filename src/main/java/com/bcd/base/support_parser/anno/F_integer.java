package com.bcd.base.support_parser.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 适用如下字段类型
 * byte、short、int、long、float、double、枚举类
 * <p>
 * 枚举类
 * 仅支持当{@link #len()}为1、2、4时候、因为默认类型为int、8会产生精度丢失
 * 要求枚举类必有如下静态方法、例如
 * public enum Example{
 * public static Example fromInteger(int i){}
 * public int toInteger(){}
 * }
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface F_integer {
    /**
     * 占用字节数
     * 和{@link #bit()}互斥
     * 不同的类型对应不同的长度
     * byte: 1
     * short: 1、2
     * int: 2、4
     * long: 4、8
     */
    int len() default 0;

    /**
     * 占用bit位
     * 和{@link #len()}互斥
     * 1-64
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
     * field3{@link #bitEnd()}=true、此时即使下一个field4、bit=1、field4也不会使用遗留的bit15、而是取第三字节的bit0使用
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
     * 注意:当此属性生效时候、{@link #order()}无效
     *
     * 注意:如果可以使用{@link #len()}表示、尽量不要使用bit、因为会导致解析变复杂、同时日志难以阅读
     */
    int bit() default 0;

    /**
     * bit位表示的值是否为无符号类型
     * 当是有符号类型时候
     * bit最高位为符号位、0代表正数、1代表负数
     * 对值的求解方式为
     * 正数、正常进行求值
     * 负数、所有bit位取反+1、求值后、代表负数
     */
    boolean bitUnsigned() default true;

    /**
     * {@link #bit()}时候生效
     * 表示当前字段bit解析结束时候、剩余多余的bit(不满1字节的)将被忽略
     * 用于连续字段的bit解析、但是下一个字段的bit不接着之前的字段bit解析
     */
    boolean bitEnd() default false;

    /**
     * 值处理表达式
     * 在解析出的原始值得基础上,进行运算
     * 公式中的x变量代表字段原始的值
     * 注意:
     * 表达式需要符合java运算表达式规则
     * 例如:
     * x-10
     * x*10
     * (x+10)*100
     * (x+100)/100
     */
    String valExpr() default "";

    /**
     * 变量名称
     * 标注此标记的会在解析时候将值缓存,供表达式使用
     * 例如: m,n,a
     */
    char var() default '0';


    /**
     * 有符号或者无符号
     */
    boolean unsigned() default true;

    /**
     * 字节序模式
     */
    ByteOrder order() default ByteOrder.Default;
}

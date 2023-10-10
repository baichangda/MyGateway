package com.bcd.share.support_parser.anno;

/**
 * 说明参考{@link F_skip#mode()}注释
 */
public enum SkipMode {
    /**
     * 直接跳过当前指定长度的字节数
     */
    skip,
    /**
     * 跳过 从当前对象开始预留字节-已使用字节
     */
    reservedFromStart,
}

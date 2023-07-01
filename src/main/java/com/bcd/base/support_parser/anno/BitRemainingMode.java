package com.bcd.base.support_parser.anno;

public enum BitRemainingMode {
    /**
     * 默认情况下、会有如下场景
     * 1、为类的最后一个字段、此时视为{@link #ignore}
     * 2、下一个字段为bit类型字段、此时视为{@link #not_ignore}
     */
    Default,
    /**
     * 忽略不满1字节的的bit
     */
    ignore,
    /**
     * 不忽略不满1字节的的bit
     */
    not_ignore
}

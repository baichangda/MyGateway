package com.bcd.base.exception;

import com.bcd.base.util.StringUtil;

/**
 * 建造此异常类的目的:
 * 1、在所有需要抛非运行时异常的地方,用此异常包装,避免方法调用时候需要捕获异常(若是其他框架自定义的异常,请不要用此类包装)
 * 2、在业务需要出异常的时候,定义异常并且抛出
 */
public class BusinessException extends RuntimeException {
    public int code = 1;

    private BusinessException(String message) {
        super(message);
    }

    private BusinessException(Throwable e) {
        super(e);
    }

    public static BusinessException get(String message) {
        return new BusinessException(message);
    }

    /**
     * 将异常信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     *
     * @param message
     * @param params
     * @return
     */
    public static BusinessException get(String message, Object... params) {
        return new BusinessException(StringUtil.format(message, params));
    }

    public static BusinessException get(Throwable e) {
        return new BusinessException(e);
    }

    public static void main(String[] args) {
        throw BusinessException.get("[{}]-[{}]", null, 100000);
    }

    public BusinessException code(int code) {
        this.code = code;
        return this;
    }
}

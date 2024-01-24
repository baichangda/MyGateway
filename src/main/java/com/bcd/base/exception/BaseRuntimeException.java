package com.bcd.base.exception;

import com.bcd.base.util.StringUtil;

/**
 * 建造此异常类的目的:
 * 1、在所有需要抛非运行时异常的地方,用此异常包装,避免方法调用时候需要捕获异常(若是其他框架自定义的异常,请不要用此类包装)
 * 2、在业务需要出异常的时候,定义异常并且抛出
 */
public class BaseRuntimeException extends RuntimeException {
    public int code = 1;

    private BaseRuntimeException(String message) {
        super(message);
    }

    private BaseRuntimeException(Throwable e) {
        super(e);
    }

    public static BaseRuntimeException getException(String message) {
        return new BaseRuntimeException(message);
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
    public static BaseRuntimeException getException(String message, Object... params) {
        return new BaseRuntimeException(StringUtil.format(message, params));
    }

    public static BaseRuntimeException getException(Throwable e) {
        return new BaseRuntimeException(e);
    }

    public static BaseRuntimeException getException(Throwable e, int code) {
        return new BaseRuntimeException(e).code(code);
    }

    public static void main(String[] args) {
        throw BaseRuntimeException.getException("[{}]-[{}]", null, 100000);
    }

    public BaseRuntimeException code(int code) {
        this.code = code;
        return this;
    }
}
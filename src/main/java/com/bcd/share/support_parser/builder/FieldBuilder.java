package com.bcd.share.support_parser.builder;


import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.SkipMode;
import io.netty.buffer.ByteBuf;

import java.lang.annotation.Annotation;

public abstract class FieldBuilder {
    public final static String varNameThis = "$0";
    public final static String varNameByteBuf = "$1";
    public final static String varNameParentProcessContext = "$2";

    public final static String varNameInstance = "_instance";
    /**
     * 如果bean存在字段注解{@link F_skip#mode()} 为 {@link SkipMode#reservedFromStart} 时候、会有如下操作
     * 解析bean开始时候、记录{@link ByteBuf#readerIndex()}
     * 反解析bean开始时候、记录{@link ByteBuf#writerIndex()}
     */
    public final static String varNameStartIndex = "_start_index";
    public final static String varNameBitBuf = "_bitBuf";

    public abstract void buildParse(final BuilderContext context);


    public void buildDeParse(final BuilderContext context) {

    }

    public abstract Class<? extends Annotation> annoClass();

}

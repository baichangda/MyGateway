package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.JavassistUtil;
import io.netty.buffer.ByteBuf;
import javassist.CtClass;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuilderContext {
    /**
     * 主要用于{@link F_customize#builderClass()}缓存、避免在生成解析类的过程中生成多个实例
     */
    public final static Map<Class, FieldBuilder> fieldBuilderCache = new HashMap<>();
    /**
     * parse方法体
     */
    public final StringBuilder body;
    /**
     * 类
     */
    public final Class clazz;
    /**
     * 生产的{@link Processor}子类
     */
    public final CtClass implCc;
    /**
     * 当前字段
     */
    public Field field;

    public String varNameBitBuf;

    public boolean logBit;
    public String varNameBitLog;
    public boolean bitEndWhenBitField_process;
    public boolean bitEndWhenBitField_deProcess;

    /**
     * 用于给
     * {@link Processor#process(ByteBuf, ProcessContext)}
     * {@link Processor#deProcess(ByteBuf, ProcessContext, Object)}
     * 的参数对象、对象复用、避免构造多个
     */
    public String processContextVarName;

    /**
     * 当前字段所属class中的变量名称对应字段名称
     */
    public final Map<Character, String> varToFieldName = new HashMap<>();

    /**
     * 类全局变量定义内容对应变量名称
     * 避免重复定义类变量
     */
    public final Map<String, String> classVarDefineToVarName;

    /**
     * 用于{@link F_skip#mode()} 为 {@link SkipMode#ReservedFromPrevReserved} 时候
     * <p>
     * 此变量名称代表相同bean内上一个字段
     * {@link F_skip#mode()}为
     * {@link SkipMode#ReservedFromStart} 或
     * {@link SkipMode#ReservedFromPrevReserved}
     * 时候索引的位置变量名称
     * 如果没有上一个这样的字段、则取值是{@link FieldBuilder#varNameStartIndex}
     */
    public String prevSkipReservedIndexVarName = FieldBuilder.varNameStartIndex;
    public final Set<String> indexFieldNameSet = new HashSet<>();

    private void initIndexFieldNameSet() {
        String prevSkipReservedFieldName = null;
        for (Field declaredField : clazz.getDeclaredFields()) {
            final F_skip f_skip = declaredField.getAnnotation(F_skip.class);
            if (f_skip != null) {
                switch (f_skip.mode()) {
                    case ReservedFromStart -> {
                        prevSkipReservedFieldName = declaredField.getName();
                    }
                    case ReservedFromPrevReserved -> {
                        if (prevSkipReservedFieldName != null) {
                            indexFieldNameSet.add(prevSkipReservedFieldName);
                        }
                        prevSkipReservedFieldName = declaredField.getName();
                    }
                }
            }
        }
    }

    public BuilderContext(StringBuilder body, Class clazz, CtClass implCc, Map<String, String> classVarDefineToVarName) {
        this.body = body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.classVarDefineToVarName = classVarDefineToVarName;
        initIndexFieldNameSet();
    }

    public final String getProcessContextVarName() {
        if (processContextVarName == null) {
            processContextVarName = "processContext";
            final String proocessContextClassName = ProcessContext.class.getName();
            JavassistUtil.append(body, "final {} {}=new {}({},{});\n",
                    proocessContextClassName,
                    processContextVarName,
                    proocessContextClassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameParentProcessContext);
        }
        return processContextVarName;
    }

    public final String getVarNameBitBuf(Class bitBufClass) {
        if (varNameBitBuf == null) {
            final String bitBufClassName = bitBufClass.getName();
            final String simpleName = bitBufClass.getSimpleName();
            final String processContextBitBufVarName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
            JavassistUtil.append(body, "final {} {};\n", bitBufClassName, FieldBuilder.varNameBitBuf);
            JavassistUtil.append(body, "if({}.{}==null){\n", FieldBuilder.varNameParentProcessContext, processContextBitBufVarName);
            JavassistUtil.append(body, "{}={}.newBitBuf({});\n", FieldBuilder.varNameBitBuf, bitBufClassName, FieldBuilder.varNameByteBuf);
            JavassistUtil.append(body, "}else{\n");
            JavassistUtil.append(body, "{}={}.{};\n", FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, processContextBitBufVarName);
            JavassistUtil.append(body, "}\n");
            varNameBitBuf = FieldBuilder.varNameBitBuf;
        }
        return varNameBitBuf;
    }
}

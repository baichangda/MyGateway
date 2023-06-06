package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.support_parser.util.*;
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
    public final Class<?> clazz;
    /**
     * 生产的{@link Processor}子类
     */
    public final CtClass implCc;
    /**
     * 当前字段
     */
    public Field field;

    public String varNameBitBuf;
    public boolean bitEndWhenBitField_process;
    public boolean bitEndWhenBitField_deProcess;

    public boolean logBit;

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

    public final Set<String> indexFieldNameSet = new HashSet<>();


    public BuilderContext(StringBuilder body, Class clazz, CtClass implCc, Map<String, String> classVarDefineToVarName) {
        this.body = body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.classVarDefineToVarName = classVarDefineToVarName;
    }

    public final String getProcessContextVarName() {
        if (processContextVarName == null) {
            processContextVarName = "processContext";
            final String proocessContextClassName = ProcessContext.class.getName();
            ParseUtil.append(body, "final {} {}=new {}({},{});\n",
                    proocessContextClassName,
                    processContextVarName,
                    proocessContextClassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameParentProcessContext);
        }
        return processContextVarName;
    }

    public final String getVarNameBitBuf_reader() {
        return varNameBitBuf;
    }

    public final String getVarNameBitBuf_writer() {
        return varNameBitBuf;
    }
}

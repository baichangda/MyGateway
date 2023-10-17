package com.bcd.share.support_parser.builder;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.anno.BitOrder;
import com.bcd.share.support_parser.anno.ByteOrder;
import com.bcd.share.support_parser.anno.F_customize;
import com.bcd.share.support_parser.processor.ProcessContext;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BuilderContext {
    /**
     * 主要用于{@link F_customize#builderClass()}缓存、避免在生成解析类的过程中生成多个实例
     */
    public final static Map<Class<?>, FieldBuilder> fieldBuilderCache = new HashMap<>();
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
     * 同一个类中共享processor变量
     */
    public final Map<String, String> beanClassAndOrder_processorVarName;
    /**
     * 当前字段所属class中的变量名称对应字段名称
     */
    public final Map<Character, String> varToFieldName = new HashMap<>();
    /**
     * 类全局变量定义内容对应变量名称
     * 避免重复定义类变量
     */
    public final Map<String, String> classVarDefineToVarName;
    public final ByteOrder byteOrder;
    public final BitOrder bitOrder;
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
     * 用于获取{@link F_customize#processorClass()}获取变量
     */
    public Map<String, String> customize_processorId_processorVarName;

    public BuilderContext(StringBuilder body, Class<?> clazz, CtClass implCc, Map<String, String> classVarDefineToVarName, Map<String, String> beanClassAndOrder_processorVarName, ByteOrder byteOrder, BitOrder bitOrder, Map<String, String> customize_processorId_processorVarName) {
        this.body = body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.classVarDefineToVarName = classVarDefineToVarName;
        this.beanClassAndOrder_processorVarName = beanClassAndOrder_processorVarName;
        this.byteOrder = byteOrder;
        this.bitOrder = bitOrder;
        this.customize_processorId_processorVarName=customize_processorId_processorVarName;
    }

    public final String getCustomizeProcessorVarName(Class<?> processorClass, String processorArgs) {
        return customize_processorId_processorVarName.get(processorClass.getName() + "," + processorArgs);
    }


    public final String getProcessContextVarName() {
        if (processContextVarName == null) {
            processContextVarName = "processContext";
            final String processContextClassName = ProcessContext.class.getName();
            ParseUtil.append(body, "final {} {}=new {}({},{});\n",
                    processContextClassName,
                    processContextVarName,
                    processContextClassName,
                    FieldBuilder.varNameInstance,
                    FieldBuilder.varNameParentProcessContext
            );
        }
        return processContextVarName;
    }


    public final String getProcessorVarName(Class<?> beanClazz) {
        final String key = ParseUtil.getProcessKey(beanClazz, byteOrder, bitOrder);
        return beanClassAndOrder_processorVarName.computeIfAbsent(key, k -> {
            final String varName = "_processor_" + beanClazz.getSimpleName();
            //在build时候预先生成、并在相应的class中生成类变量
            Parser.getProcessor(beanClazz, byteOrder, bitOrder);
            String format = ParseUtil.format("public final {} {}={}.beanClassNameAndOrder_processor.get(\"{}\");\n",
                    Processor.class.getName(),
                    varName,
                    Parser.class.getName(),
                    k);
            try {
                implCc.addField(CtField.make(format, implCc));
            } catch (CannotCompileException e) {
                throw BaseRuntimeException.getException(e);
            }
            return varName;
        });
    }

    public final String getVarNameBitBuf_reader() {
        return varNameBitBuf;
    }

    public final String getVarNameBitBuf_writer() {
        return varNameBitBuf;
    }
}

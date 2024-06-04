package com.bcd.base.support_parser.builder;

import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.BitOrder;
import com.bcd.base.support_parser.anno.ByteOrder;
import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.support_parser.util.*;
import io.netty.buffer.ByteBuf;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderContext {
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

    /**
     * 字段集合
     */
    public final List<Field> fieldList;
    /**
     * 当前解析字段索引
     */
    public int fieldIndex;


    /**
     * 上下文缓存、用于不同的注解解析时候、多个字段需要共享某些信息、可以缓存在这里
     */
    public final Map<String, Object> cache = new HashMap<>();

    /**
     * 变量序号
     * 为了保证在一个解析方法中、定义的临时变量不重复
     */
    public int varIndex = 0;

    public BuilderContext(StringBuilder body, Class<?> clazz,
                          CtClass implCc, Map<String, String> classVarDefineToVarName,
                          Map<String, String> beanClassAndOrder_processorVarName, ByteOrder byteOrder, BitOrder bitOrder,
                          Map<String, String> customize_processorId_processorVarName,
                          List<Field> fieldList) {
        this.body = body;
        this.clazz = clazz;
        this.implCc = implCc;
        this.classVarDefineToVarName = classVarDefineToVarName;
        this.beanClassAndOrder_processorVarName = beanClassAndOrder_processorVarName;
        this.byteOrder = byteOrder;
        this.bitOrder = bitOrder;
        this.customize_processorId_processorVarName = customize_processorId_processorVarName;
        this.fieldList = fieldList;
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
        final String key = ParseUtil.getProcessorKey(beanClazz, byteOrder, bitOrder);
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
                throw MyException.get(e);
            }
            return varName;
        });
    }

    public final String getBitBuf_parse() {
        if (!cache.containsKey("hasBitBuf")) {
            final String bitBuf_reader_className = Parser.logCollector_parse == null ? BitBuf_reader.class.getName() : BitBuf_reader_log.class.getName();
            final String funcName = Parser.logCollector_parse == null ? "getBitBuf_reader" : "getBitBuf_reader_log";
            ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
            cache.put("hasBitBuf", true);
        }
        return FieldBuilder.varNameBitBuf;
    }

    public final String getBitBuf_deParse() {
        if (!cache.containsKey("hasBitBuf")) {
            final String bitBuf_writer_className = Parser.logCollector_parse == null ? BitBuf_writer.class.getName() : BitBuf_writer_log.class.getName();
            final String funcName = Parser.logCollector_parse == null ? "getBitBuf_writer" : "getBitBuf_writer_log";
            ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
            cache.put("hasBitBuf", true);
        }
        return FieldBuilder.varNameBitBuf;
    }
}

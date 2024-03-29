package com.bcd.base.support_parser;

import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.processor.ProcessContext;
import com.bcd.base.support_parser.processor.Processor;
import com.bcd.base.support_parser.util.BitBuf_reader_log;
import com.bcd.base.support_parser.util.BitBuf_writer_log;
import com.bcd.base.support_parser.util.LogUtil;
import com.bcd.base.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import javassist.*;
import javassist.bytecode.SignatureAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 解析器
 * 配合注解完成解析工作
 * 会扫描当前类和其父类的所有字段
 * 会忽视如下字段
 * 1、没有被{@link Parser#anno_fieldBuilder}中注解标注的字段
 * 2、static或者final修饰的字段
 * 3、非public字段
 * 解析字段的顺序为 父类字段在子类之前
 *
 * <p>
 * 工作原理:
 * 使用javassist框架配合自定义注解、生成一套解析代码
 * 使用方法:
 * 1、首先获取类处理器
 * {@link #getProcessor(Class)}
 * {@link #getProcessor(Class, ByteOrder, BitOrder)}
 * 2、调用解析或者反解析
 * <p>
 * 解析调用入口:
 * {@link Processor#process(ByteBuf, ProcessContext)}
 * <p>
 * 反解析调用入口:
 * {@link Processor#deProcess(ByteBuf, ProcessContext, Object)}
 * <p>
 * 性能表现:
 * 由于是字节码增强技术、和手动编写代码解析效率一样
 * <p>
 * 可配置方法
 * {@link #enableGenerateClassFile()}
 * {@link #enablePrintBuildLog()}
 * {@link #withDefaultLogCollector_parse()}
 * {@link #withDefaultLogCollector_deParse()}
 * <p>
 * 注意:
 * 如果启动了解析和反解析日志、并不是所有字段都会打印、逻辑参考
 * {@link ParseUtil#needLog(BuilderContext)}
 */
public class Parser {

    public final static Logger logger = LoggerFactory.getLogger(Parser.class);

    public final static Map<Class<? extends Annotation>, FieldBuilder> anno_fieldBuilder;

    static {
        anno_fieldBuilder = ParseUtil.getAllFieldBuild();
    }

    public final static Map<String, Processor<?>> beanClassNameAndOrder_processor = new HashMap<>();
    /**
     * 解析log采集器
     * 需要注意的是、此功能用于调试、会在生成的class中加入日志代码、影响性能
     * 而且此功能开启时候避免多线程调用解析、会产生日志混淆、不易调试
     */
    public static LogCollector_parse logCollector_parse;
    public static LogCollector_deParse logCollector_deParse;
    /**
     * 是否在src/main/java下面生成class文件
     * 主要用于开发测试阶段、便于查看生成的结果
     */
    private static boolean generateClassFile = false;
    /**
     * 是否打印javassist生成class的过程日志
     */
    private static boolean printBuildLog = false;

    public static void withDefaultLogCollector_parse() {
        logCollector_parse = new LogCollector_parse() {

            @Override
            public void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content, Object val, String processorClassName) {
                logger.info("--parse field{}--[{}].[{}] [{}]->[{}]"
                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , ByteBufUtil.hexDump(content).toUpperCase()
                        , val
                );
            }

            @Override
            public void collect_field_skip(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content) {
                logger.info("--parse field{}--[{}].[{}] skip len[{}] hex[{}]"
                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , content.length
                        , ByteBufUtil.hexDump(content).toUpperCase()
                );
            }

            @Override
            public void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, BitBuf_reader_log.Log[] logs, Object val, String processorClassName) {
                for (BitBuf_reader_log.Log log : logs) {
                    logger.info("--parse field{}--[{}].[{}] val[{}] {}"
                            , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                            , clazz.getSimpleName()
                            , fieldName
                            , val
                            , log.msg());
                }
            }

            @Override
            public void collect_class(Class<?> clazz, String msg) {
                logger.info("--parse class[{}] {}", clazz.getName(), msg);
            }
        };

    }

    public static void withDefaultLogCollector_deParse() {
        logCollector_deParse = new LogCollector_deParse() {
            @Override
            public void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, byte[] content, String processorClassName) {
                logger.info("--deParse field{}--[{}].[{}] [{}]->[{}]"
                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , val
                        , ByteBufUtil.hexDump(content).toUpperCase());
            }

            @Override
            public void collect_field_skip(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content) {
                logger.info("--deParse field{}--[{}].[{}] append len[{}] [{}]"
                        , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , content.length
                        , ByteBufUtil.hexDump(content).toUpperCase());
            }

            @Override
            public void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, BitBuf_writer_log.Log[] logs, String processorClassName) {
                for (BitBuf_writer_log.Log log : logs) {
                    logger.info("--deParse field{}--[{}].[{}] val[{}] {}"
                            , LogUtil.getFieldStackTrace(fieldDeclaringClass, fieldName)
                            , clazz.getSimpleName()
                            , fieldName
                            , val
                            , log.msg());
                }
            }

            @Override
            public void collect_class(Class<?> clazz, String msg) {
                logger.info("--deParse class[{}] {}", clazz.getName(), msg);
            }
        };
    }

    public static void enablePrintBuildLog() {
        printBuildLog = true;
    }

    public static void enableGenerateClassFile() {
        generateClassFile = true;
    }

    private static void buildMethodBody_process(BuilderContext context) {
        final List<Field> fieldList = context.fieldList;
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_parse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            context.fieldIndex = i;
            boolean logBit = field.isAnnotationPresent(F_bit_num.class);
            F_skip anno = field.getAnnotation(F_skip.class);
            if (anno != null && (anno.lenBefore() != 0 || !anno.lenExprBefore().isEmpty())) {
                ParseUtil.appendSkip_parse(anno.lenBefore(), anno.lenExprBefore(), context);
            }
            if (logCollector_parse != null) {
                if (!logBit) {
                    ParseUtil.prependLogCode_parse(context);
                }
            }
            try {
                for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : anno_fieldBuilder.entrySet()) {
                    Class<? extends Annotation> annoClass = entry.getKey();
                    if (field.isAnnotationPresent(annoClass)) {
                        entry.getValue().buildParse(context);
                    }
                }
            } finally {
                if (logCollector_parse != null) {
                    if (logBit) {
                        ParseUtil.appendBitLogCode_parse(context);
                    } else {
                        ParseUtil.appendLogCode_parse(context);
                    }
                }
            }
            if (anno != null && (anno.lenAfter() != 0 || !anno.lenExprAfter().isEmpty())) {
                ParseUtil.appendSkip_parse(anno.lenAfter(), anno.lenExprAfter(), context);
            }
        }

    }

    private static void buildMethodBody_deProcess(BuilderContext context) {
        final List<Field> fieldList = context.fieldList;
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_deParse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            context.fieldIndex = i;
            boolean logBit = field.isAnnotationPresent(F_bit_num.class);
            F_skip anno = field.getAnnotation(F_skip.class);
            if (anno != null && (anno.lenBefore() != 0 || !anno.lenExprBefore().isEmpty())) {
                ParseUtil.appendSkip_deParse(anno.lenBefore(), anno.lenExprBefore(), context);
            }
            if (logCollector_deParse != null) {
                if (!logBit) {
                    ParseUtil.prependLogCode_deParse(context);
                }
            }
            try {
                for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : anno_fieldBuilder.entrySet()) {
                    Class<? extends Annotation> annoClass = entry.getKey();
                    if (field.isAnnotationPresent(annoClass)) {
                        entry.getValue().buildDeParse(context);
                    }
                }
            } finally {
                if (logCollector_deParse != null) {
                    if (logBit) {
                        ParseUtil.appendBitLogCode_deParse(context);
                    } else {
                        ParseUtil.appendLogCode_deParse(context);
                    }
                }
            }
            if (anno != null && (anno.lenAfter() != 0 || !anno.lenExprAfter().isEmpty())) {
                ParseUtil.appendSkip_deParse(anno.lenAfter(), anno.lenExprAfter(), context);
            }
        }
    }

    public static <T> Class<T> buildClass(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder) throws CannotCompileException, NotFoundException, IOException {
        final String processor_class_name = Processor.class.getName();
        final String byteBufClassName = ByteBuf.class.getName();
        final String clazzName = clazz.getName();

        String implProcessor_class_name = ParseUtil.getProcessClassName(clazz, byteOrder, bitOrder);
        final CtClass cc = ClassPool.getDefault().makeClass(implProcessor_class_name);

        //添加泛型
        SignatureAttribute.ClassSignature class_cs = new SignatureAttribute.ClassSignature(null, null, new SignatureAttribute.ClassType[]{
                new SignatureAttribute.ClassType(processor_class_name, new SignatureAttribute.TypeArgument[]{
                        new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(clazzName))
                })
        });
        cc.setGenericSignature(class_cs.encode());

        cc.setModifiers(Modifier.FINAL | Modifier.PUBLIC);

        StringBuilder initBody = new StringBuilder();
        final CtConstructor constructor = CtNewConstructor.make(new CtClass[]{}, null, cc);
        initBody.append("{\n");
        //加processorClass字段并初始化
        Map<String, String> customize_processorId_processorVarName = new HashMap<>();
        int processVarIndex = 0;
        final List<F_customize> f_customize_list = Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getAnnotation(F_customize.class)).filter(Objects::nonNull).filter(e -> e.processorClass() != void.class).collect(Collectors.toList());
        for (F_customize f_customize : f_customize_list) {
            Class<?> processorClass = f_customize.processorClass();
            String processorArgs = f_customize.processorArgs();
            final String processorClassName = processorClass.getName();
            final String processorId = processorClassName + "," + processorArgs;
            String processorVarName = customize_processorId_processorVarName.get(processorId);
            if (processorVarName == null) {
                processorVarName = "_processor_" + processVarIndex++;
                cc.addField(CtField.make("private final " + processorClassName + " " + processorVarName + ";", cc));
                initBody.append(ParseUtil.format("this.{}=new {}({});\n", processorVarName, processorClassName, processorArgs));
                customize_processorId_processorVarName.put(processorId, processorVarName);
            }
        }
        initBody.append("}\n");
        if (printBuildLog) {
            logger.info("----------clazz[{}] constructor body-------------\n{}", clazz.getName(), initBody.toString());
        }
        constructor.setBody(initBody.toString());
        cc.addConstructor(constructor);

        final Map<String, String> classVarDefineToVarName = new HashMap<>();
        final Map<String, String> beanClassAndOrder_processorVarName = new HashMap<>();

        //添加实现、定义process方法
        final CtClass interface_cc = ClassPool.getDefault().get(processor_class_name);
        cc.addInterface(interface_cc);
        final CtMethod process_cm = CtNewMethod.make(
                /**
                 * 在这里定义返回值为Object类型
                 * 因为正常的继承、asm实现方法需要额外创建一个桥接方法、针对泛型部分的参数为Object类型
                 */
                ClassPool.getDefault().get(Object.class.getName()),
                "process",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName())
                }, null, null, cc);

        cc.addMethod(process_cm);
        //process方法体
        StringBuilder processBody = new StringBuilder();
        processBody.append("\n{\n");
        ParseUtil.append(processBody, "final {} {}=new {}();\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        final List<Field> fieldList = ParseUtil.getParseFields(clazz);
        BuilderContext parseBuilderContext = new BuilderContext(processBody, clazz, cc, classVarDefineToVarName, beanClassAndOrder_processorVarName, byteOrder, bitOrder, customize_processorId_processorVarName, fieldList);

        C_skip c_skip = clazz.getAnnotation(C_skip.class);
        if (c_skip == null) {
            buildMethodBody_process(parseBuilderContext);
        }else{
            int classByteLen = ParseUtil.getClassByteLenIfPossible(clazz);
            if (classByteLen == -1) {
                ParseUtil.append(processBody, "final int {}={}.readerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
                buildMethodBody_process(parseBuilderContext);
                String lenValCode;
                if (c_skip.len() == 0) {
                    lenValCode = ParseUtil.replaceLenExprToCode(c_skip.lenExpr(), parseBuilderContext.varToFieldName, clazz);
                } else {
                    lenValCode = c_skip.len() + "";
                }
                ParseUtil.append(processBody, "final int {}={}-{}.readerIndex()+{};\n", FieldBuilder.varNameShouldSkip, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(processBody, "if({}>0){\n", FieldBuilder.varNameShouldSkip);
                ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, FieldBuilder.varNameShouldSkip);
                if (logCollector_parse != null) {
                    ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,\"@C_skip skip[\"+{}+\"]\");\n", Parser.class.getName(), clazzName, FieldBuilder.varNameShouldSkip);
                }
                ParseUtil.append(processBody, "}\n");
            } else {
                buildMethodBody_process(parseBuilderContext);
                if (c_skip.len() == 0) {
                    String lenValCode = ParseUtil.replaceLenExprToCode(c_skip.lenExpr(), parseBuilderContext.varToFieldName, clazz);
                    String skipCode = "(" + lenValCode + "-" + classByteLen + ")";
                    ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipCode);
                    if (logCollector_parse != null) {
                        ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,\"@C_skip skip[\"+{}+\"]\");\n", Parser.class.getName(), clazzName, skipCode);
                    }
                } else {
                    int skip = c_skip.len() - classByteLen;
                    if(skip>0){
                        ParseUtil.append(processBody, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skip);
                        if (logCollector_parse != null) {
                            ParseUtil.append(processBody, "{}.logCollector_parse.collect_class({}.class,\"@C_skip skip[{}]\");\n", Parser.class.getName(), clazzName, skip);
                        }
                    }
                }
            }
        }

        ParseUtil.append(processBody, "return {};\n", FieldBuilder.varNameInstance);
        processBody.append("}");
        if (printBuildLog) {
            logger.info("\n-----------class[{}] process-----------{}\n", clazz.getName(), processBody.toString());
        }
        process_cm.setBody(processBody.toString());

        //添加实现、定义deProcess方法
        final CtMethod deProcess_cm = CtNewMethod.make(
                ClassPool.getDefault().get(void.class.getName()),
                "deProcess",
                new CtClass[]{
                        ClassPool.getDefault().get(byteBufClassName),
                        ClassPool.getDefault().get(ProcessContext.class.getName()),
                        ClassPool.getDefault().get(Object.class.getName()),
                }, null, null, cc);

        cc.addMethod(deProcess_cm);
        //deProcess方法体
        StringBuilder deProcessBody = new StringBuilder();
        deProcessBody.append("\n{\n");
        ParseUtil.append(deProcessBody, "final {} {}=({})$3;\n", clazzName, FieldBuilder.varNameInstance, clazzName);
        BuilderContext deParseBuilderContext = new BuilderContext(deProcessBody, clazz, cc, classVarDefineToVarName, beanClassAndOrder_processorVarName, byteOrder, bitOrder, customize_processorId_processorVarName, fieldList);

        if (c_skip == null) {
            buildMethodBody_deProcess(deParseBuilderContext);
        }else{
            int classByteLen = ParseUtil.getClassByteLenIfPossible(clazz);
            if (classByteLen == -1) {
                ParseUtil.append(deProcessBody, "final int {}={}.writerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
                buildMethodBody_deProcess(deParseBuilderContext);
                String lenValCode;
                if (c_skip.len() == 0) {
                    lenValCode = ParseUtil.replaceLenExprToCode(c_skip.lenExpr(), parseBuilderContext.varToFieldName, clazz);
                } else {
                    lenValCode = c_skip.len() + "";
                }
                ParseUtil.append(deProcessBody, "final int {}={}-{}.writerIndex()+{};\n", FieldBuilder.varNameShouldSkip, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(deProcessBody, "if({}>0){\n", FieldBuilder.varNameShouldSkip);
                ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, FieldBuilder.varNameShouldSkip);
                if (logCollector_parse != null) {
                    ParseUtil.append(processBody, "{}.logCollector_deParse.collect_class({}.class,\"@C_skip append[\"+{}+\"]\");\n", Parser.class.getName(), clazzName, FieldBuilder.varNameShouldSkip);
                }
                ParseUtil.append(deProcessBody, "}\n");
            } else {
                buildMethodBody_deProcess(deParseBuilderContext);
                if (c_skip.len() == 0) {
                    String lenValCode = ParseUtil.replaceLenExprToCode(c_skip.lenExpr(), parseBuilderContext.varToFieldName, clazz);
                    String skipCode = "(" + lenValCode + "-" + classByteLen + ")";
                    ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, skipCode);
                    if (logCollector_parse != null) {
                        ParseUtil.append(deProcessBody, "{}.logCollector_deParse.collect_class({}.class,\"@C_skip append[\"+{}+\"]\");\n", Parser.class.getName(), clazzName, skipCode);
                    }
                } else {
                    int skip = c_skip.len() - classByteLen;
                    if(skip>0) {
                        ParseUtil.append(deProcessBody, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, skip);
                        if (logCollector_parse != null) {
                            ParseUtil.append(processBody, "{}.logCollector_deParse.collect_class({}.class,\"@C_skip append[{}]\");\n", Parser.class.getName(), clazzName, skip);
                        }
                    }
                }
            }
        }

        deProcessBody.append("}");
        if (printBuildLog) {
            logger.info("\n-----------class[{}] deProcess-----------{}\n", clazz.getName(), deProcessBody.toString());
        }
        deProcess_cm.setBody(deProcessBody.toString());

        if (generateClassFile) {
            cc.writeFile("src/main/java");
        }
        return (Class<T>) cc.toClass(Processor.class);
//        return cc.toClass();
    }

    /**
     * 获取类解析器
     * 使用默认字节序模式和位模式
     *
     * @param clazz 实体类类型
     * @param <T>
     * @return
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz) {
        return getProcessor(clazz, ByteOrder.Default, BitOrder.Default);
    }

    /**
     * 获取类解析器
     *
     * @param clazz     实体类类型
     * @param byteOrder 实体类字节码实现 字节序模式
     * @param bitOrder  实体类字节码实现 位模式
     * @param <T>
     * @return
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder) {
        final String key = ParseUtil.getProcessKey(clazz, byteOrder, bitOrder);
        Processor<T> processor = (Processor<T>) beanClassNameAndOrder_processor.get(key);
        if (processor == null) {
            synchronized (beanClassNameAndOrder_processor) {
                processor = (Processor<T>) beanClassNameAndOrder_processor.get(key);
                if (processor == null) {
                    try {
                        final Class<T> processClass = Parser.buildClass(clazz, byteOrder, bitOrder);
                        processor = (Processor<T>) processClass.getConstructor().newInstance();
                        beanClassNameAndOrder_processor.put(key, processor);
                        return processor;
                    } catch (CannotCompileException | NotFoundException | IOException | NoSuchMethodException |
                             InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw BaseRuntimeException.getException(e);
                    }
                }
            }
        }
        return processor;
    }

    public interface LogCollector_parse {
        /**
         * 收集每个字段解析的详情
         *
         * @param clazz               实体类
         * @param fieldDeclaringClass 字段所属类
         * @param fieldName           字段名称
         * @param content             解析之前字节数组
         * @param val                 解析后的值
         * @param processorClassName  解析器类名
         */
        void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content, Object val, String processorClassName);

        void collect_field_skip(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content);

        /**
         * 收集每个字段解析的详情
         *
         * @param clazz               实体类
         * @param fieldDeclaringClass 字段所属类
         * @param fieldName           字段名称
         * @param logs                bit解析日志
         * @param val                 解析后的值
         * @param processorClassName  解析器类名
         */
        void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, BitBuf_reader_log.Log[] logs, Object val, String processorClassName);


        /**
         * 收集类解析的详情
         * 用于{@link C_skip}
         *
         * @param clazz
         * @param msg
         */
        void collect_class(Class<?> clazz, String msg);
    }


    public interface LogCollector_deParse {
        /**
         * 收集每个字段解析的详情
         *
         * @param clazz               实体类
         * @param fieldDeclaringClass 字段所属类
         * @param fieldName           字段名称
         * @param val                 值
         * @param content             值转换成的字节数组
         * @param processorClassName  解析器类名
         */
        void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, byte[] content, String processorClassName);

        void collect_field_skip(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content);

        /**
         * 收集每个字段解析的详情
         *
         * @param clazz               实体类
         * @param fieldDeclaringClass 字段所属类
         * @param fieldName           字段名称
         * @param val                 值
         * @param logs                bit解析日志
         * @param processorClassName  解析器类名
         */
        void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, BitBuf_writer_log.Log[] logs, String processorClassName);

        /**
         * 收集类解析的详情
         * 用于{@link C_skip}
         *
         * @param clazz
         * @param msg
         */
        void collect_class(Class<?> clazz, String msg);
    }
}

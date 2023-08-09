package com.bcd.base.support_parser;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.builder.*;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 解析器
 * 配合注解完成解析工作
 * 会扫描当前类和其父类的所有字段
 * 会忽视如下字段
 * 1、没有被{@link ParseUtil#annoSet}中注解标注的字段
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
@SuppressWarnings("unchecked")
public class Parser {

    public final static Logger logger = LoggerFactory.getLogger(Parser.class);

    private final static FieldBuilder__F_bean fieldBuilder__f_bean = new FieldBuilder__F_bean();
    private final static FieldBuilder__F_bean_list fieldBuilder__f_bean_list = new FieldBuilder__F_bean_list();
    private final static FieldBuilder__F_date field_builder__f_date = new FieldBuilder__F_date();
    private final static FieldBuilder__F_num_array fieldBuilder__f_num_array = new FieldBuilder__F_num_array();
    private final static FieldBuilder__F_num fieldBuilder__f_num = new FieldBuilder__F_num();
    private final static FieldBuilder__F_skip fieldBuilder__f_skip = new FieldBuilder__F_skip();
    private final static FieldBuilder__F_string fieldBuilder__f_string = new FieldBuilder__F_string();
    private final static FieldBuilder__F_customize fieldBuilder__f_customize = new FieldBuilder__F_customize();
    private final static FieldBuilder__F_bit_num fieldbuilder__f_bit_num = new FieldBuilder__F_bit_num();
    private final static FieldBuilder__F_bit_num_array fieldbuilder__f_bit_num_array = new FieldBuilder__F_bit_num_array();
    private final static FieldBuilder__F_bit_skip fieldbuilder__f_bit_skip = new FieldBuilder__F_bit_skip();


    /**
     * 是否在src/main/java下面生成class文件
     * 主要用于开发测试阶段、便于查看生成的结果
     */
    private static boolean generateClassFile = false;

    /**
     * 是否打印javassist生成class的过程日志
     */
    private static boolean printBuildLog = false;


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
    }

    /**
     * 解析log采集器
     * 需要注意的是、此功能用于调试、会在生成的class中加入日志代码、影响性能
     * 而且此功能开启时候避免多线程调用解析、会产生日志混淆、不易调试
     */
    public static LogCollector_parse logCollector_parse;
    public static LogCollector_deParse logCollector_deParse;


    public static void withDefaultLogCollector_parse() {
        logCollector_parse = new LogCollector_parse() {
            @Override
            public void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, byte[] content, Object val, String processorClassName) {
                logger.info("--parse field{}--[{}].[{}] [{}]->[{}]"
                        , LogUtil.getDeclaredFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , ByteBufUtil.hexDump(content).toUpperCase()
                        , val
                );
            }

            @Override
            public void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, BitBuf_reader_log.Log[] logs, Object val, String processorClassName) {
                for (BitBuf_reader_log.Log log : logs) {
                    logger.info("--parse field{}--[{}].[{}] val[{}] {}"
                            , LogUtil.getDeclaredFieldStackTrace(fieldDeclaringClass, fieldName)
                            , clazz.getSimpleName()
                            , fieldName
                            , val
                            , log.msg());
                }
            }
        };

    }

    public static void withDefaultLogCollector_deParse() {
        logCollector_deParse = new LogCollector_deParse() {
            @Override
            public void collect_field(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, byte[] content, String processorClassName) {
                logger.info("--deParse field{}--[{}].[{}] [{}]->[{}]"
                        , LogUtil.getDeclaredFieldStackTrace(fieldDeclaringClass, fieldName)
                        , clazz.getSimpleName()
                        , fieldName
                        , val
                        , ByteBufUtil.hexDump(content).toUpperCase());
            }

            @Override
            public void collect_field_bit(Class<?> clazz, Class<?> fieldDeclaringClass, String fieldName, Object val, BitBuf_writer_log.Log[] logs, String processorClassName) {
                for (BitBuf_writer_log.Log log : logs) {
                    logger.info("--deParse field{}--[{}].[{}] val[{}] {}"
                            , LogUtil.getDeclaredFieldStackTrace(fieldDeclaringClass, fieldName)
                            , clazz.getSimpleName()
                            , fieldName
                            , val
                            , log.msg());
                }
            }
        };
    }

    public static void enablePrintBuildLog() {
        printBuildLog = true;
    }

    public static void enableGenerateClassFile() {
        generateClassFile = true;
    }

    private static void bitEndWhenBitField(List<Field> fieldList, int i, BuilderContext context) {
        final Field cur = fieldList.get(i);
        final F_bit_num f_bit_num1 = cur.getAnnotation(F_bit_num.class);
        final F_bit_num_array f_bit_num_array1 = cur.getAnnotation(F_bit_num_array.class);
        final F_bit_skip f_bit_skip1 = cur.getAnnotation(F_bit_skip.class);
        if (f_bit_num1 != null || f_bit_skip1 != null) {
            context.logBit = true;
        }
        BitRemainingMode bitRemainingMode1 = null;
        if (f_bit_num1 != null) {
            bitRemainingMode1 = f_bit_num1.bitRemainingMode();
        }
        if (f_bit_num_array1 != null) {
            bitRemainingMode1 = f_bit_num_array1.bitRemainingMode();
        }
        if (f_bit_skip1 != null) {
            bitRemainingMode1 = f_bit_skip1.bitRemainingMode();
        }

        if (bitRemainingMode1 == null) {
            return;
        }

        switch (bitRemainingMode1) {
            case ignore -> {
                context.bitEndWhenBitField_process = true;
                context.bitEndWhenBitField_deProcess = true;
            }
            case not_ignore -> {
                context.bitEndWhenBitField_process = false;
                context.bitEndWhenBitField_deProcess = false;
            }
            default -> {
                if (i == fieldList.size() - 1) {
                    context.bitEndWhenBitField_process = true;
                    context.bitEndWhenBitField_deProcess = true;
                } else {
                    final Field next = fieldList.get(i + 1);
                    final F_bit_num f_bit_num2 = next.getAnnotation(F_bit_num.class);
                    final F_bit_num_array f_bit_num_array2 = next.getAnnotation(F_bit_num_array.class);
                    final F_bit_skip f_bit_skip2 = next.getAnnotation(F_bit_skip.class);
                    if (f_bit_num2 == null && f_bit_skip2 == null && f_bit_num_array2 == null) {
                        context.bitEndWhenBitField_process = true;
                        context.bitEndWhenBitField_deProcess = true;
                    } else {
                        context.bitEndWhenBitField_process = false;
                        context.bitEndWhenBitField_deProcess = false;
                    }
                }
            }
        }
    }

    private static void buildMethodBody_process(Class<?> clazz, BuilderContext context) {
        final List<Field> fieldList = ParseUtil.getParseFields(clazz);
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_parse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            bitEndWhenBitField(fieldList, i, context);
            if (logCollector_parse != null) {
                if (!context.logBit) {
                    ParseUtil.prependLogCode_parse(context);
                }
            }
            try {
                final F_num f_num = field.getAnnotation(F_num.class);
                if (f_num != null) {
                    fieldBuilder__f_num.buildParse(context);
                    continue;
                }

                final F_num_array f_num_array = field.getAnnotation(F_num_array.class);
                if (f_num_array != null) {
                    fieldBuilder__f_num_array.buildParse(context);
                    continue;
                }

                final F_string f_string = field.getAnnotation(F_string.class);
                if (f_string != null) {
                    fieldBuilder__f_string.buildParse(context);
                    continue;
                }

                final F_date f_date = field.getAnnotation(F_date.class);
                if (f_date != null) {
                    field_builder__f_date.buildParse(context);
                    continue;
                }

                final F_bean f_bean = field.getAnnotation(F_bean.class);
                if (f_bean != null) {
                    fieldBuilder__f_bean.buildParse(context);
                    continue;
                }

                final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
                if (f_bean_list != null) {
                    fieldBuilder__f_bean_list.buildParse(context);
                    continue;
                }

                final F_customize f_customize = field.getAnnotation(F_customize.class);
                if (f_customize != null) {
                    fieldBuilder__f_customize.buildParse(context);
                }

                final F_skip f_skip = field.getAnnotation(F_skip.class);
                if (f_skip != null) {
                    fieldBuilder__f_skip.buildParse(context);
                }

                final F_bit_num f_bit_num = field.getAnnotation(F_bit_num.class);
                if (f_bit_num != null) {
                    fieldbuilder__f_bit_num.buildParse(context);
                }

                final F_bit_num_array f_bit_num_array = field.getAnnotation(F_bit_num_array.class);
                if (f_bit_num_array != null) {
                    fieldbuilder__f_bit_num_array.buildParse(context);
                }

                final F_bit_skip f_bit_skip = field.getAnnotation(F_bit_skip.class);
                if (f_bit_skip != null) {
                    fieldbuilder__f_bit_skip.buildParse(context);
                }
            } finally {
                if (logCollector_parse != null) {
                    if (context.logBit) {
                        ParseUtil.appendBitLogCode_parse(context);
                    } else {
                        ParseUtil.appendLogCode_parse(context);
                    }
                }
            }
        }

    }

    private static void buildMethodBody_deProcess(Class<?> clazz, BuilderContext context) {
        final List<Field> fieldList = ParseUtil.getParseFields(clazz);
        if (fieldList.isEmpty()) {
            return;
        }
        if (ParseUtil.needBitBuf(fieldList)) {
            ParseUtil.newBitBuf_deParse(context);
        }
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            context.field = field;
            bitEndWhenBitField(fieldList, i, context);
            try {
                if (logCollector_deParse != null) {
                    if (!context.logBit) {
                        ParseUtil.prependLogCode_deParse(context);
                    }

                }
                final F_num f_num = field.getAnnotation(F_num.class);
                if (f_num != null) {
                    fieldBuilder__f_num.buildDeParse(context);
                    continue;
                }

                final F_num_array f_num_array = field.getAnnotation(F_num_array.class);
                if (f_num_array != null) {
                    fieldBuilder__f_num_array.buildDeParse(context);
                    continue;
                }

                final F_string f_string = field.getAnnotation(F_string.class);
                if (f_string != null) {
                    fieldBuilder__f_string.buildDeParse(context);
                    continue;
                }

                final F_date f_date = field.getAnnotation(F_date.class);
                if (f_date != null) {
                    field_builder__f_date.buildDeParse(context);
                    continue;
                }

                final F_bean f_bean = field.getAnnotation(F_bean.class);
                if (f_bean != null) {
                    fieldBuilder__f_bean.buildDeParse(context);
                    continue;
                }

                final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
                if (f_bean_list != null) {
                    fieldBuilder__f_bean_list.buildDeParse(context);
                    continue;
                }

                final F_customize f_customize = field.getAnnotation(F_customize.class);
                if (f_customize != null) {
                    fieldBuilder__f_customize.buildDeParse(context);
                }

                final F_skip f_skip = field.getAnnotation(F_skip.class);
                if (f_skip != null) {
                    fieldBuilder__f_skip.buildDeParse(context);
                }

                final F_bit_num f_bit_num = field.getAnnotation(F_bit_num.class);
                if (f_bit_num != null) {
                    fieldbuilder__f_bit_num.buildDeParse(context);
                }

                final F_bit_num_array f_bit_num_array = field.getAnnotation(F_bit_num_array.class);
                if (f_bit_num_array != null) {
                    fieldbuilder__f_bit_num_array.buildDeParse(context);
                }

                final F_bit_skip f_bit_skip = field.getAnnotation(F_bit_skip.class);
                if (f_bit_skip != null) {
                    fieldbuilder__f_bit_skip.buildDeParse(context);
                }
            } finally {
                if (logCollector_deParse != null) {
                    if (context.logBit) {
                        ParseUtil.appendBitLogCode_deParse(context);
                    } else {
                        ParseUtil.appendLogCode_deParse(context);
                    }
                }
            }
        }
    }

    /**
     * 生成类的序号
     */
    private static int processorIndex = 0;

    public static <T> Class<T> buildClass(Class<T> clazz, BuilderContext parentContext, ByteOrder byteOrder, BitOrder bitOrder) throws CannotCompileException, NotFoundException, IOException {
        final String processor_class_name = Processor.class.getName();
        final String byteBufClassName = ByteBuf.class.getName();
        final String clazzName = clazz.getName();

        final int lastIndexOf = processor_class_name.lastIndexOf(".");
        String implProcessor_class_name = processor_class_name.substring(0, lastIndexOf) + "." + processor_class_name.substring(lastIndexOf + 1) + "_" + processorIndex++ + "_" + clazz.getSimpleName() + "_" + byteOrder + "_" + bitOrder;
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
        final List<Class<?>> processorClassList = Arrays.stream(clazz.getDeclaredFields()).map(f -> f.getAnnotation(F_customize.class)).filter(Objects::nonNull).map(F_customize::processorClass).filter(e -> e != void.class).collect(Collectors.toList());
        for (Class<?> processorClass : processorClassList) {
            final String processorClassName = processorClass.getName();
            final String processorVarName = ParseUtil.getProcessorVarName(processorClass);
            cc.addField(CtField.make("private final " + processorClassName + " " + processorVarName + ";", cc));
            initBody.append(ParseUtil.format("this.{}=new {}();\n", processorVarName, processorClassName));
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
        boolean hasFieldSkipModeReserved = Arrays.stream(clazz.getDeclaredFields()).anyMatch(e -> {
            final F_skip f_skip = e.getAnnotation(F_skip.class);
            if (f_skip == null) {
                return false;
            } else {
                return f_skip.mode() == SkipMode.reservedFromStart;
            }
        });
        if (hasFieldSkipModeReserved) {
            ParseUtil.append(processBody, "final int {}={}.readerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
        }
        BuilderContext parseBuilderContext = new BuilderContext(processBody, clazz, cc, classVarDefineToVarName, beanClassAndOrder_processorVarName, byteOrder, bitOrder, parentContext);
        buildMethodBody_process(clazz, parseBuilderContext);
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
        if (hasFieldSkipModeReserved) {
            ParseUtil.append(deProcessBody, "final int {}={}.writerIndex();\n", FieldBuilder.varNameStartIndex, FieldBuilder.varNameByteBuf);
        }
        BuilderContext deParseBuilderContext = new BuilderContext(deProcessBody, clazz, cc, classVarDefineToVarName, beanClassAndOrder_processorVarName, byteOrder, bitOrder, parentContext);
        buildMethodBody_deProcess(clazz, deParseBuilderContext);
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
     * @param clazz 实体类类型
     * @return
     * @param <T>
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz) {
        return getProcessor(clazz, ByteOrder.Default, BitOrder.Default, null);
    }

    /**
     * 获取类解析器
     * @param clazz 实体类类型
     * @param byteOrder 实体类字节码实现 字节序模式
     * @param bitOrder 实体类字节码实现 位模式
     * @return
     * @param <T>
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder) {
        return getProcessor(clazz, byteOrder, bitOrder, null);
    }

    /**
     * 获取类解析器
     * @param clazz 实体类类型
     * @param byteOrder 实体类字节码实现 字节序模式
     * @param bitOrder 实体类字节码实现 位模式
     * @param parentContext 上层bean的build上下文
     * @return
     * @param <T>
     */
    public static <T> Processor<T> getProcessor(Class<T> clazz, ByteOrder byteOrder, BitOrder bitOrder, BuilderContext parentContext) {
        final String clazzName = clazz.getName();
        final String key = clazzName + "," + byteOrder + "," + bitOrder;
        Processor<T> processor = (Processor<T>) beanClassNameAndOrder_processor.get(key);
        if (processor == null) {
            synchronized (beanClassNameAndOrder_processor) {
                processor = (Processor<T>) beanClassNameAndOrder_processor.get(key);
                if (processor == null) {
                    try {
                        final Class<T> processClass = Parser.buildClass(clazz, parentContext, byteOrder, bitOrder);
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


    public final static Map<String, Processor<?>> beanClassNameAndOrder_processor = new HashMap<>();
}

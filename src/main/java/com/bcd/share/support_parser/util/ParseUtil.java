package com.bcd.share.support_parser.util;


import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.builder.BuilderContext;
import com.bcd.share.support_parser.builder.FieldBuilder;
import com.bcd.share.support_parser.processor.Processor;
import com.google.common.collect.Sets;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ParseUtil {

    static final double[] pows;
    private final static Set<Class<?>> logFieldTypeSet = Sets.newHashSet(
            byte.class, short.class, int.class, long.class, float.class, double.class,
            byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class,
            String.class,
            Date.class
    );
    static Logger logger = LoggerFactory.getLogger(ParseUtil.class);
    /**
     * 生成类的序号
     */
    private static int processorIndex = 0;

    static {
        pows = new double[10];
        for (int i = 0; i < pows.length; i++) {
            pows[i] = Math.pow(10, i);
        }
    }

    public static void notSupport_numType(Class<?> clazz, Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] numType not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_charset(Class<?> clazz, Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] charset not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_type(Class<?> clazz, Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] type not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_fieldType(Class<?> clazz, Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    private static String toFirstLowerCase(final String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static boolean bigEndian(BitOrder order, BitOrder parentOrder) {
        if (parentOrder == null) {
            if (order == BitOrder.Default) {
                return true;
            } else {
                return order == BitOrder.bigEndian;
            }
        } else {
            if (order == BitOrder.Default) {
                if (parentOrder == BitOrder.Default) {
                    return true;
                } else {
                    return parentOrder == BitOrder.bigEndian;
                }
            } else {
                return order == BitOrder.bigEndian;
            }
        }
    }

    public static boolean bigEndian(ByteOrder order, ByteOrder parentOrder) {
        if (parentOrder == null) {
            if (order == ByteOrder.Default) {
                return true;
            } else {
                return order == ByteOrder.bigEndian;
            }
        } else {
            if (order == ByteOrder.Default) {
                if (parentOrder == ByteOrder.Default) {
                    return true;
                } else {
                    return parentOrder == ByteOrder.bigEndian;
                }
            } else {
                return order == ByteOrder.bigEndian;
            }
        }
    }

    /**
     * 定义类变量、解析其中的变量名称
     *
     * @param context
     * @param valDefine
     * @param params
     * @return
     */
    public static String defineClassVar(final BuilderContext context, Class<?> varClass, final String valDefine, Object... params) {
        return context.classVarDefineToVarName.computeIfAbsent(format(valDefine, params), k -> {
            final int size = context.classVarDefineToVarName.size();
            final String varName = "_" + size + "_" + varClass.getSimpleName();
            final CtClass ctClass = context.implCc;
            try {
                final CtField ctField = CtField.make("private final " + varClass.getName() + " " + varName + "=" + k + ";\n", ctClass);
                ctClass.addField(ctField);
            } catch (CannotCompileException e) {
                throw BaseRuntimeException.getException(e);
            }
            return varName;
        });
    }

    private static String getFieldByteBufReaderIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_readerIndex";
    }

    private static String getFieldByteBufWriterIndexVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_byteBuf_writerIndex";
    }

    private static String getFieldLogBytesVarName(final BuilderContext context) {
        final String fieldVarName = getFieldVarName(context);
        return fieldVarName + "_log_bytes";
    }

    public static boolean needLog(final BuilderContext context) {
        final Class<?> fieldType = context.field.getType();
        if (logFieldTypeSet.contains(fieldType)) {
            return true;
        } else {
            return fieldType.isEnum();
        }
    }

    public static boolean needBitBuf(List<Field> fieldList) {
        return fieldList.stream().anyMatch(e -> e.isAnnotationPresent(F_bit_num.class) || e.isAnnotationPresent(F_bit_num_array.class) || e.isAnnotationPresent(F_bit_skip.class));
    }

    public static void newBitBuf_parse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_reader_className = Parser.logCollector_parse == null ? BitBuf_reader.class.getName() : BitBuf_reader_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_reader" : "getBitBuf_reader_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
        context.varNameBitBuf = FieldBuilder.varNameBitBuf;
    }

    public static void newBitBuf_deParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_writer_className = Parser.logCollector_parse == null ? BitBuf_writer.class.getName() : BitBuf_writer_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_writer" : "getBitBuf_writer_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
        context.varNameBitBuf = FieldBuilder.varNameBitBuf;
    }

    public static void appendBitLogCode_parse(final BuilderContext context) {
        final String varNameBitBuf = context.getVarNameBitBuf_reader();
        if (varNameBitBuf != null) {
            final Class<?> clazz = context.clazz;
            final Class<?> declaringClass = context.field.getDeclaringClass();
            final String fieldName = context.field.getName();
            append(context.body, "{}.logCollector_parse.collect_field_bit({}.class,{}.class,\"{}\",{}.takeLogs(),{},\"{}\");\n",
                    Parser.class.getName()
                    , clazz.getName()
                    , declaringClass.getName()
                    , fieldName
                    , varNameBitBuf
                    , boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType())
                    , context.implCc.getSimpleName());
        }
    }

    public static void appendBitLogCode_deParse(final BuilderContext context) {
        final String varNameBitBuf = context.getVarNameBitBuf_writer();
        if (varNameBitBuf != null) {
            final Class<?> clazz = context.clazz;
            final Class<?> declaringClass = context.field.getDeclaringClass();
            final String fieldName = context.field.getName();
            append(context.body, "{}.logCollector_deParse.collect_field_bit({}.class,{}.class,\"{}\",{},{}.takeLogs(),\"{}\");\n",
                    Parser.class.getName()
                    , clazz.getName()
                    , declaringClass.getName()
                    , fieldName
                    , boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType())
                    , varNameBitBuf
                    , context.implCc.getSimpleName());
        }
    }

    public static void prependLogCode_parse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String varName = getFieldByteBufReaderIndexVarName(context);
        append(context.body, "final int {}={}.readerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_parse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String fieldByteBufReaderIndexVarName = getFieldByteBufReaderIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.body, "final byte[] {}=new byte[{}.readerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName);
        append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName, fieldLogBytesVarName);
        append(context.body, "{}.logCollector_parse.collect_field({}.class,{}.class,\"{}\",{},{},\"{}\");\n",
                Parser.class.getName(),
                context.clazz.getName(),
                context.field.getDeclaringClass().getName(),
                context.field.getName(),
                fieldLogBytesVarName,
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()),
                context.implCc.getSimpleName());
    }

    public static void prependLogCode_deParse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }
        final String varName = getFieldByteBufWriterIndexVarName(context);
        append(context.body, "final int {}={}.writerIndex();\n", varName, FieldBuilder.varNameByteBuf);
    }

    public static void appendLogCode_deParse(final BuilderContext context) {
        if (!needLog(context)) {
            return;
        }

        final String fieldByteBufWriterIndexVarName = getFieldByteBufWriterIndexVarName(context);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context);
        append(context.body, "final byte[] {}=new byte[{}.writerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName);
        append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName, fieldLogBytesVarName);
        append(context.body, "{}.logCollector_deParse.collect_field({}.class,{}.class,\"{}\",{},{},\"{}\");\n",
                Parser.class.getName(),
                context.clazz.getName(),
                context.field.getDeclaringClass().getName(),
                context.field.getName(),
                boxing(FieldBuilder.varNameInstance + "." + context.field.getName(), context.field.getType()),
                fieldLogBytesVarName,
                context.implCc.getSimpleName());
    }

    public static String getFieldVarName(final BuilderContext context) {
        return context.field.getName();
    }

    public static String replaceValExprToCode(final String expr, final String valExpr) {
        if (expr.isEmpty()) {
            return valExpr;
        }
        final StringBuilder sb = new StringBuilder();
        final char[] chars = expr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && c != '.' && !Character.isDigit(c)) {
                sb.append(valExpr);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String replaceValExprToCode_round(final String expr, final String valExpr) {
        return format("{}.round(", ParseUtil.class.getName()) + replaceValExprToCode(expr, valExpr) + ")";
    }

    public static String unBoxing(final String num, final Class<?> clazz) {
        if (clazz == byte.class) {
            return num + ".byteValue()";
        } else if (clazz == short.class) {
            return num + ".shortValue()";
        } else if (clazz == int.class) {
            return num + ".intValue()";
        } else if (clazz == long.class) {
            return num + ".longValue()";
        } else if (clazz == float.class) {
            return num + ".floatValue()";
        } else if (clazz == double.class) {
            return num + ".doubleValue()";
        } else {
            return num;
        }
    }

    public static String boxing(final String num, final Class<?> clazz) {
        if (clazz == byte.class) {
            return "Byte.valueOf(" + num + ")";
        } else if (clazz == short.class) {
            return "Short.valueOf(" + num + ")";
        } else if (clazz == int.class) {
            return "Integer.valueOf(" + num + ")";
        } else if (clazz == long.class) {
            return "Long.valueOf(" + num + ")";
        } else if (clazz == float.class) {
            return "Float.valueOf(" + num + ")";
        } else if (clazz == double.class) {
            return "Double.valueOf(" + num + ")";
        } else {
            return num;
        }
    }

    public static String replaceLenExprToCode(final String lenExpr, final Map<Character, String> map, final Field field) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = lenExpr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && !Character.isDigit(c)) {
                final String s = map.get(c);
                if (s == null) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] expr[{}] can't find char[{}] value", field.getDeclaringClass().getName(), field.getName(), lenExpr, c);
                }
                //所有的len字段必须转化为int运算
                sb.append("(int)(").append(s).append(")");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将信息转换为格式化
     * 使用方式和sl4j log一样、例如
     * {@link org.slf4j.Logger#info(String, Object...)}
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(final String message, final Object... params) {
        return MessageFormatter.arrayFormat(message, params, null).getMessage();
    }

    public static void append(final StringBuilder sb, final String message, Object... params) {
        sb.append(format(message, params));
    }

    public static void insert(final StringBuilder sb, int index, final String message, Object... params) {
        sb.insert(index, format(message, params));
    }

    public static long round(double d) {
        if (d > 0d) {
            return Math.round(d);
        } else if (d == 0d) {
            return 0;
        } else {
            return -Math.round(-d);
        }
    }

    public static int round(float f) {
        if (f > 0d) {
            return Math.round(f);
        } else if (f == 0d) {
            return 0;
        } else {
            return -Math.round(-f);
        }
    }

    public static double format(double d, int i) {
        if (d > 0) {
            if (i == 0) {
                return Math.floor(d);
            } else {
                return Math.floor(d * pows[i]) / pows[i];
            }

        } else if (d < 0) {
            if (i == 0) {
                return Math.ceil(d);
            } else {
                return Math.ceil(d * pows[i]) / pows[i];
            }
        } else {
            return 0;
        }
    }

    public static boolean needParse(Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (Parser.anno_fieldBuilder.containsKey(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static List<Field> getParseFields(Class<?> clazz) {
        return ClassUtil.getAllFields(clazz).stream().filter(ParseUtil::needParse).collect(Collectors.toList());
    }

    public static String getProcessKey(Class<?> clazz, ByteOrder byteOrder, BitOrder bitOrder) {
        return clazz.getName()
                + "_" + (byteOrder == ByteOrder.smallEndian ? 0 : 1)
                + "_" + (bitOrder == BitOrder.smallEndian ? 0 : 1);
    }

    public static String getProcessClassName(Class<?> clazz, ByteOrder byteOrder, BitOrder bitOrder) {
        String clazzName = Processor.class.getName();
        return clazzName.substring(0, clazzName.lastIndexOf(".")) + ".P_" + (processorIndex++) + "_" + clazz.getSimpleName()
                + "_" + (byteOrder == ByteOrder.smallEndian ? 0 : 1)
                + "_" + (bitOrder == BitOrder.smallEndian ? 0 : 1);
    }

    public static boolean checkChildrenHasAnno_F_customize(Class<?> clazz) {
        List<Field> parseFields = getParseFields(clazz);
        for (int i = 0; i < parseFields.size(); i++) {
            Field field = parseFields.get(i);
            F_customize f_customize = field.getAnnotation(F_customize.class);
            if (f_customize != null) {
                return true;
            }
            F_bean f_bean = field.getAnnotation(F_bean.class);
            if (f_bean != null) {
                Class<?> fieldType = field.getType();
                parseFields.addAll(getParseFields(fieldType));
            }
            F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
            if (f_bean_list != null) {
                Class<?> fieldType = field.getType();
                if (fieldType.isArray()) {
                    parseFields.addAll(getParseFields(fieldType.getComponentType()));
                } else {
                    Class<?> actualTypeArgument = (Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                    parseFields.addAll(getParseFields(actualTypeArgument));
                }
            }
        }
        return false;
    }


    public static Map<Class<? extends Annotation>, FieldBuilder> getAllFieldBuild() {
        String pkg = "com.bcd.share.support_parser.builder";
        Map<Class<? extends Annotation>, FieldBuilder> map = new HashMap<>();
        try {
            List<Class<?>> classes = ClassUtil.getClasses(pkg);
            for (Class<?> clazz : classes) {
                if (clazz != FieldBuilder.class && FieldBuilder.class.isAssignableFrom(clazz)) {
                    FieldBuilder instance = (FieldBuilder) clazz.getConstructor().newInstance();
                    map.put(instance.annoClass(), instance);
                }
            }
        } catch (Exception e) {
            throw BaseRuntimeException.getException(e);
        }
        logger.info("scan pkg[{}] list[{}]:", pkg, map.size());
        for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : map.entrySet()) {
            logger.info("Anno[{}] FieldBuilder[{}]", entry.getKey().getName(), entry.getValue().getClass().getName());
        }
        return map;
    }

    public static void main(String[] args) {
        getAllFieldBuild();
    }
}

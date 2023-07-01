package com.bcd.base.support_parser.util;


import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.google.common.collect.Sets;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import org.slf4j.helpers.MessageFormatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ParseUtil {
    public static void notSupport_numType(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] numType not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_charset(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] charset not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_type(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] type not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_order(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] order not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_fieldType(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_len(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_singleLen(final Field field, Class<?> annoClass) {
        throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] singleLen not support", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
    }

    private static String toFirstLowerCase(final String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String getProcessorVarName(final Class<?> processorClass) {
        return "_" + toFirstLowerCase(processorClass.getSimpleName());
    }

    public static boolean bigEndian(BitOrder order, BitOrder pkgOrder) {
        if (pkgOrder == null) {
            if (order == BitOrder.Default) {
                return true;
            } else {
                return order == BitOrder.bigEndian;
            }
        } else {
            if (order == BitOrder.Default) {
                if (pkgOrder == BitOrder.Default) {
                    return true;
                } else {
                    return pkgOrder == BitOrder.bigEndian;
                }
            } else {
                return order == BitOrder.bigEndian;
            }
        }
    }

    /**
     * @param order
     * @param clazz
     * @return
     */
    public static boolean bigEndian(BitOrder order, Class<?> clazz) {
        BitOrder configOrder = null;
        final String className = clazz.getName();
        for (Parser.BitOrderConfig config : Parser.bitOrderConfigs) {
            if (className.startsWith(config.classPrefix())) {
                configOrder = config.order();
            }
        }
        return bigEndian(order, configOrder);
    }

    public static boolean bigEndian(ByteOrder order, ByteOrder pkgOrder) {
        if (pkgOrder == null) {
            if (order == ByteOrder.Default) {
                return true;
            } else {
                return order == ByteOrder.bigEndian;
            }
        } else {
            if (order == ByteOrder.Default) {
                if (pkgOrder == ByteOrder.Default) {
                    return true;
                } else {
                    return pkgOrder == ByteOrder.bigEndian;
                }
            } else {
                return order == ByteOrder.bigEndian;
            }
        }
    }

    /**
     * @param order
     * @param clazz
     * @return
     */
    public static boolean bigEndian(ByteOrder order, Class<?> clazz) {
        ByteOrder configOrder = null;
        final String className = clazz.getName();
        for (Parser.ByteOrderConfig config : Parser.byteOrderConfigs) {
            if (className.startsWith(config.classPrefix())) {
                configOrder = config.order();
            }
        }
        return bigEndian(order, configOrder);


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


    private final static Set<Class<?>> logFieldTypeSet = Sets.newHashSet(
            byte.class, short.class, int.class, long.class, float.class, double.class,
            byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class,
            String.class,
            Date.class
    );

    public static boolean needLog(final BuilderContext context) {
        final Class<?> fieldType = context.field.getType();
        if (logFieldTypeSet.contains(fieldType)) {
            return true;
        } else {
            return fieldType.isEnum();
        }
    }

    public static boolean needBitBuf(List<Field> fieldList) {
        return fieldList.stream().anyMatch(e -> {
            if (e.isAnnotationPresent(F_bit_num.class) || e.isAnnotationPresent(F_bit_num_array.class) || e.isAnnotationPresent(F_bit_skip.class)) {
                return true;
            } else {
                final F_bean f_bean = e.getAnnotation(F_bean.class);
                if (f_bean != null && f_bean.passBitBuf()) {
                    return true;
                }
                final F_bean_list f_bean_list = e.getAnnotation(F_bean_list.class);
                if (f_bean_list != null && f_bean_list.passBitBuf()) {
                    return true;
                }
                final F_customize f_customize = e.getAnnotation(F_customize.class);
                if (f_customize != null && f_customize.passBitBuf()) {
                    return true;
                }
            }
            return false;
        });
    }

    public static void newBitBuf_parse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_reader_className = BitBuf_reader.class.getName();
        final String bitBuf_reader_log_className = BitBuf_reader_log.class.getName();

        if (Parser.logCollector_parse == null) {
            ParseUtil.append(body, "final {} {};\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf);
            ParseUtil.append(body, "if ({} == null || {}.bitBuf_reader == null) {\n", FieldBuilder.varNameParentProcessContext, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "{}=new {}({});\n", FieldBuilder.varNameBitBuf, bitBuf_reader_className, FieldBuilder.varNameByteBuf);
            ParseUtil.append(body, "}else{\n");
            ParseUtil.append(body, "{}={}.bitBuf_reader;\n", FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "}\n");
        } else {
            ParseUtil.append(body, "final {} {};\n", bitBuf_reader_log_className, FieldBuilder.varNameBitBuf);
            ParseUtil.append(body, "if ({} == null || {}.bitBuf_reader == null) {\n", FieldBuilder.varNameParentProcessContext, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "{}=new {}({});\n", FieldBuilder.varNameBitBuf, bitBuf_reader_log_className, FieldBuilder.varNameByteBuf);
            ParseUtil.append(body, "}else{\n");
            ParseUtil.append(body, "{}=({}){}.bitBuf_reader;\n", FieldBuilder.varNameBitBuf, bitBuf_reader_log_className, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "}\n");
        }
        context.varNameBitBuf = FieldBuilder.varNameBitBuf;
    }

    public static void newBitBuf_deParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_writer_className = BitBuf_writer.class.getName();
        final String bitBuf_writer_log_className = BitBuf_writer_log.class.getName();
        if (Parser.logCollector_parse == null) {
            ParseUtil.append(body, "final {} {};\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf);
            ParseUtil.append(body, "if ({} == null || {}.bitBuf_writer == null) {\n", FieldBuilder.varNameParentProcessContext, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "{}=new {}({});\n", FieldBuilder.varNameBitBuf, bitBuf_writer_className, FieldBuilder.varNameByteBuf);
            ParseUtil.append(body, "}else{\n");
            ParseUtil.append(body, "{}={}.bitBuf_writer;\n", FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "}\n");
        } else {
            ParseUtil.append(body, "final {} {};\n", bitBuf_writer_log_className, FieldBuilder.varNameBitBuf);
            ParseUtil.append(body, "if ({} == null || {}.bitBuf_writer == null) {\n", FieldBuilder.varNameParentProcessContext, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "{}=new {}({});\n", FieldBuilder.varNameBitBuf, bitBuf_writer_log_className, FieldBuilder.varNameByteBuf);
            ParseUtil.append(body, "}else{\n");
            ParseUtil.append(body, "{}=({}){}.bitBuf_writer;\n", FieldBuilder.varNameBitBuf, bitBuf_writer_log_className, FieldBuilder.varNameParentProcessContext);
            ParseUtil.append(body, "}\n");
        }
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


    public static String unBoxing(final String num, final Class clazz) {
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

    public static String boxing(final String num, final Class clazz) {
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

    static final double[] pows;

    static {
        pows = new double[10];
        for (int i = 0; i < pows.length; i++) {
            pows[i] = Math.pow(10, i);
        }
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


    public final static Set<Class<?>> annoSet = new HashSet<>();

    static {
        annoSet.add(F_num.class);
        annoSet.add(F_num_array.class);

        annoSet.add(F_string.class);

        annoSet.add(F_date.class);

        annoSet.add(F_bean.class);
        annoSet.add(F_bean_list.class);

        annoSet.add(F_customize.class);

        annoSet.add(F_skip.class);

        annoSet.add(F_bit_num.class);
        annoSet.add(F_bit_num_array.class);
        annoSet.add(F_bit_skip.class);
    }


    public static boolean needParse(Field field) {
        final Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annoSet.contains(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    public static List<Field> getParseFields(Class<?> clazz) {
        return ClassUtil.getAllFields(clazz).stream().filter(ParseUtil::needParse).collect(Collectors.toList());
    }

}

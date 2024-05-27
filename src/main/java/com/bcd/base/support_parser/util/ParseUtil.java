package com.bcd.base.support_parser.util;


import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.processor.Processor;
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
    static Logger logger = LoggerFactory.getLogger(ParseUtil.class);
    /**
     * 生成类的序号
     */
    private static int processorIndex = 0;

    private final static Set<Class<?>> logFieldTypeSet = Sets.newHashSet(
            byte.class, short.class, int.class, long.class, float.class, double.class,
            byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class,
            String.class,
            Date.class);

    static final double[] pows;

    static {
        pows = new double[10];
        for (int i = 0; i < pows.length; i++) {
            pows[i] = Math.pow(10, i);
        }
    }

    public static void notSupport_numType(Class<?> clazz, Field field, Class<?> annoClass) {
        throw MyException.get("class[{}] field[{}] anno[{}] numType not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_type(Class<?> clazz, Field field, Class<?> annoClass) {
        throw MyException.get("class[{}] field[{}] anno[{}] type not support", clazz.getName(), field.getName(), annoClass.getName());
    }

    public static void notSupport_fieldType(Class<?> clazz, Field field, Class<?> annoClass) {
        throw MyException.get("class[{}] field[{}] anno[{}] not support", clazz.getName(), field.getName(), annoClass.getName());
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
                throw MyException.get(e);
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
            if (fieldType.isEnum()) {
                return true;
            } else {
                return context.field.isAnnotationPresent(F_customize.class);
            }
        }
    }

    public static boolean needBitBuf(List<Field> fieldList) {
        return fieldList.stream().anyMatch(e -> e.isAnnotationPresent(F_bit_num.class) || e.isAnnotationPresent(F_bit_num_array.class));
    }

    public static void newBitBuf_parse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_reader_className = Parser.logCollector_parse == null ? BitBuf_reader.class.getName() : BitBuf_reader_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_reader" : "getBitBuf_reader_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_reader_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
    }

    public static void newBitBuf_deParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String bitBuf_writer_className = Parser.logCollector_parse == null ? BitBuf_writer.class.getName() : BitBuf_writer_log.class.getName();
        final String funcName = Parser.logCollector_parse == null ? "getBitBuf_writer" : "getBitBuf_writer_log";
        ParseUtil.append(body, "final {} {}={}.{}();\n", bitBuf_writer_className, FieldBuilder.varNameBitBuf, FieldBuilder.varNameParentProcessContext, funcName);
    }

    public static void appendBitLogCode_parse(final BuilderContext context) {
        final String varNameBitBuf = context.getBitBuf_parse();
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
        final String varNameBitBuf = context.getBitBuf_deParse();
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
                    throw MyException.get("class[{}] field[{}] expr[{}] can't find char[{}] value", field.getDeclaringClass().getName(), field.getName(), lenExpr, c);
                }
                //所有的len字段必须转化为int运算
                sb.append("(int)(").append(s).append(")");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String replaceLenExprToCode(final String lenExpr, final Map<Character, String> map, Class<?> clazz) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = lenExpr.toCharArray();
        for (char c : chars) {
            if (c != '+' && c != '-' && c != '*' && c != '/' && !Character.isDigit(c)) {
                final String s = map.get(c);
                if (s == null) {
                    throw MyException.get("class[{}] c_skip lenExpr[{}] can't find char[{}] value", clazz.getName(), lenExpr, c);
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

    public static Map<Class<? extends Annotation>, FieldBuilder> getAllFieldBuild() {
        String parserClassName = Parser.class.getName();
        String pkg = parserClassName.substring(0, parserClassName.lastIndexOf("."));
        Map<Class<? extends Annotation>, FieldBuilder> map = new HashMap<>();
        try {
            List<Class<?>> classes = ClassUtil.getClasses(pkg + ".builder");
            for (Class<?> clazz : classes) {
                if (clazz != FieldBuilder.class && FieldBuilder.class.isAssignableFrom(clazz)) {
                    FieldBuilder instance = (FieldBuilder) clazz.getConstructor().newInstance();
                    String clazzSimpleName = clazz.getSimpleName();
                    String annoSimpleClassName = clazzSimpleName.substring(clazzSimpleName.indexOf("__") + 2);
                    String annoClassName = pkg + ".anno." + annoSimpleClassName;
                    map.put((Class<? extends Annotation>) Class.forName(annoClassName), instance);
                }
            }
        } catch (Exception e) {
            throw MyException.get(e);
        }

        StringJoiner sj = new StringJoiner("\n");
        for (Map.Entry<Class<? extends Annotation>, FieldBuilder> entry : map.entrySet()) {
            sj.add(format("Anno[{}] FieldBuilder[{}]", entry.getKey().getName(), entry.getValue().getClass().getName()));
        }
        logger.info("scan pkg[{}] list[{}]:\n{}", pkg, map.size(), sj);
        return map;
    }

    public static int getClassByteLenIfPossible(Class<?> clazz) {
        int all = 0;
        List<Field> parseFields = getParseFields(clazz);
        int bit = 0;
        int maxBitEndEasy = 0;
        for (int i = 0; i < parseFields.size(); i++) {
            Field parseField = parseFields.get(i);
            F_skip f_skip = parseField.getAnnotation(F_skip.class);
            if (f_skip != null) {
                if (f_skip.lenExprBefore().isEmpty() && f_skip.lenExprAfter().isEmpty()) {
                    all += (f_skip.lenBefore() + f_skip.lenAfter());
                } else {
                    return -1;
                }
            }

            F_num f_num = parseField.getAnnotation(F_num.class);
            if (f_num != null) {
                switch (f_num.type()) {
                    case uint8, int8 -> all += 1;
                    case uint16, int16 -> all += 2;
                    case uint24, int24 -> all += 3;
                    case uint32, int32, float32 -> all += 4;
                    case uint40, int40 -> all += 5;
                    case uint48, int48 -> all += 6;
                    case uint56, int56 -> all += 7;
                    case uint64, int64, float64 -> all += 8;
                    default -> {
                        return -1;
                    }
                }
                continue;
            }

            F_num_array f_num_array = parseField.getAnnotation(F_num_array.class);
            if (f_num_array != null) {
                int len = f_num_array.len();
                int singleSkip = f_num_array.singleSkip();
                if (len > 0) {
                    switch (f_num_array.singleType()) {
                        case uint8, int8 -> all += (1 + singleSkip) * len;
                        case uint16, int16 -> all += (2 + singleSkip) * len;
                        case uint24, int24 -> all += (3 + singleSkip) * len;
                        case uint32, int32, float32 -> all += (4 + singleSkip) * len;
                        case uint40, int40 -> all += (5 + singleSkip) * len;
                        case uint48, int48 -> all += (6 + singleSkip) * len;
                        case uint56, int56 -> all += (7 + singleSkip) * len;
                        case uint64, int64, float64 -> all += (8 + singleSkip) * len;
                        default -> {
                            return -1;
                        }
                    }
                } else {
                    return -1;
                }
                continue;
            }


            F_bit_num f_bit_num = parseField.getAnnotation(F_bit_num.class);
            if (f_bit_num != null) {
                bit += (f_bit_num.skipBefore() + f_bit_num.len() + f_bit_num.skipAfter());
                boolean ignore;
                switch (f_bit_num.bitRemainingMode()) {
                    case ignore -> ignore = true;
                    case not_ignore -> ignore = false;
                    default -> {
                        if (i == parseFields.size() - 1) {
                            ignore = true;
                        } else {
                            Field nextField = parseFields.get(i + 1);
                            ignore = !nextField.isAnnotationPresent(F_bit_num.class)
                                    && !nextField.isAnnotationPresent(F_bit_num_array.class);
                        }
                    }
                }
                if (ignore) {
                    all += (bit / 8) + (bit % 8 == 0 ? 0 : 1);
                    bit = 0;
                }
                continue;
            }

            F_bit_num_array f_bit_num_array = parseField.getAnnotation(F_bit_num_array.class);
            if (f_bit_num_array != null) {
                int len = f_bit_num_array.len();
                if (len > 0) {
                    bit += (f_bit_num_array.skipBefore() + f_bit_num_array.skipAfter());
                    bit += len * (f_bit_num_array.singleLen() + f_bit_num_array.singleSkip());
                    boolean ignore;
                    switch (f_bit_num_array.bitRemainingMode()) {
                        case ignore -> ignore = true;
                        case not_ignore -> ignore = false;
                        default -> {
                            if (i == parseFields.size() - 1) {
                                ignore = true;
                            } else {
                                Field nextField = parseFields.get(i + 1);
                                ignore = !nextField.isAnnotationPresent(F_bit_num.class)
                                        && !nextField.isAnnotationPresent(F_bit_num_array.class);
                            }
                        }
                    }
                    if (ignore) {
                        all += (bit / 8) + (bit % 8 == 0 ? 0 : 1);
                        bit = 0;
                    }
                } else {
                    return -1;
                }
                continue;
            }

            F_bit_num_easy f_bit_num_easy = parseField.getAnnotation(F_bit_num_easy.class);
            if (f_bit_num_easy != null) {
                maxBitEndEasy = Math.max(maxBitEndEasy, f_bit_num_easy.bitEnd());
                boolean end;
                if (f_bit_num_easy.end()) {
                    end = true;
                } else {
                    if (i == parseFields.size() - 1) {
                        end = true;
                    } else {
                        Field nextField = parseFields.get(i + 1);
                        end = !nextField.isAnnotationPresent(F_bit_num_easy.class);
                    }
                }
                if (end) {
                    all += (maxBitEndEasy / 8) + (maxBitEndEasy % 8 == 0 ? 0 : 1);
                    maxBitEndEasy = 0;
                }
            }


            F_string f_string = parseField.getAnnotation(F_string.class);
            if (f_string != null) {
                int len = f_string.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_string_bcd f_string_bcd = parseField.getAnnotation(F_string_bcd.class);
            if (f_string_bcd != null) {
                int len = f_string_bcd.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_date_bcd f_date_bcd = parseField.getAnnotation(F_date_bcd.class);
            if (f_date_bcd != null) {
                int len = f_date_bcd.len();
                if (len > 0) {
                    all += len;
                } else {
                    return -1;
                }
                continue;
            }

            F_date_bytes_6 f_date_bytes_6 = parseField.getAnnotation(F_date_bytes_6.class);
            if (f_date_bytes_6 != null) {
                all += 6;
                continue;
            }
            F_date_bytes_7 f_date_bytes_7 = parseField.getAnnotation(F_date_bytes_7.class);
            if (f_date_bytes_7 != null) {
                all += 7;
                continue;
            }
            F_date_ts f_date_ts = parseField.getAnnotation(F_date_ts.class);
            if (f_date_ts != null) {
                switch (f_date_ts.mode()) {
                    case uint64_ms, uint64_s, float64_ms, float64_s -> all += 8;
                    case uint32_s -> all += 4;
                    default -> {
                        return -1;
                    }
                }
                continue;
            }

            F_bean f_bean = parseField.getAnnotation(F_bean.class);
            if (f_bean != null) {
                int beanLen = getClassByteLenIfPossible(parseField.getType());
                if (beanLen == -1) {
                    return -1;
                } else {
                    all += beanLen;
                }
            }

            F_bean_list f_bean_list = parseField.getAnnotation(F_bean_list.class);
            if (f_bean_list != null) {
                int listLen = f_bean_list.listLen();
                if (listLen > 0) {
                    int beanLen;
                    final Class<?> fieldType = parseField.getType();
                    if (fieldType.isArray()) {
                        beanLen = getClassByteLenIfPossible(fieldType.getComponentType());
                    } else if (List.class.isAssignableFrom(fieldType)) {
                        beanLen = getClassByteLenIfPossible((Class<?>) ((ParameterizedType) parseField.getGenericType()).getActualTypeArguments()[0]);
                    } else {
                        return -1;
                    }
                    if (beanLen == -1) {
                        return -1;
                    } else {
                        all += beanLen * listLen;
                    }
                } else {
                    return -1;
                }
            }

            F_customize f_customize = parseField.getAnnotation(F_customize.class);
            if (f_customize != null) {
                return -1;
            }

        }
        return all;
    }

    public static void appendSkip_parse(int len, String lenExpr, BuilderContext context) {
        String lenValCode;
        if (len == 0) {
            lenValCode = ParseUtil.replaceLenExprToCode(lenExpr, context.varToFieldName, context.field);
        } else {
            lenValCode = len + "";
        }
        final String fieldByteBufReaderIndexVarName = getFieldByteBufReaderIndexVarName(context) + "_skip_" + (context.varIndex++);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context) + "_skip_" + (context.varIndex++);
        if (Parser.logCollector_parse != null) {
            append(context.body, "final int {}={}.readerIndex();\n", fieldByteBufReaderIndexVarName, FieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(context.body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, lenValCode);
        if (Parser.logCollector_parse != null) {
            append(context.body, "final byte[] {}=new byte[{}.readerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName);
            append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufReaderIndexVarName, fieldLogBytesVarName);
            append(context.body, "{}.logCollector_parse.collect_field_skip({}.class,{}.class,\"{}\",{});\n",
                    Parser.class.getName(),
                    context.clazz.getName(),
                    context.field.getDeclaringClass().getName(),
                    context.field.getName(),
                    fieldLogBytesVarName);
        }
    }

    public static void appendSkip_deParse(int len, String lenExpr, BuilderContext context) {
        String lenValCode;
        if (len == 0) {
            lenValCode = ParseUtil.replaceLenExprToCode(lenExpr, context.varToFieldName, context.field);
        } else {
            lenValCode = len + "";
        }
        final String fieldByteBufWriterIndexVarName = getFieldByteBufWriterIndexVarName(context) + "_skip_" + (context.varIndex++);
        final String fieldLogBytesVarName = getFieldLogBytesVarName(context) + "_skip_" + (context.varIndex++);
        if (Parser.logCollector_deParse != null) {
            append(context.body, "final int {}={}.writerIndex();\n", fieldByteBufWriterIndexVarName, FieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(context.body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, lenValCode);
        if (Parser.logCollector_deParse != null) {
            append(context.body, "final byte[] {}=new byte[{}.writerIndex()-{}];\n", fieldLogBytesVarName, FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName);
            append(context.body, "{}.getBytes({},{});\n", FieldBuilder.varNameByteBuf, fieldByteBufWriterIndexVarName, fieldLogBytesVarName);
            append(context.body, "{}.logCollector_deParse.collect_field_skip({}.class,{}.class,\"{}\",{});\n",
                    Parser.class.getName(),
                    context.clazz.getName(),
                    context.field.getDeclaringClass().getName(),
                    context.field.getName(),
                    fieldLogBytesVarName);
        }
    }

    public static void main(String[] args) {
//        getAllFieldBuild();
//        System.out.println(getClassByteLenIfPossible(Evt_0001.class));
//        System.out.println(getClassByteLenIfPossible(Evt_0800.class));
//        System.out.println(getClassByteLenIfPossible(Evt_0006.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D006.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D010.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D011.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D012.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D013.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D014.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D015.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D016.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D017.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D008.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D009.class));
//        System.out.println(getClassByteLenIfPossible(Evt_D00A.class));
    }
}

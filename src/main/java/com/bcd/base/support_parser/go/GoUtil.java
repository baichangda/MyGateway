package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class GoUtil {

    public final static GoFieldBuilder__F_num fieldBuilder__f_num = new GoFieldBuilder__F_num();
    public final static GoFieldBuilder__F_num_array fieldBuilder__f_num_array = new GoFieldBuilder__F_num_array();
    public final static GoFieldBuilder__F_bit_num fieldBuilder__f_bit_num = new GoFieldBuilder__F_bit_num();
    public final static GoFieldBuilder__F_bit_num_array fieldBuilder__f_bit_num_array = new GoFieldBuilder__F_bit_num_array();
    public final static GoFieldBuilder__F_float_ieee754 fieldBuilder__f_float_ieee754 = new GoFieldBuilder__F_float_ieee754();

    private static boolean hasBitField(List<Field> parseFields) {
        return parseFields.stream().anyMatch(e -> e.isAnnotationPresent(F_bit_num.class) ||
                e.isAnnotationPresent(F_bit_num_array.class) ||
                e.isAnnotationPresent(F_bit_skip.class) ||
                e.isAnnotationPresent(F_customize.class));
    }

    private static void bitEndWhenBitField(List<Field> fieldList, int i, GoBuildContext context) {
        final Field cur = fieldList.get(i);
        final F_bit_num f_bit_num1 = cur.getAnnotation(F_bit_num.class);
        final F_bit_num_array f_bit_num_array1 = cur.getAnnotation(F_bit_num_array.class);
        final F_bit_skip f_bit_skip1 = cur.getAnnotation(F_bit_skip.class);
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
            case Ignore -> {
                context.bitEndWhenBitField_process = true;
                context.bitEndWhenBitField_deProcess = true;
            }
            case Not_ignore -> {
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

    public final static void toSourceCode(String pkg, ByteOrder byteOrder, BitOrder bitOrder, String goFilePath, String goPkg) {
        final StringBuilder body = new StringBuilder();
        final List<Class> classes;
        try {
            classes = ClassUtil.getClasses(pkg);
        } catch (IOException | ClassNotFoundException ex) {
            throw BaseRuntimeException.getException(ex);
        }

        for (Class clazz : classes) {
            final List<Field> parseFields = ParseUtil.getParseFields(clazz);
            if (parseFields.isEmpty()) {
                continue;
            }
            final boolean hasBitField = hasBitField(parseFields);
            final StringBuilder structBody = new StringBuilder();
            final StringBuilder parseBody = new StringBuilder();
            final StringBuilder deParseBody = new StringBuilder();
            final GoBuildContext context = new GoBuildContext(clazz, byteOrder, bitOrder, structBody, parseBody, deParseBody);
            ParseUtil.append(structBody, "type {} struct{\n", context.goStructName);
            if (hasBitField) {
                ParseUtil.append(parseBody, "func To{}({} *util.ByteBuf,{} *util.BitBuf_reader) (*{},error){\n", context.goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameBitBuf, context.goStructName);
                ParseUtil.append(deParseBody, "func({} *{})Write({} *util.ByteBuf,{} *util.BitBuf_writer){\n", GoFieldBuilder.varNameInstance, context.goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameBitBuf);
            } else {
                ParseUtil.append(parseBody, "func To{}({} *util.ByteBuf) (*{},error){\n", context.goStructName, GoFieldBuilder.varNameByteBuf, context.goStructName);
                ParseUtil.append(deParseBody, "func({} *{})Write({} *util.ByteBuf){\n", GoFieldBuilder.varNameInstance, context.goStructName, GoFieldBuilder.varNameByteBuf);
            }
            ParseUtil.append(parseBody, "{}:={}{}\n", GoFieldBuilder.varNameInstance, context.goStructName);
            for (int i = 0; i < parseFields.size(); i++) {
                final Field field = parseFields.get(i);
                context.setField(field, i);
                bitEndWhenBitField(parseFields, i, context);
                GoFieldBuilder goFieldBuilder = null;
                if (field.isAnnotationPresent(F_num.class)) {
                    goFieldBuilder = fieldBuilder__f_num;
                } else if (field.isAnnotationPresent(F_num_array.class)) {
                    goFieldBuilder = fieldBuilder__f_num_array;
                } else if (field.isAnnotationPresent(F_bit_num.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_num;
                } else if (field.isAnnotationPresent(F_bit_num_array.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_num_array;
                } else if (field.isAnnotationPresent(F_float_ieee754.class)) {
                    goFieldBuilder = fieldBuilder__f_float_ieee754;
                }
                if (goFieldBuilder != null) {
                    goFieldBuilder.buildStruct(context);
                    goFieldBuilder.buildParse(context);
                    goFieldBuilder.buildDeParse(context);
                }
            }
            ParseUtil.append(structBody, "}\n");
            ParseUtil.append(parseBody, "return &{},nil\n", GoFieldBuilder.varNameInstance);
            ParseUtil.append(parseBody, "}\n");
            ParseUtil.append(deParseBody, "}\n");
            body.append(structBody);
            body.append("\n");
            body.append(parseBody);
            body.append("\n");
            body.append(deParseBody);
            body.append("\n");
        }

        final Path path = Paths.get(goFilePath);
        try (final BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            bw.write("package " + goPkg);
            bw.newLine();
            bw.write(body.toString());
            bw.flush();
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
    }

    private static String toFieldDefine(Field field) {
        return toFirstUpperCase(field.getName()) + " " + toFieldType(field);
    }

    private static String toFieldType(Field field) {
        Annotation anno;
        if ((anno = field.getAnnotation(F_num.class)) != null) {
            F_num f_num = (F_num) anno;
            switch (f_num.len()) {
                case 1 -> {
                    return f_num.unsigned() ? "uint8" : "int8";
                }
                case 2 -> {
                    return f_num.unsigned() ? "uint16" : "int16";
                }
                case 4 -> {
                    return f_num.unsigned() ? "uint32" : "int32";
                }
                case 8 -> {
                    return f_num.unsigned() ? "uint64" : "int64";
                }
                default -> {
                    ParseUtil.notSupport_len(field, f_num.getClass());
                    return "";
                }
            }
        } else if ((anno = field.getAnnotation(F_bit_num.class)) != null) {
            F_bit_num f_bit_num = (F_bit_num) anno;
            final int len = f_bit_num.len();
            if (len >= 1 && len <= 8) {
                return f_bit_num.unsigned() ? "uint8" : "int8";
            } else if (len >= 9 && len <= 16) {
                return f_bit_num.unsigned() ? "uint16" : "int16";
            } else if (len >= 17 && len <= 32) {
                return f_bit_num.unsigned() ? "uint32" : "int32";
            } else if (len >= 33 && len <= 64) {
                return f_bit_num.unsigned() ? "uint64" : "int64";
            } else {
                ParseUtil.notSupport_len(field, f_bit_num.getClass());
                return "";
            }
        } else if ((anno = field.getAnnotation(F_num_array.class)) != null) {
            F_num_array f_num_array = (F_num_array) anno;
            switch (f_num_array.singleLen()) {
                case 1 -> {
                    return f_num_array.unsigned() ? "[]uint8" : "[]int8";
                }
                case 2 -> {
                    return f_num_array.unsigned() ? "[]uint16" : "[]int16";
                }
                case 4 -> {
                    return f_num_array.unsigned() ? "[]uint32" : "[]int32";
                }
                case 8 -> {
                    return f_num_array.unsigned() ? "[]uint64" : "[]int64";
                }
                default -> {
                    ParseUtil.notSupport_singleLen(field, f_num_array.getClass());
                    return "";
                }
            }
        } else if ((anno = field.getAnnotation(F_bit_num_array.class)) != null) {
            F_bit_num_array f_bit_num_array = (F_bit_num_array) anno;
            final int singleLen = f_bit_num_array.singleLen();
            if (singleLen >= 1 && singleLen <= 8) {
                return f_bit_num_array.unsigned() ? "[]uint8" : "[]int8";
            } else if (singleLen >= 9 && singleLen <= 16) {
                return f_bit_num_array.unsigned() ? "[]uint16" : "[]int16";
            } else if (singleLen >= 17 && singleLen <= 32) {
                return f_bit_num_array.unsigned() ? "[]uint32" : "[]int32";
            } else if (singleLen >= 33 && singleLen <= 64) {
                return f_bit_num_array.unsigned() ? "[]uint64" : "[]int64";
            } else {
                ParseUtil.notSupport_len(field, f_bit_num_array.getClass());
                return "";
            }
        } else if ((anno = field.getAnnotation(F_string.class)) != null) {
            return "string";
        } else if ((anno = field.getAnnotation(F_date.class)) != null) {
            return "time.Time";
        } else if ((anno = field.getAnnotation(F_float_ieee754.class)) != null) {
            F_float_ieee754 f_float_ieee754 = (F_float_ieee754) anno;
            if (f_float_ieee754.type() == FloatType_ieee754.Float32) {
                return "float32";
            } else {
                return "float64";
            }
        } else if ((anno = field.getAnnotation(F_float_ieee754_array.class)) != null) {
            F_float_ieee754_array f_float_ieee754_array = (F_float_ieee754_array) anno;
            if (f_float_ieee754_array.type() == FloatType_ieee754.Float32) {
                return "[]float32";
            } else {
                return "[]float64";
            }
        } else if ((anno = field.getAnnotation(F_bean.class)) != null) {
            final String simpleName = field.getType().getSimpleName();
            return toFirstUpperCase(simpleName);
        } else if ((anno = field.getAnnotation(F_bean_list.class)) != null) {
            final String simpleName = field.getType().getComponentType().getSimpleName();
            return "[]" + toFirstUpperCase(simpleName);
        } else if ((anno = field.getAnnotation(F_customize.class)) != null) {
            return "interface{}";
        } else {
            return null;
        }
    }

    private static String toStruct(Class<?> clazz) {
        final List<Field> fields = ParseUtil.getParseFields(clazz);
        if (fields.isEmpty()) {
            return null;
        }
        final String clazzSimpleName = clazz.getSimpleName();
        final StringBuilder sb = new StringBuilder();
        sb.append("type ");
        sb.append(toFirstUpperCase(clazzSimpleName));
        sb.append(" struct{\n");
        for (Field field : fields) {
            sb.append("  ").append(toFieldDefine(field)).append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }


    public static String toFirstUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static void main(String[] args) {
        final String s = "com.bcd.base.support_parser.impl.immotors.ep33.data";
//        final String s = "com.bcd.base.support_parser.impl.gb32960.data";
        toSourceCode(s, ByteOrder.BigEndian, BitOrder.BigEndian,
                "/Users/baichangda/bcd/goworkspace/MyGateway_go/test.go", "main");
    }
}

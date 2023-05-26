package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import com.bcd.base.support_parser.impl.gb32960.data.VehicleCommonData;
import com.bcd.base.support_parser.impl.gb32960.data.VehicleSupplementData;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoUtil {

    public final static GoFieldBuilder__F_num fieldBuilder__f_num = new GoFieldBuilder__F_num();

    public final static String toSourceCode(String pkg) {
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
            final StringBuilder structBody = new StringBuilder();
            final StringBuilder parseFuncBody = new StringBuilder();
            final StringBuilder deParseFuncBody = new StringBuilder();
            final GoBuildContext context = new GoBuildContext(clazz, structBody, parseFuncBody, deParseFuncBody);
            ParseUtil.append(structBody, "type {} struct{\n", context.goStructName);
            ParseUtil.append(parseFuncBody, "func To{}(buf *util.ByteBuf) (*{},error){\n", context.goStructName, context.goStructName);
            ParseUtil.append(parseFuncBody, "  e:={}{}\n", context.goStructName);
            ParseUtil.append(deParseFuncBody, "func(e *{})Write(buf *util.ByteBuf){\n", context.goStructName);
            for (int i = 0; i < parseFields.size(); i++) {
                final Field field = parseFields.get(i);
                context.setField(field, i);
                final F_num f_num = field.getAnnotation(F_num.class);
                if (f_num != null) {
                    fieldBuilder__f_num.buildStruct(context);
                    fieldBuilder__f_num.buildFunc(context);
                    continue;
                }
            }
            ParseUtil.append(structBody, "}\n");
            ParseUtil.append(parseFuncBody, "  return &e,nil\n");
            ParseUtil.append(parseFuncBody, "}\n");
            ParseUtil.append(deParseFuncBody, "}\n");
            body.append(structBody);
            body.append("\n");
            body.append(parseFuncBody);
            body.append("\n");
            body.append(deParseFuncBody);
            body.append("\n");
        }
        return body.toString();
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
        final String readSourceCode = toSourceCode("com.bcd.base.support_parser.impl.gb32960.data");
        System.out.println(readSourceCode);
    }
}

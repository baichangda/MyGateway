package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class GoUtil {
    public final static Set<String> noPointerStructSet = new HashSet<>();
    public final static Set<String> unsafePointerStructSet = new HashSet<>();
    public final static Set<String> unsafePointerStructFieldTypeSet = new HashSet<>();
    static {
        unsafePointerStructFieldTypeSet.add("int8");
        unsafePointerStructFieldTypeSet.add("int8");
        unsafePointerStructFieldTypeSet.add("int16");
        unsafePointerStructFieldTypeSet.add("int16");
        unsafePointerStructFieldTypeSet.add("int32");
        unsafePointerStructFieldTypeSet.add("int32");
        unsafePointerStructFieldTypeSet.add("int64");
        unsafePointerStructFieldTypeSet.add("int64");
        unsafePointerStructFieldTypeSet.add("float32");
        unsafePointerStructFieldTypeSet.add("float32");
        unsafePointerStructFieldTypeSet.add("float64");
        unsafePointerStructFieldTypeSet.add("float64");
    }

    public final static Map<String, String> zoneId_varNameLocation = new HashMap<>();

    public final static GoFieldBuilder__F_num fieldBuilder__f_num = new GoFieldBuilder__F_num();
    public final static GoFieldBuilder__F_num_array fieldBuilder__f_num_array = new GoFieldBuilder__F_num_array();
    public final static GoFieldBuilder__F_bit_num fieldBuilder__f_bit_num = new GoFieldBuilder__F_bit_num();
    public final static GoFieldBuilder__F_bit_num_array fieldBuilder__f_bit_num_array = new GoFieldBuilder__F_bit_num_array();
    public final static GoFieldBuilder__F_float_ieee754 fieldBuilder__f_float_ieee754 = new GoFieldBuilder__F_float_ieee754();
    public final static GoFieldBuilder__F_float_ieee754_array fieldBuilder__f_float_ieee754_array = new GoFieldBuilder__F_float_ieee754_array();
    public final static GoFieldBuilder__F_string fieldBuilder__f_string = new GoFieldBuilder__F_string();
    public final static GoFieldBuilder__F_skip fieldBuilder__f_skip = new GoFieldBuilder__F_skip();
    public final static GoFieldBuilder__F_date fieldBuilder__f_date = new GoFieldBuilder__F_date();
    public final static GoFieldBuilder__F_bit_skip fieldBuilder__f_bit_skip = new GoFieldBuilder__F_bit_skip();
    public final static GoFieldBuilder__F_bean fieldBuilder__f_bean = new GoFieldBuilder__F_bean();
    public final static GoFieldBuilder__F_bean_list fieldBuilder__f_bean_list = new GoFieldBuilder__F_bean_list();
    public final static GoFieldBuilder__F_customize fieldBuilder__f_customize = new GoFieldBuilder__F_customize();


    private static void initNoPointerStructSet(Class<?> clazz) {
        noPointerStructSet.addAll(ParseUtil.getParseFields(clazz).stream().filter(e -> e.isAnnotationPresent(F_bean_list.class)).map(field -> {
            final Class<?> fieldType = field.getType();
            final Class<?> c;
            if (fieldType.isArray()) {
                c = fieldType.getComponentType();
            } else {
                c = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
            }
            return toFirstUpperCase(c.getSimpleName());
        }).collect(Collectors.toSet()));
    }

    private static void initUnsafePointerStructSet(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            final List<Field> parseFields = ParseUtil.getParseFields(clazz);
            for (Field field : parseFields) {
                final Class<?> fieldType = field.getType();
            }

        }
    }

    private static boolean hasFieldSkipModeReserved(List<Field> parseFields) {
        return parseFields.stream().anyMatch(e -> {
            final F_skip f_skip = e.getAnnotation(F_skip.class);
            if (f_skip == null) {
                return false;
            } else {
                return f_skip.mode() == SkipMode.ReservedFromStart;
            }
        });
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

    public final static void toSourceCode(String pkg, ByteOrder byteOrder, BitOrder bitOrder, String goFilePath) {
        final StringBuilder body = new StringBuilder();
        final StringBuilder customizeBody = new StringBuilder();
        final List<Class<?>> classes;
        try {
            classes = ClassUtil.getClasses(pkg);
        } catch (IOException | ClassNotFoundException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        for (Class<?> clazz : classes) {
            initNoPointerStructSet(clazz);
        }

        final StringBuilder globalBody = new StringBuilder();
        for (Class<?> clazz : classes) {
            final List<Field> parseFields = ParseUtil.getParseFields(clazz);
            if (parseFields.isEmpty()) {
                continue;
            }
            final boolean hasFieldSkipModeReserved = hasFieldSkipModeReserved(parseFields);
            final StringBuilder structBody = new StringBuilder();
            final StringBuilder parseBody = new StringBuilder();
            final StringBuilder deParseBody = new StringBuilder();
            final GoBuildContext context = new GoBuildContext(clazz, byteOrder, bitOrder, globalBody, structBody, parseBody, deParseBody, customizeBody);
            final String goStructName = context.goStructName;
            ParseUtil.append(structBody, "type {} struct{\n", goStructName);
            if (noPointerStructSet.contains(goStructName)) {
                ParseUtil.append(parseBody, "func To{}({} *parse.ByteBuf,{} *parse.ParseContext) {}{\n",
                        goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName);
                ParseUtil.append(deParseBody, "func({} {})Write({} *parse.ByteBuf,{} *parse.ParseContext){\n",
                        GoFieldBuilder.varNameInstance, goStructName,
                        GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            } else {
                ParseUtil.append(parseBody, "func To{}({} *parse.ByteBuf,{} *parse.ParseContext) *{}{\n",
                        goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName);
                ParseUtil.append(deParseBody, "func(_{} *{})Write({} *parse.ByteBuf,{} *parse.ParseContext){\n",
                        GoFieldBuilder.varNameInstance, goStructName,
                        GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
                ParseUtil.append(deParseBody, "{}:= *_{}\n", GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameInstance);
            }
            ParseUtil.append(parseBody, "{}:={}{}\n", GoFieldBuilder.varNameInstance, goStructName);


            if (hasFieldSkipModeReserved) {
                ParseUtil.append(parseBody, "{}:={}.ReaderIndex()\n", GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(deParseBody, "{}:={}.WriterIndex()\n", GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
            }


            for (int i = 0; i < parseFields.size(); i++) {
                final Field field = parseFields.get(i);
                context.setField(field);
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
                } else if (field.isAnnotationPresent(F_float_ieee754_array.class)) {
                    goFieldBuilder = fieldBuilder__f_float_ieee754_array;
                } else if (field.isAnnotationPresent(F_string.class)) {
                    goFieldBuilder = fieldBuilder__f_string;
                } else if (field.isAnnotationPresent(F_skip.class)) {
                    goFieldBuilder = fieldBuilder__f_skip;
                } else if (field.isAnnotationPresent(F_date.class)) {
                    goFieldBuilder = fieldBuilder__f_date;
                } else if (field.isAnnotationPresent(F_bit_skip.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_skip;
                } else if (field.isAnnotationPresent(F_bean.class)) {
                    goFieldBuilder = fieldBuilder__f_bean;
                } else if (field.isAnnotationPresent(F_bean_list.class)) {
                    goFieldBuilder = fieldBuilder__f_bean_list;
                } else if (field.isAnnotationPresent(F_customize.class)) {
                    goFieldBuilder = fieldBuilder__f_customize;
                }
                if (goFieldBuilder != null) {
                    goFieldBuilder.buildStruct(context);
                    goFieldBuilder.buildParse(context);
                    goFieldBuilder.buildDeParse(context);
                }
            }


            ParseUtil.append(structBody, "}\n");
            if (noPointerStructSet.contains(goStructName)) {
                ParseUtil.append(parseBody, "return {}\n", GoFieldBuilder.varNameInstance);
            } else {
                ParseUtil.append(parseBody, "return &{}\n", GoFieldBuilder.varNameInstance);
            }
            ParseUtil.append(parseBody, "}\n");
            ParseUtil.append(deParseBody, "}\n");

            body.append(structBody);
            body.append("\n");
            body.append(parseBody);
            body.append("\n");
            body.append(deParseBody);
            body.append("\n");
        }

        body.insert(0, globalBody);

        final Path path = Paths.get(goFilePath);
        final Path parent = path.getParent();
        final Path goPkg = parent.getName(parent.getNameCount() - 1);
        try {
            Files.createDirectories(parent);
            try (final BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write("package " + goPkg);
                bw.newLine();
                bw.write(body.toString());
                bw.flush();
            }
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        final Path customizePath = Paths.get(parent + "/customize.go");
        if (!customizeBody.isEmpty() && !Files.exists(customizePath)) {
            try (final BufferedWriter bw = Files.newBufferedWriter(customizePath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                bw.write("package " + goPkg);
                bw.newLine();
                bw.write(customizeBody.toString());
                bw.flush();
            } catch (IOException ex) {
                throw BaseRuntimeException.getException(ex);
            }
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
//        final String s = "com.bcd.base.support_parser.impl.icd.data";
//        toSourceCode(s, ByteOrder.BigEndian, BitOrder.BigEndian, "/Users/baichangda/bcd/goworkspace/MyGateway_go/support_parse/icd/java.go");
//        final String s = "com.bcd.base.support_parser.impl.gb32960.data";
//        toSourceCode(s, ByteOrder.BigEndian, BitOrder.BigEndian, "/Users/baichangda/bcd/goworkspace/MyGateway_go/support_parse/gb32960/java.go");
        final String s = "com.bcd.base.support_parser.impl.immotors.ep33.data";
        toSourceCode(s, ByteOrder.BigEndian, BitOrder.BigEndian, "/Users/baichangda/bcd/goworkspace/MyGateway_go/support_parse/immotors/ep33/java.go");
    }
}

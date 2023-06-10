package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class GoParseUtil {
    //非指针结构体、parse和deParse 对象都为结构体
    public final static Set<String> noPointerStructSet = new HashSet<>();

    //可以直接使用unsafe指针转换的结构体
    public final static Map<String, Integer> unsafePointerStruct_byteLen = new HashMap<>();

    static void initNoPointerStructSet(Class<?> clazz) {
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

    static void initUnsafePointerStructSet(Class<?> clazz, ByteOrder pkg_byteOrder) {
        final String goStructName = toFirstUpperCase(clazz.getSimpleName());
        if (unsafePointerStruct_byteLen.containsKey(goStructName)) {
            return;
        }

        unsafePointerStruct_byteLen.put(goStructName, -1);
        final List<Field> parseFields = ParseUtil.getParseFields(clazz);
        int byteLen = 0;
        for (Field field : parseFields) {
            final Class<?> fieldType = field.getType();
            final F_num f_num = field.getAnnotation(F_num.class);
            if (f_num != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_num.order(), pkg_byteOrder);
                if (bigEndian && f_num.len() > 0 && f_num.valExpr().isEmpty()) {
                    byteLen += f_num.len();
                    continue;
                } else {
                    return;
                }
            }

            final F_num_array f_num_array = field.getAnnotation(F_num_array.class);
            if (f_num_array != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_num_array.order(), pkg_byteOrder);
                if (bigEndian && f_num_array.len() > 0 && f_num_array.valExpr().isEmpty() && f_num_array.singleSkip() == 0) {
                    byteLen += f_num_array.len() * f_num_array.singleLen();
                    continue;
                } else {
                    return;
                }
            }
            final F_bean f_bean = field.getAnnotation(F_bean.class);
            if (f_bean != null) {
                final String goFieldTypeStructName = toFirstUpperCase(fieldType.getSimpleName());
                Integer fieldByteLen = unsafePointerStruct_byteLen.get(goFieldTypeStructName);
                if (fieldByteLen == null) {
                    initUnsafePointerStructSet(fieldType, pkg_byteOrder);
                    fieldByteLen = unsafePointerStruct_byteLen.get(goFieldTypeStructName);
                }
                if (fieldByteLen >= 0) {
                    byteLen += fieldByteLen;
                    continue;
                } else {
                    return;
                }
            }

            final F_bean_list f_bean_list = field.getAnnotation(F_bean_list.class);
            if (f_bean_list != null) {
                if (f_bean_list.listLen() > 0) {
                    Class<?> goFieldType;
                    if (fieldType.isArray()) {
                        goFieldType = fieldType.getComponentType();
                    } else {
                        goFieldType = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
                    }
                    final String goFieldTypeStructName = toFirstUpperCase(goFieldType.getSimpleName());
                    Integer fieldByteLen = unsafePointerStruct_byteLen.get(goFieldTypeStructName);
                    if (fieldByteLen == null) {
                        initUnsafePointerStructSet(fieldType, pkg_byteOrder);
                        fieldByteLen = unsafePointerStruct_byteLen.get(goFieldTypeStructName);
                    }
                    if (fieldByteLen >= 0) {
                        byteLen += fieldByteLen * f_bean_list.listLen();
                        continue;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }

            final F_float_ieee754 f_float_ieee754 = field.getAnnotation(F_float_ieee754.class);
            if (f_float_ieee754 != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_float_ieee754.order(), pkg_byteOrder);
                if (bigEndian && f_float_ieee754.valExpr().isEmpty()) {
                    switch (f_float_ieee754.type()) {
                        case Float32 -> {
                            byteLen += 4;
                        }
                        case Float64 -> {
                            byteLen += 8;
                        }
                    }
                    continue;
                } else {
                    return;
                }
            }

            final F_float_ieee754_array f_float_ieee754_array = field.getAnnotation(F_float_ieee754_array.class);
            if (f_float_ieee754_array != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_float_ieee754_array.order(), pkg_byteOrder);
                if (bigEndian && f_float_ieee754_array.len() > 0 && f_float_ieee754_array.valExpr().isEmpty()) {
                    switch (f_float_ieee754.type()) {
                        case Float32 -> {
                            byteLen += 4 * f_float_ieee754_array.len();
                        }
                        case Float64 -> {
                            byteLen += 8 * f_float_ieee754_array.len();
                        }
                    }
                    continue;
                } else {
                    return;
                }
            }
            return;
        }
        unsafePointerStruct_byteLen.put(goStructName, byteLen);
    }

    static boolean hasFieldSkipModeReserved(List<Field> parseFields) {
        return parseFields.stream().anyMatch(e -> {
            final F_skip f_skip = e.getAnnotation(F_skip.class);
            if (f_skip == null) {
                return false;
            } else {
                return f_skip.mode() == SkipMode.ReservedFromStart;
            }
        });
    }

    static void bitEndWhenBitField(List<Field> fieldList, int i, GoBuildContext context) {
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

    public static String wrapTypeNameFunc(String goTypeName, boolean bigEndian) {
        if ("uin8".equals(goTypeName) || "int8".equals(goTypeName)) {
            return goTypeName;
        } else {
            return goTypeName + (bigEndian ? "" : "_le");
        }
    }


    public static String toFirstUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String get_unsafe_slice_to_array_1(String structType, int arrLen) {
        return ParseUtil.format("[{}]{}({}.Read_bytes({}))", arrLen, structType, GoFieldBuilder.varNameByteBuf, arrLen);
    }

    public static String get_unsafe_slice_to_array_2(String structType, int arrLen, int sliceLen) {
        return ParseUtil.format("(*[{}]{})(unsafe.Pointer(unsafe.SliceData({}.Read_bytes({}))))", arrLen, structType, GoFieldBuilder.varNameByteBuf, sliceLen);
    }

    public static String get_unsafe_array_to_slice_1(String arrValCode) {
        return ParseUtil.format("{}[:]", arrValCode);
    }

    public static String get_unsafe_array_to_slice_2(String arrValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
                    Data: uintptr(unsafe.Pointer(&{})),
                    Len:  {},
                    Cap:  {},
                }))""", arrValCode, sliceLen, sliceLen);
    }

    public static String get_unsafe_slice_to_struct(String structType, int sliceLen) {
        return ParseUtil.format("(*{})(unsafe.Pointer(unsafe.SliceData({}.Read_bytes({}))))", structType, GoFieldBuilder.varNameByteBuf, sliceLen);
    }

    public static String get_unsafe_struct_to_slice(String structValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
                	Data: uintptr(unsafe.Pointer({})),
                	Len:  {},
                	Cap:  {},
                }))""", structValCode, sliceLen, sliceLen);
    }

    public static String get_unsafe_slice_to_structArr(String structType, int arrLen, int sliceLen) {
        return ParseUtil.format("(*[{}]{})(unsafe.Pointer(unsafe.SliceData({}.Read_bytes({}))))", arrLen, structType, GoFieldBuilder.varNameByteBuf, sliceLen);
    }

    public static String get_unsafe_structArr_to_slice(String structValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
                	Data: uintptr(unsafe.Pointer({})),
                	Len:  {},
                	Cap:  {},
                }))""", structValCode, sliceLen, sliceLen);
    }

    public static void appendUnsafeStructFunc(StringBuilder body, String goStructName, int structByteLen) {
        if (GoParseUtil.noPointerStructSet.contains(goStructName)) {
            ParseUtil.append(body, """
                            func To{}({} *parse.ByteBuf, {} *parse.ParseContext) {} {
                                 return *(*{})(unsafe.Pointer(unsafe.SliceData({}.Read_bytes({}))))
                            }
                            func({} {})Write({} *parse.ByteBuf,{} *parse.ParseContext){
                                {}.Write_bytes(*(*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
                                    Data: uintptr(unsafe.Pointer(&{})),
                                    Len:  {},
                                    Cap:  {},
                                })))
                            }
                            """,
                    goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName, goStructName, GoFieldBuilder.varNameByteBuf, structByteLen,
                    GoFieldBuilder.varNameInstance, goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameInstance, structByteLen, structByteLen);
        } else {
            ParseUtil.append(body, """
                            func To{}({} *parse.ByteBuf, {} *parse.ParseContext) *{} {
                                 return (*{})(unsafe.Pointer(unsafe.SliceData({}.Read_bytes({}))))
                            }
                            func(_{} *{})Write({} *parse.ByteBuf,{} *parse.ParseContext){
                                {}.Write_bytes(*(*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
                                    Data: uintptr(unsafe.Pointer(_{})),
                                    Len:  {},
                                    Cap:  {},
                                })))
                            }
                            """,
                    goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName, goStructName, GoFieldBuilder.varNameByteBuf, structByteLen,
                    GoFieldBuilder.varNameInstance, goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameInstance, structByteLen, structByteLen);
        }
    }


}

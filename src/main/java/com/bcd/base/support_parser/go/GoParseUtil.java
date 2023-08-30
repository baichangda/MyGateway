package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoParseUtil {
    //可以直接使用unsafe指针转换的结构体
    public final static Map<String, Integer> unsafePointerStruct_byteLen = new HashMap<>();

    static void initUnsafePointerStructSet(Class<?> clazz, ByteOrder pkg_byteOrder) {
        final String goStructName = toFirstUpperCase(clazz.getSimpleName());
        if (unsafePointerStruct_byteLen.containsKey(goStructName)) {
            return;
        }

        unsafePointerStruct_byteLen.put(goStructName, -1);
        final List<Field> parseFields = ParseUtil.getParseFields(clazz);
        int byteLen = 0;
        for (Field field : parseFields) {
            final F_num f_num = field.getAnnotation(F_num.class);
            if (f_num != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_num.order(), pkg_byteOrder);
                if (bigEndian && f_num.valExpr().isEmpty()) {
                    byteLen += getByteLength(f_num.type());
                    continue;
                } else {
                    return;
                }
            }

            final F_num_array f_num_array = field.getAnnotation(F_num_array.class);
            if (f_num_array != null) {
                final boolean bigEndian = ParseUtil.bigEndian(f_num_array.singleOrder(), pkg_byteOrder);
                if (bigEndian && f_num_array.len() > 0 && f_num_array.singleValExpr().isEmpty() && f_num_array.singleSkip() == 0) {
                    byteLen += f_num_array.len() * getByteLength(f_num_array.singleType());
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
                return f_skip.mode() == SkipMode.reservedFromStart;
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
        return ParseUtil.format("[{}]{}({}.Read_slice_{}({}))", arrLen, structType, GoFieldBuilder.varNameByteBuf, structType, arrLen);
    }

    public static String get_unsafe_slice_to_array_2(String structType, int arrLen, int sliceLen) {
        return ParseUtil.format("(*[{}]{})(unsafe.Pointer(unsafe.SliceData({}.Read_slice_{}({}))))", arrLen, structType, GoFieldBuilder.varNameByteBuf, structType, sliceLen);
    }

    public static String get_unsafe_array_to_slice_1(String arrValCode) {
        return ParseUtil.format("{}[:]", arrValCode);
    }

    public static String get_unsafe_array_to_slice_2(String structType, String arrValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]{})(unsafe.Pointer(&reflect.SliceHeader{
                    Data: uintptr(unsafe.Pointer(&{})),
                    Len:  {},
                    Cap:  {},
                }))""", structType, arrValCode, sliceLen, sliceLen);
    }

    public static String get_unsafe_slice_to_struct(String structType, int sliceLen) {
        return ParseUtil.format("(*{})(unsafe.Pointer(unsafe.SliceData({}.Read_slice_uint8({}))))", structType, GoFieldBuilder.varNameByteBuf, sliceLen);
    }

    public static String get_unsafe_struct_to_slice(String structValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]uint8)(unsafe.Pointer(&reflect.SliceHeader{
                	Data: uintptr(unsafe.Pointer({})),
                	Len:  {},
                	Cap:  {},
                }))""", structValCode, sliceLen, sliceLen);
    }

    public static String get_unsafe_slice_to_structArr(String structType, int arrLen, int sliceLen) {
        return ParseUtil.format("(*[{}]{})(unsafe.Pointer(unsafe.SliceData({}.Read_slice_uint8({}))))", arrLen, structType, GoFieldBuilder.varNameByteBuf, sliceLen);
    }

    public static String get_unsafe_structArr_to_slice(String structValCode, int sliceLen) {
        return ParseUtil.format("""
                (*[]uint8)(unsafe.Pointer(&reflect.SliceHeader{
                	Data: uintptr(unsafe.Pointer({})),
                	Len:  {},
                	Cap:  {},
                }))""", structValCode, sliceLen, sliceLen);
    }

    public static int getByteLength(NumType type) {
        switch (type) {
            case uint8, int8 -> {
                return 1;
            }
            case uint16, int16 -> {
                return 2;
            }
            case uint32, int32, float32 -> {
                return 4;
            }
            case uint64, int64, float64 -> {
                return 8;
            }
            default -> {
                return 0;
            }
        }
    }

    public static void appendUnsafeStructFunc(StringBuilder body, String goStructName, int structByteLen) {
        ParseUtil.append(body, """
                        func To_{}({} *parse.ByteBuf, {} *parse.ParseContext) *{} {
                             return (*{})(unsafe.Pointer(unsafe.SliceData({}.Read_slice_uint8({}))))
                        }
                        func(_{} *{})Write({} *parse.ByteBuf,{} *parse.ParseContext){
                            {}.Write_slice_uint8(*(*[]byte)(unsafe.Pointer(&reflect.SliceHeader{
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

package com.bcd.root.support_parser.go;

import com.bcd.root.support_parser.anno.F_num_array;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.util.ParseUtil;
import com.bcd.root.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_num_array extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_num_array anno = field.getAnnotation(F_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_num_array> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final String jsonExt = goField.jsonExt;
        final int len = anno.len();
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final Class<?> fieldType = field.getType();
        final NumType singleType = anno.singleType();
        final NumType singleValType = anno.singleValType();
        final String goFieldTypeName;
        final String goReadTypeName;
        switch (singleType) {
            case uint8 -> {
                goReadTypeName = "uint8";
            }
            case int8 -> {
                goReadTypeName = "int8";
            }
            case uint16 -> {
                goReadTypeName = "uint16";
            }
            case int16 -> {
                goReadTypeName = "int16";
            }
            case uint32 -> {
                goReadTypeName = "uint32";
            }
            case int32 -> {
                goReadTypeName = "int32";
            }
            case uint64 -> {
                goReadTypeName = "uint64";
            }
            case int64 -> {
                goReadTypeName = "int64";
            }
            case float32 -> {
                goReadTypeName = "float32";
            }
            case float64 -> {
                goReadTypeName = "float64";
            }
            default -> {
                ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                goReadTypeName = null;
            }
        }

        switch (singleValType) {
            case uint8 -> {
                goFieldTypeName = "uint8";
            }
            case int8 -> {
                goFieldTypeName = "int8";
            }
            case uint16 -> {
                goFieldTypeName = "uint16";
            }
            case int16 -> {
                goFieldTypeName = "int16";
            }
            case uint32 -> {
                goFieldTypeName = "uint32";
            }
            case int32 -> {
                goFieldTypeName = "int32";
            }
            case uint64 -> {
                goFieldTypeName = "uint64";
            }
            case int64 -> {
                goFieldTypeName = "int64";
            }
            case float32 -> {
                goFieldTypeName = "float32";
            }
            case float64 -> {
                goFieldTypeName = "float64";
            }
            default -> {
                goFieldTypeName = goReadTypeName;
            }
        }
        goField.goFieldTypeName = goFieldTypeName;
        goField.goReadTypeName = goReadTypeName;
        if (len == 0) {
            if ("uint8".equals(goFieldTypeName)) {
                ParseUtil.append(body, "{} parse.JsonUint8Arr {}\n", goFieldName, jsonExt);
            } else {
                ParseUtil.append(body, "{} []{} {}\n", goFieldName, goFieldTypeName, jsonExt);
            }
        } else {
            ParseUtil.append(body, "{} [{}]{} {}\n", goFieldName, len, goFieldTypeName, jsonExt);
        }
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_num_array anno = field.getAnnotation(F_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goReadTypeName = goField.goReadTypeName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_num_array> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final int singleSkip = anno.singleSkip();
        final String valExpr = anno.singleValExpr();
        final NumType singleType = anno.singleType();
        final NumType singleValType = anno.singleValType();
        String varNameLen = goFieldName + "_len";
        final int len = anno.len();
        final String varNameArr = goFieldName + "_arr";
        int singleLen;
        switch (singleType) {
            case uint8, int8 -> {
                singleLen = 1;
            }
            case uint16, int16 -> {
                singleLen = 2;
            }
            case uint32, int32, float32 -> {
                singleLen = 4;
            }
            case uint64, int64, float64 -> {
                singleLen = 8;
            }
            default -> {
                ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                singleLen = 0;
            }
        }
        if (len > 0 && valExpr.isEmpty() && singleSkip == 0) {
            if (singleLen == 1) {
                ParseUtil.append(body, "{}:={}\n", varNameArr, GoParseUtil.get_unsafe_slice_to_array_1(goFieldTypeName, len));
            } else {
                ParseUtil.append(body, "{}:=(*{})\n", varNameArr, GoParseUtil.get_unsafe_slice_to_array_2(goFieldTypeName, len, len * singleLen));
            }
        } else {
            if (len == 0) {
                ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            } else {
                ParseUtil.append(body, "{}:={}\n", varNameLen, len);
            }
            if (singleLen == 1 && valExpr.isEmpty() && singleSkip == 0) {
                ParseUtil.append(body, "{}:={}.Read_slice_{}({})\n", varNameArr, varNameByteBuf, goFieldTypeName, varNameLen);
            } else {
                if (len == 0) {
                    ParseUtil.append(body, "{}:=make([]{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
                } else {
                    ParseUtil.append(body, "{}:=[{}]{}{}\n", varNameArr, len, goFieldTypeName);
                }
                ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
                ParseUtil.append(body, "e:={}.Read_{}()\n", varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName, bigEndian));
                if (singleSkip > 0) {
                    ParseUtil.append(body, "e:={}.Skip({})\n", varNameByteBuf, singleSkip);
                }
                String valCode = "e";
                if (!goReadTypeName.equals(goFieldTypeName)) {
                    valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
                }
                valCode = ParseUtil.replaceValExprToCode(anno.singleValExpr(), valCode);
                ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
                ParseUtil.append(body, "}\n");
            }
        }
        ParseUtil.append(body, "{}.{}={}\n", varNameInstance, goFieldName, varNameArr);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_num_array anno = field.getAnnotation(F_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goReadTypeName = goField.goReadTypeName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_num_array> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final NumType singleType = anno.singleType();
        final NumType singleValType = anno.singleValType();
        final int singleSkip = anno.singleSkip();
        final int len = anno.len();
        final String valExpr = anno.singleValExpr();
        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, varNameInstance, goFieldName);
        int singleLen;
        switch (singleType) {
            case uint8, int8 -> {
                singleLen = 1;
            }
            case uint16, int16 -> {
                singleLen = 2;
            }
            case uint32, int32, float32 -> {
                singleLen = 4;
            }
            case uint64, int64, float64 -> {
                singleLen = 8;
            }
            default -> {
                ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                singleLen = 0;
            }
        }
        if (len > 0 && valExpr.isEmpty() && singleSkip == 0) {
            if (singleLen == 1) {
                ParseUtil.append(body, "{}.Write_slice_{}({})\n", varNameByteBuf, goFieldTypeName, GoParseUtil.get_unsafe_array_to_slice_1(varNameArr));
            } else {
                ParseUtil.append(body, "{}.Write_slice_{}(*({}))\n", varNameByteBuf, goFieldTypeName, GoParseUtil.get_unsafe_array_to_slice_2(goFieldTypeName, varNameArr, (singleLen + singleSkip) * len));
            }
        } else {
            if (singleLen == 1 && valExpr.isEmpty()) {
                ParseUtil.append(body, "{}.Write_slice_{}({})\n", varNameByteBuf, goFieldTypeName, varNameArr);
            } else {
                ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
                String valCode = ParseUtil.format("{}[i]", varNameArr);
                if (!valExpr.isEmpty()) {
                    valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
                }

                //原始值不是小数、字段值是小数
                if (!goReadTypeName.equals("float32") && !goReadTypeName.equals("float64")
                        && (goFieldTypeName.equals("float32") || goFieldTypeName.equals("float64"))) {
                    valCode = ParseUtil.format("{}(parse.Round({}))", goReadTypeName, valCode);
                } else {
                    if (!goReadTypeName.equals(goFieldTypeName)) {
                        valCode = ParseUtil.format("{}({})", goReadTypeName, valCode);
                    }
                }


                ParseUtil.append(body, "{}.Write_{}({})\n", varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName, bigEndian), valCode);
                if (singleSkip > 0) {
                    ParseUtil.append(body, "{}.Write_zero({})\n", varNameByteBuf, singleSkip);
                }
                ParseUtil.append(body, "}\n");
            }
        }
    }

}

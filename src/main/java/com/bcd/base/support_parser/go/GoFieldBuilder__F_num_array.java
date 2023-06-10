package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

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
        final int len = anno.len();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldType = field.getType();
        final String goFieldTypeName;
        final String goReadTypeName;
        switch (anno.singleLen()) {
            case 1 -> {
                goReadTypeName = unsigned ? "uint8" : "int8";
            }
            case 2 -> {
                goReadTypeName = unsigned ? "uint16" : "int16";
            }
            case 4 -> {
                goReadTypeName = unsigned ? "uint32" : "int32";
            }
            case 8 -> {
                goReadTypeName = unsigned ? "uint64" : "int64";
            }
            default -> {
                ParseUtil.notSupport_singleLen(field, annoClass);
                goReadTypeName = null;
            }
        }
        if (fieldType == float[].class) {
            goFieldTypeName = "float32";
        } else if (fieldType == double[].class) {
            goFieldTypeName = "float64";
        } else {
            goFieldTypeName = goReadTypeName;
        }
        goField.goFieldTypeName = goFieldTypeName;
        goField.goReadTypeName = goReadTypeName;
        if (len == 0) {
            ParseUtil.append(body, "{} []{}\n", goFieldName, goFieldTypeName);
        } else {
            ParseUtil.append(body, "{} [{}]{}\n", goFieldName, len, goFieldTypeName);
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final String valExpr = anno.valExpr();
        String varNameLen = goFieldName + "_len";
        final int len = anno.len();
        final String varNameArr = goFieldName + "_arr";
        if (len > 0 && unsigned && valExpr.isEmpty() && singleSkip == 0) {
            if (singleLen == 1) {
                ParseUtil.append(body, "{}:={}\n", varNameArr, GoParseUtil.get_unsafe_slice_to_array_1(goFieldTypeName, len));
            } else {
                ParseUtil.append(body, "{}:=(*{})\n", varNameArr, GoParseUtil.get_unsafe_slice_to_array_2(goFieldTypeName, len, len * singleLen));
            }
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            if (singleLen == 1 && unsigned && valExpr.isEmpty() && singleSkip == 0) {
                ParseUtil.append(body, "{}:={}.Read_bytes({})\n", varNameArr, GoFieldBuilder.varNameByteBuf, varNameLen);
            } else {
                if (len == 0) {
                    ParseUtil.append(body, "{}:=make([]{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
                } else {
                    ParseUtil.append(body, "{}:=[{}]{}{}\n", varNameArr, varNameLen, goFieldTypeName);
                }
                ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
                ParseUtil.append(body, "e:={}.Read_{}()\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName, bigEndian));
                if (singleSkip > 0) {
                    ParseUtil.append(body, "e:={}.Skip({})\n", GoFieldBuilder.varNameByteBuf, singleSkip);
                }
                String valCode = "e";
                if (!goReadTypeName.equals(goFieldTypeName)) {
                    valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
                }
                valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), valCode);
                ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
                ParseUtil.append(body, "}\n");
            }
        }
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, varNameArr);
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final int len = anno.len();
        final String valExpr = anno.valExpr();
        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        if (len > 0 && unsigned && valExpr.isEmpty() && singleSkip == 0) {
            if (singleLen == 1) {
                ParseUtil.append(body, "{}.Write_bytes({})\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.get_unsafe_array_to_slice_1(varNameArr));
            } else {
                ParseUtil.append(body, "{}.Write_bytes(*({}))\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.get_unsafe_array_to_slice_2(varNameArr, (singleLen + singleSkip) * len));
            }
        } else {
            if (singleLen == 1 && unsigned && valExpr.isEmpty()) {
                ParseUtil.append(body, "{}.Write_bytes({})\n", GoFieldBuilder.varNameByteBuf, varNameArr);
            } else {
                ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
                String valCode = ParseUtil.format("{}[i]", varNameArr);
                if (!valExpr.isEmpty()) {
                    valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
                }
                if (!goReadTypeName.equals(goFieldTypeName)) {
                    valCode = ParseUtil.format("{}(parse.Round({}))", goReadTypeName, valCode);
                }
                ParseUtil.append(body, "{}.Write_{}({})\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName,bigEndian), valCode);
                if (singleSkip > 0) {
                    ParseUtil.append(body, "{}.Write_zero({})\n", GoFieldBuilder.varNameByteBuf, singleSkip);
                }
                ParseUtil.append(body, "}\n");
            }
        }
    }

}

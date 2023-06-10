package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_float_ieee754_array;
import com.bcd.base.support_parser.anno.FloatType_ieee754;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_float_ieee754_array extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754_array anno = field.getAnnotation(F_float_ieee754_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_float_ieee754_array> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final FloatType_ieee754 type = anno.type();
        final int len = anno.len();
        final String goFieldTypeName;
        switch (type) {
            case Float32 -> {
                goFieldTypeName = "float32";
            }
            case Float64 -> {
                goFieldTypeName = "float64";
            }
            default -> {
                goFieldTypeName = null;
            }
        }
        goField.goFieldTypeName = goFieldTypeName;
        if (len == 0) {
            ParseUtil.append(body, "{} []{}\n", goFieldName, goFieldTypeName);
        } else {
            ParseUtil.append(body, "{} [{}]{}\n", goFieldName, len, goFieldTypeName);
        }
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754_array anno = field.getAnnotation(F_float_ieee754_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_float_ieee754_array> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String valExpr = anno.valExpr();
        final FloatType_ieee754 type = anno.type();
        final int len = anno.len();
        final int singleSkip = anno.singleSkip();
        if (len > 0 && valExpr.isEmpty() && bigEndian && singleSkip == 0) {
            int sliceLen;
            switch (type) {
                case Float32 -> {
                    sliceLen = len * 2;
                }
                case Float64 -> {
                    sliceLen = len * 4;
                }
                default -> {
                    sliceLen = 0;
                }
            }
            ParseUtil.append(body, "{}.{}=*({})\n",
                    GoFieldBuilder.varNameInstance, goFieldName, GoParseUtil.get_unsafe_slice_to_array_2(goFieldTypeName, len, sliceLen));
        } else {
            String varNameLen = goFieldName + "_len";
            if (len == 0) {
                ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            } else {
                ParseUtil.append(body, "{}:={}\n", varNameLen, len);
            }
            final String varNameArr = goFieldName + "_arr";
            ParseUtil.append(body, "{}:=make([]{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
            ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
            ParseUtil.append(body, "e:={}.Read_{}({})\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName, bigEndian);
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.Skip({})\n", GoFieldBuilder.varNameByteBuf, singleSkip);
            }
            String valCode = "e";
            valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);
            ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
            ParseUtil.append(body, "}\n");
            ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, varNameArr);

        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754_array anno = field.getAnnotation(F_float_ieee754_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_float_ieee754_array> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String valExpr = anno.valExpr();
        final int len = anno.len();
        final int singleSkip = anno.singleSkip();
        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        if (len > 0 && valExpr.isEmpty() && bigEndian && singleSkip == 0) {
            ParseUtil.append(body, "{}.Write_bytes(*({}))\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.get_unsafe_array_to_slice_2(varNameArr, len));
        } else {
            ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
            String valCode = ParseUtil.format("{}[i]", varNameArr);
            valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
            ParseUtil.append(body, "{}.Write_{}({},{})\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName, valCode, bigEndian);
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.Write_zero({})\n", GoFieldBuilder.varNameByteBuf, singleSkip);
            }
            ParseUtil.append(body, "}\n");
        }
    }

}

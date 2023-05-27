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
        if (fieldType == float.class) {
            goFieldTypeName = "float32";
        } else if (fieldType == double.class) {
            goFieldTypeName = "float64";
        } else {
            goFieldTypeName = goReadTypeName;
        }
        goField.goFieldTypeName = goFieldTypeName;
        goField.goReadTypeName = goReadTypeName;
        ParseUtil.append(body, "{} []{}\n", goFieldName, goFieldTypeName);
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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final int fieldIndex = context.fieldIndex;
        final String valExpr = anno.valExpr();
        String varNameLen = ParseUtil.format("len{}", fieldIndex);
        final int len = anno.len();
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        final String varNameArr = ParseUtil.format("arr{}", fieldIndex);
        if (singleLen == 1 && unsigned && valExpr.isEmpty()) {
            ParseUtil.append(body, "{},err:={}.Read_bytes({})\n", varNameArr, GoFieldBuilder.varNameByteBuf, varNameLen);
            ParseUtil.append(body, "if err!=nil{\n");
            ParseUtil.append(body, "return nil,err\n");
            ParseUtil.append(body, "}\n");
        } else {
            ParseUtil.append(body, "{}:=make([]{},{})\n", varNameArr, goFieldTypeName, varNameLen);
            ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
            switch (singleLen) {
                case 1 -> {
                    ParseUtil.append(body, "e,err:={}.Read_{}()\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName);
                }
                case 2, 4, 8 -> {
                    ParseUtil.append(body, "e,err:={}.Read_{}({})\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName, bigEndian);
                }
                default -> {
                    ParseUtil.notSupport_len(field, annoClass);
                }
            }
            ParseUtil.append(body, "if err!=nil{\n");
            ParseUtil.append(body, "return nil,err\n");
            ParseUtil.append(body, "}\n");
            String valCode = "e";
            if (!goReadTypeName.equals(goFieldTypeName)) {
                valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
            }
            valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), valCode);
            ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
            ParseUtil.append(body, "}\n");
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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final int fieldIndex = context.fieldIndex;
        final String valExpr = anno.valExpr();

        final String varNameArr = ParseUtil.format("arr{}", fieldIndex);
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        if (singleLen == 1 && unsigned && valExpr.isEmpty()) {
            ParseUtil.append(body, "{}.Write_bytes({})\n", GoFieldBuilder.varNameByteBuf, varNameArr);
        } else {
            ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
            String valCode = ParseUtil.format("{}[i]", varNameArr);
            if (!valExpr.isEmpty()) {
                valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
            }
            if (!goReadTypeName.equals(goFieldTypeName)) {
                valCode = ParseUtil.format("{}(util.Round(float64({})))", goReadTypeName, valCode);
            }
            switch (singleLen) {
                case 1 -> {
                    ParseUtil.append(body, "{}.Write_{}({})\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName, valCode);
                }
                case 2, 4, 8 -> {
                    ParseUtil.append(body, "{}.Write_{}({},{})\n", GoFieldBuilder.varNameByteBuf, goFieldTypeName, valCode, bigEndian);
                }
                default -> {
                    ParseUtil.notSupport_len(field, annoClass);
                }
            }
            ParseUtil.append(body, "}\n");
        }
    }

}

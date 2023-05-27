package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_num extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_num> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldType = field.getType();
        final String goFieldTypeName;
        final String goReadTypeName;
        switch (anno.len()) {
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
                ParseUtil.notSupport_len(field, annoClass);
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
        ParseUtil.append(body, "{} {}\n", goFieldName, goFieldTypeName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String goReadTypeName = goField.goReadTypeName;
        final Class<? extends F_num> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final int fieldIndex = context.fieldIndex;
        final Class<?> fieldType = field.getType();
        final String varNameReadVal = "v" + fieldIndex;
        final String valExpr = anno.valExpr();
        switch (anno.len()) {
            case 1 -> {
                ParseUtil.append(body, "{},err:={}.Read_{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, goReadTypeName);
            }
            case 2, 4, 8 -> {
                ParseUtil.append(body, "{},err:={}.Read_{}({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, goReadTypeName, bigEndian);
            }
            default -> {
                ParseUtil.notSupport_len(field, annoClass);
            }
        }

        ParseUtil.append(body, "if err!=nil{\n");
        ParseUtil.append(body, "return nil,err\n");
        ParseUtil.append(body, "}\n");
        String valCode = varNameReadVal;
        if (!goReadTypeName.equals(goFieldTypeName)) {
            valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
        }
        valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);
        ParseUtil.append(body, "{}.{}={}\n\n", GoFieldBuilder.varNameInstance, goFieldName, valCode);
        if (anno.var() != '0') {
            if (valExpr.isEmpty()) {
                varToGoFieldName.put(anno.var(), varNameReadVal);
            } else {
                varToGoFieldName.put(anno.var(), ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName));
            }
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String goReadTypeName = goField.goReadTypeName;
        final Class<? extends F_num> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final boolean unsigned = anno.unsigned();
        final String valExpr = anno.valExpr();
        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        if (!valExpr.isEmpty()) {
            valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        }
        if (!goReadTypeName.equals(goFieldTypeName)) {
            valCode = ParseUtil.format("{}(util.Round(float64({})))", goReadTypeName, valCode);
        }
        switch (anno.len()) {
            case 1 -> {
                ParseUtil.append(body, "{}.Write_{}({})\n", GoFieldBuilder.varNameByteBuf, goReadTypeName, valCode);
            }
            case 2, 4, 8 -> {
                ParseUtil.append(body, "{}.Write_{}({},{})\n", GoFieldBuilder.varNameByteBuf, goReadTypeName, valCode, bigEndian);
            }
            default -> {
                ParseUtil.notSupport_len(field, annoClass);
                valCode = "";
            }
        }

    }

}

package com.bcd.root.support_parser.go;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.util.ParseUtil;
import com.bcd.root.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_num extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String jsonExt = goField.jsonExt;
        final Class<? extends F_num> annoClass = anno.getClass();
        final NumType type = anno.type();
        final NumType valType = anno.valType();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final Class<?> fieldType = field.getType();
        final String goFieldTypeName;
        final String goReadTypeName;

        switch (type) {
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

        switch (valType) {
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
        ParseUtil.append(body, "{} {} {}\n", goFieldName, goFieldTypeName, jsonExt);
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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final Class<?> fieldType = field.getType();
        final String varNameReadVal = goFieldName + "_v";
        final String valExpr = anno.valExpr();
        ParseUtil.append(body, "{}:={}.Read_{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName, bigEndian));

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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String valExpr = anno.valExpr();
        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
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

        ParseUtil.append(body, "{}.Write_{}({})\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goReadTypeName, bigEndian), valCode);

        if (anno.var() != '0') {
            varToGoFieldName.put(anno.var(), ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName));
        }

    }

}

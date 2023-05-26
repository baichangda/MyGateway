package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_float_ieee754;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_ieee754 extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float_ieee754> annoClass = F_float_ieee754.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldType = fieldTypeClass.getName();

        switch (fieldType) {
            case "float", "double" -> {
            }
            default -> {
                ParseUtil.notSupport_fieldType(field, annoClass);
            }
        }


        final F_float_ieee754 anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;


        final String funcName;
        switch (anno.type()) {
            case Float32 -> {
                funcName = bigEndian ? "readFloat" : "readFloatLE";
            }
            case Float64 -> {
                funcName = bigEndian ? "readDouble" : "readDoubleLE";
            }
            default -> {
                ParseUtil.notSupport_type(field, annoClass);
                funcName = null;
            }
        }

        final String valCode = ParseUtil.format("{}.{}()", varNameByteBuf, funcName);

        if (anno.valPrecision() == -1) {
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), ParseUtil.replaceValExprToCode(anno.valExpr(), ParseUtil.format("(({}){})", fieldType, valCode)));
        } else {
            ParseUtil.append(body, "{}.{}=({}){}.format((double)({}),{});\n", varNameInstance, field.getName(), fieldType, ParseUtil.class.getName(), ParseUtil.replaceValExprToCode(anno.valExpr(), valCode), anno.valPrecision());
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float_ieee754> annoClass = F_float_ieee754.class;
        final Field field = context.field;
        final F_float_ieee754 anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String funcName;
        final String funcParamTypeName;

        final String valCode;
        if (anno.valExpr().isEmpty()) {
            valCode = varNameInstance + "." + fieldName;
        } else {
            final String reverseExpr = RpnUtil.reverseExpr(anno.valExpr());
            valCode = ParseUtil.replaceValExprToCode(reverseExpr, varNameInstance + "." + fieldName);
        }

        switch (anno.type()) {
            case Float32 -> {
                funcName = bigEndian ? "writeFloat" : "writeFloatLE";
                funcParamTypeName = "float";
            }
            case Float64 -> {
                funcName = bigEndian ? "writeDouble" : "writeDoubleLE";
                funcParamTypeName = "double";
            }
            default -> {
                ParseUtil.notSupport_type(field, annoClass);
                funcName = null;
                funcParamTypeName = null;
            }
        }
        ParseUtil.append(body, "{}.{}(({})({}));\n", varNameByteBuf, funcName, funcParamTypeName, valCode);
    }
}

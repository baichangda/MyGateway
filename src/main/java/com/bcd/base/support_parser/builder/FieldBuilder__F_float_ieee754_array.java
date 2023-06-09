package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_float_ieee754_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_ieee754_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float_ieee754_array> annoClass = F_float_ieee754_array.class;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final String fieldName = field.getName();
        final String arrayElementType = fieldType.componentType().getName();

        final F_float_ieee754_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);

        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }


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
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, lenRes);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        final String valCode = ParseUtil.format("{}.{}()", varNameByteBuf, funcName);
        ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementType, ParseUtil.replaceValExprToCode(anno.valExpr(), valCode));
        ParseUtil.append(body, "};\n");
        ParseUtil.append(body, "{}.{}={};\n", varNameInstance, fieldName, arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float_ieee754_array> annoClass = F_float_ieee754_array.class;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_float_ieee754_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String valCode = FieldBuilder.varNameInstance + "." + fieldName;

        ParseUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);

        final String arrayElementType = fieldType.componentType().getName();

        switch (arrayElementType) {
            case "float", "double" -> {
            }
            default -> {
                ParseUtil.notSupport_fieldType(field, annoClass);
            }
        }

        final String funcName;
        final String funcParamTypeName;
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

        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        ParseUtil.append(body, "final {}[] {}={};\n", arrayElementType, arrVarName, valCode);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);

        if (anno.valExpr().isEmpty()) {
            ParseUtil.append(body, "{}.{}(({})({}[i]));\n", varNameByteBuf, funcName, funcParamTypeName, arrVarName);
        } else {
            ParseUtil.append(body, "{}.{}(({})({}));\n", varNameByteBuf, funcName, funcParamTypeName, ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), arrVarName + "[i]"));
        }

        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "}\n");
    }
}

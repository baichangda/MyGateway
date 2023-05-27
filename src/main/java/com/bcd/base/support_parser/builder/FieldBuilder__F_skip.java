package com.bcd.base.support_parser.builder;


import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String lenValCode;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenValCode = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenValCode = anno.len() + "";
        }
        switch (anno.mode()) {
            case Skip -> {
                ParseUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, lenValCode);
            }
            case ReservedFromStart -> {
                final String skipVarName = varNameField + "_skip";
                ParseUtil.append(body, "final int {}={}-{}.readerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(body, "if({}>0){\n", skipVarName);
                ParseUtil.append(body, "{}.skipBytes({});\n", FieldBuilder.varNameByteBuf, skipVarName);
                ParseUtil.append(body, "}\n");
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_skip anno = field.getAnnotation(F_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String lenValCode;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), fieldName, F_skip.class.getName());
            } else {
                lenValCode = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenValCode = anno.len() + "";
        }
        switch (anno.mode()) {
            case Skip -> {
                ParseUtil.append(body, "{}.writeBytes(new byte[{}]);\n", FieldBuilder.varNameByteBuf, lenValCode);
            }
            case ReservedFromStart -> {
                final String skipVarName = varNameField + "_skip";
                ParseUtil.append(body, "final int {}={}-{}.writerIndex()+{};\n", skipVarName, lenValCode, FieldBuilder.varNameByteBuf, FieldBuilder.varNameStartIndex);
                ParseUtil.append(body, "if({}>0){\n", skipVarName);
                ParseUtil.append(body, "{}.writeZero({});\n", FieldBuilder.varNameByteBuf, skipVarName);
                ParseUtil.append(body, "}\n");
            }
        }

    }
}

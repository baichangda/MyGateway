package com.bcd.share.support_parser.builder;


import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class FieldBuilder__F_string extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_string anno = field.getAnnotation(F_string.class);
        final Class<? extends F_string> annoClass = anno.getClass();
        if (fieldType != String.class) {
            ParseUtil.notSupport_type(context.clazz, field, annoClass);
        }
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string.class.getName());
            } else {
                lenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        final String varNameField = ParseUtil.getFieldVarName(context);
        final Charset charset = Charset.forName(anno.charset());
        final String charsetClassName = Charset.class.getName();
        final String charsetVarName = ParseUtil.defineClassVar(context, Charset.class, "{}.forName(\"{}\")", charsetClassName, charset.name());
        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}={}.readCharSequence({},{}).toString();\n", varNameInstance, field.getName(), varNameByteBuf, lenRes, charsetVarName);
            }
            case lowAddressAppend -> {
                final String lenVarName = varNameField + "_len";
                final String arrVarName = varNameField + "_arr";
                final String discardLenVarName = varNameField + "_discardLen";
                ParseUtil.append(body, "final int {}={};\n", lenVarName, lenRes);
                ParseUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, lenVarName);
                ParseUtil.append(body, "{}.readBytes({});\n", varNameByteBuf, arrVarName);
                ParseUtil.append(body, "int {}=0;\n", discardLenVarName);
                ParseUtil.append(body, "for(int i=0;i<{};i++){\n", lenVarName);
                ParseUtil.append(body, "if({}[i]==0){\n", arrVarName);
                ParseUtil.append(body, "{}++;\n", discardLenVarName);
                ParseUtil.append(body, "}else{\n");
                ParseUtil.append(body, "break;\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=new String({},{},{}-{},{});\n", varNameInstance, field.getName(), arrVarName, discardLenVarName, lenVarName, discardLenVarName, charsetVarName);
            }
            case highAddressAppend -> {
                final String lenVarName = varNameField + "_len";
                final String arrVarName = varNameField + "_arr";
                final String discardLenVarName = varNameField + "_discardLen";
                ParseUtil.append(body, "final int {}={};\n", lenVarName, lenRes);
                ParseUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, lenVarName);
                ParseUtil.append(body, "{}.readBytes({});\n", varNameByteBuf, arrVarName);
                ParseUtil.append(body, "int {}=0;\n", discardLenVarName);
                ParseUtil.append(body, "for(int i={}-1;i>=0;i--){\n", lenVarName);
                ParseUtil.append(body, "if({}[i]==0){\n", arrVarName);
                ParseUtil.append(body, "{}++;\n", discardLenVarName);
                ParseUtil.append(body, "}else{\n");
                ParseUtil.append(body, "break;\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=new String({},0,{}-{},{});\n", varNameInstance, field.getName(), arrVarName, lenVarName, discardLenVarName, charsetVarName);
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameFieldVal = varNameField + "_val";

        ParseUtil.append(body, "final String {};\n", varNameFieldVal);
        ParseUtil.append(body, "if({}==null){\n", valCode);
        ParseUtil.append(body, "{}=\"\";\n", varNameFieldVal);
        ParseUtil.append(body, "}else{\n", valCode);
        ParseUtil.append(body, "{}={};\n", varNameFieldVal, valCode);
        ParseUtil.append(body, "}\n", valCode);

        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string.class.getName());
            } else {
                lenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }


        String arrVarName = varNameField + "_arr";
        String arrLeaveVarName = varNameField + "_leave";

        final Charset charset = Charset.forName(anno.charset());
        final String charsetClassName = Charset.class.getName();
        final String charsetVarName = ParseUtil.defineClassVar(context, Charset.class, "{}.forName(\"{}\")", charsetClassName, charset.name());

        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.writeBytes({}.getBytes({}));\n", varNameByteBuf, varNameFieldVal, charsetVarName);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "final byte[] {}={}.getBytes({});\n", arrVarName, varNameFieldVal, charsetVarName);
                ParseUtil.append(body, "final int {}={}-{}.length;\n", arrLeaveVarName, lenRes, arrVarName);
                ParseUtil.append(body, "if({}>0){\n", arrLeaveVarName);
                ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, arrLeaveVarName);
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.writeBytes({});\n", varNameByteBuf, arrVarName);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "final byte[] {}={}.getBytes({});\n", arrVarName, varNameFieldVal, charsetVarName);
                ParseUtil.append(body, "{}.writeBytes({});\n", varNameByteBuf, arrVarName);
                ParseUtil.append(body, "final int {}={}-{}.length;\n", arrLeaveVarName, lenRes, arrVarName);
                ParseUtil.append(body, "if({}>0){\n", arrLeaveVarName);
                ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, arrLeaveVarName);
                ParseUtil.append(body, "}\n");
            }
        }
    }

}

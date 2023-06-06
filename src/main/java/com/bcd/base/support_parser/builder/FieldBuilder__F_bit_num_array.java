package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_bit_num_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_num_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final Class<?> arrayElementType = fieldTypeClass.componentType();
        final String arrayElementTypeName = arrayElementType.getName();
        final Class<F_bit_num_array> annoClass = F_bit_num_array.class;
        final F_bit_num_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
        switch (arrayElementTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
            }
            default -> {
                if (arrayElementType.isEnum()) {
                } else {
                    ParseUtil.notSupport_fieldType(field, annoClass);
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            arrLenRes = anno.len() + "";
        }


        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final String valExpr = anno.valExpr();
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final String varNameArrayElement = varNameField + "_arrEle";
        final String varNameBitBuf = context.getVarNameBitBuf_reader();

        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        ParseUtil.append(body, "final {} {}=({}){}.read({},{},{});\n", arrayElementTypeName, varNameArrayElement, arrayElementTypeName, varNameBitBuf, singleLen, bigEndian, unsigned);
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, ParseUtil.replaceValExprToCode(valExpr, varNameArrayElement));
        ParseUtil.append(body, "}\n");
        if (context.bitEndWhenBitField_process) {
            ParseUtil.append(body, "{}.finish();\n", context.varNameBitBuf);
        }
        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_bit_num_array> annoClass = F_bit_num_array.class;
        final F_bit_num_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final Class<?> arrElementType = fieldTypeClass.componentType();
        final boolean isFloat = arrElementType == float.class || arrElementType == double.class;
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();

        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameBitBuf = context.getVarNameBitBuf_writer();
        ParseUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
        final String varNameFieldArr = varNameField + "_arr";
        ParseUtil.append(body, "final {}[] {}={};\n", arrElementType, varNameFieldArr, valCode);
        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
        if (anno.valExpr().isEmpty()) {
            ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, varNameFieldArr + "[i]", singleLen, bigEndian, unsigned);
        } else {
            if (isFloat) {
                ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"), singleLen, bigEndian, unsigned);
            } else {
                ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"), singleLen, bigEndian, unsigned);
            }
        }
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        ParseUtil.append(body, "}\n");

        if (context.bitEndWhenBitField_deProcess) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
        ParseUtil.append(body, "}\n");

    }
}

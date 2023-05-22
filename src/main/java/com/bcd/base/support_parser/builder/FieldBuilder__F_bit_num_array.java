package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_bit_num_array;
import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
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
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
        switch (arrayElementTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
            }
            default -> {
                if (arrayElementType.isEnum()) {
                } else {
                    JavassistUtil.notSupport_fieldType(field, annoClass);
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                arrLenRes = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            arrLenRes = anno.len() + "";
        }


        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final String valExpr = anno.valExpr();
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final String varNameArrayElement = varNameField + "_arrEle";
        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

        JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        JavassistUtil.append(body, "final {} {}=({}){}.read({},{},{});\n", arrayElementTypeName, varNameArrayElement, arrayElementTypeName, varNameBitBuf, singleLen, bigEndian, unsigned);
        if (singleSkip > 0) {
            JavassistUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        JavassistUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
        JavassistUtil.append(body, "}\n");

        if (context.bitEndWhenBitField_process) {
            JavassistUtil.append(body, "{}.finish();\n", context.varNameBitBuf);
        }
        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_bit_num_array> annoClass = F_bit_num_array.class;
        final F_bit_num_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);
        JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
        final String varNameFieldArr = varNameField + "_arr";
        JavassistUtil.append(body, "final {}[] {}={};\n", fieldTypeClass.componentType(), varNameFieldArr, valCode);
        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
        if (anno.valExpr().isEmpty()) {
            JavassistUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, varNameFieldArr + "[i]", singleLen, bigEndian, unsigned);
        } else {
            JavassistUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"), singleLen, bigEndian, unsigned);
        }
        if (singleSkip > 0) {
            JavassistUtil.append(body, "{}.skip({});\n", varNameBitBuf, singleSkip);
        }
        JavassistUtil.append(body, "}\n");

        if (context.bitEndWhenBitField_deProcess) {
            JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
        JavassistUtil.append(body, "}\n");

    }
}

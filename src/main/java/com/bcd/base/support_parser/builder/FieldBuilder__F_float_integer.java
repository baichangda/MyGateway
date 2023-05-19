package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_float_integer;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_integer extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_float_integer> annoClass = F_float_integer.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldType;
        if (float.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "float";
        } else if (double.class.isAssignableFrom(fieldTypeClass)) {
            fieldType = "double";
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            fieldType = "";
        }
        final F_float_integer anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
            } else {
                final int bit = anno.bit();
                if (bit < 1 || bit > 64) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] bit[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), bit);
                }
                String varNameBitBuf = context.varNameBitBuf;
                if (varNameBitBuf == null) {
                    varNameBitBuf = varNameField + "_bitBuf";
                    final String bitBufClassName = BitBuf_reader.class.getName();
                    JavassistUtil.append(body, "final {} {}={}.newBitBuf({});\n", bitBufClassName, varNameBitBuf, bitBufClassName, FieldBuilder.varNameByteBuf);
                    context.varNameBitBuf=varNameBitBuf;
                }
                JavassistUtil.append(body, "final long {}={}.read({});\n", varNameField, varNameBitBuf, bit);
                if (context.bitEndWhenBitField_process) {
                    JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
                }
            }
        } else {
            switch (anno.len()) {
                case 1: {
                    final String funcName = anno.unsigned() ? "readUnsignedByte" : "readByte";
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                case 2: {
                    final String funcName;
                    if (bigEndian) {
                        funcName = anno.unsigned() ? "readUnsignedShort" : "readShort";
                    } else {
                        funcName = anno.unsigned() ? "readUnsignedShortLE" : "readShortLE";
                    }
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                case 4: {
                    final String funcName;
                    if (bigEndian) {
                        funcName = anno.unsigned() ? "readUnsignedInt" : "readInt";
                    } else {
                        funcName = anno.unsigned() ? "readUnsignedIntLE" : "readIntLE";
                    }
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                case 8: {
                    final String funcName = bigEndian ? "readLong" : "readLongLE";
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", fieldType, varNameField, fieldType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                default: {
                    JavassistUtil.notSupport_len(field, annoClass);
                    break;
                }
            }
        }
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), JavassistUtil.format("(({}){})",fieldType,varNameField)));
        } else {
            JavassistUtil.append(body, "{}.{}=({}){}.format((double){},{});\n", varNameInstance, field.getName(), fieldType, JavassistUtil.class.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField), anno.valPrecision());
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_float_integer> annoClass = F_float_integer.class;
        final Field field = context.field;
        final F_float_integer anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String valCode;
        if (anno.valExpr().isEmpty()) {
            valCode = varNameInstance + "." + fieldName;
        } else {
            final String reverseExpr = RpnUtil.reverseExpr(anno.valExpr());
            valCode = JavassistUtil.replaceValExprToCode(reverseExpr, varNameInstance + "." + fieldName);
        }
        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), field.getName(), annoClass.getName());
            } else {
                final int bit = anno.bit();
                if (bit < 1 || bit > 64) {
                    throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] bit[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), bit);
                }
                String varNameBitBuf = context.varNameBitBuf;
                if (varNameBitBuf == null) {
                    varNameBitBuf = varNameField + "_bitBuf";
                    final String bitBufClassName = BitBuf_writer.class.getName();
                    JavassistUtil.append(body, "final {} {}={}.newBitBuf({});\n", bitBufClassName, varNameBitBuf, bitBufClassName, FieldBuilder.varNameByteBuf);
                    context.varNameBitBuf=varNameBitBuf;
                }
                JavassistUtil.append(body, "{}.write((long)({}),{});\n", varNameBitBuf, valCode, bit);
                if (context.bitEndWhenBitField_deProcess) {
                    JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
                }
            }
        } else {
            switch (anno.len()) {
                case 1 -> {
                    JavassistUtil.append(body, "{}.writeByte((int)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                }
                case 2 -> {
                    final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                    JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
                }
                case 4 -> {
                    final String funcName = bigEndian ? "writeInt" : "writeIntLE";
                    JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
                }
                case 8 -> {
                    final String funcName = bigEndian ? "writeLong" : "writeLongLE";
                    JavassistUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
                }
                default -> {
                    JavassistUtil.notSupport_len(field, annoClass);
                }
            }
        }
    }
}

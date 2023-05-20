package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_integer extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_integer> annoClass = F_integer.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_integer anno = context.field.getAnnotation(annoClass);
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

                if (Parser.logCollector_parse == null) {
                    JavassistUtil.append(body, "final long {}={}.read({});\n", varNameField, varNameBitBuf, bit);
                } else {
                    context.varNameBitLogRes = varNameField + "_bitLogRes";
                    JavassistUtil.append(body, "final {} {}={}.read_log({});\n", BitBuf_reader.LogRes.class.getName(), context.varNameBitLogRes, varNameBitBuf, bit);
                    JavassistUtil.append(body, "final long {}={}.val;\n", varNameField, context.varNameBitLogRes);
                }

                if (context.bitEndWhenBitField_process) {
                    JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
                }
                if (fieldTypeClass.isEnum()) {
                    JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeClass.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                } else {
                    JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                }
            }
        } else {
            if (byte.class.isAssignableFrom(fieldTypeClass)) {
                if (anno.len() == 1) {
                    JavassistUtil.append(body, "final byte {}={}.readByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                } else {
                    JavassistUtil.notSupport_len(field, annoClass);
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (short.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 1 -> {
                        JavassistUtil.append(body, "final short {}={}.readUnsignedByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                    }
                    case 2 -> {
                        final String funcName = bigEndian ? "readShort" : "readShortLE";
                        JavassistUtil.append(body, "final short {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (int.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 2 -> {
                        final String funcName = bigEndian ? "readUnsignedShort" : "readUnsignedShortLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    case 4 -> {
                        final String funcName = bigEndian ? "readInt" : "readIntLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (long.class.isAssignableFrom(fieldTypeClass)) {
                switch (anno.len()) {
                    case 4 -> {
                        final String funcName = bigEndian ? "readUnsignedInt" : "readUnsignedIntLE";
                        JavassistUtil.append(body, "final long {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    case 8 -> {
                        final String funcName = bigEndian ? "readLong" : "readLongLE";
                        JavassistUtil.append(body, "final long {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else if (fieldTypeClass.isEnum()) {
                switch (anno.len()) {
                    case 1 -> {
                        JavassistUtil.append(body, "final short {}={}.readUnsignedByte();\n", varNameField, FieldBuilder.varNameByteBuf);
                    }
                    case 2 -> {
                        final String funcName = bigEndian ? "readUnsignedShort" : "readUnsignedShortLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    case 4 -> {
                        final String funcName = bigEndian ? "readInt" : "readIntLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameField, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_len(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
            } else {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
        }


        final char var = anno.var();
        if (var != '0') {
            context.varToFieldName.put(var, varNameField);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_integer> annoClass = F_integer.class;
        final Field field = context.field;
        final F_integer anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameField = JavassistUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final char var = anno.var();
        String valCode;
        //先判断是否是枚举类型、如果是枚举转换为int
        if (fieldType.isEnum()) {
            valCode = JavassistUtil.format("{}.toInteger()", varNameInstance + "." + fieldName);
        } else {
            valCode = varNameInstance + "." + fieldName;
        }

        //判断是否用到变量中、如果用到了、需要定义变量
        if (var != '0') {
            JavassistUtil.append(body, "final {} {}={};\n", fieldType.getName(), varNameField, valCode);
            context.varToFieldName.put(var, varNameField);
        }

        //最后判断是否用了值表达式、如果用了、进行表达式处理
        if (!anno.valExpr().isEmpty()) {
            valCode = JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
        }

        if (anno.len() == 0) {
            if (anno.bit() == 0) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] must have len or bit", field.getDeclaringClass().getName(), fieldName, annoClass.getName());
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

                if (Parser.logCollector_parse == null) {
                    JavassistUtil.append(body, "{}.write((long)({}),{});\n", varNameBitBuf, valCode, bit);
                } else {
                    context.varNameBitLogRes = varNameField + "_bitLogRes";
                    JavassistUtil.append(body, "final {} {}={}.write_log((long)({}),{});\n", BitBuf_writer.LogRes.class.getName(), context.varNameBitLogRes, varNameBitBuf, valCode, bit);
                }

                if (context.bitEndWhenBitField_deProcess) {
                    JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
                }

            }
        } else {
            switch (anno.len()) {
                case 1 -> {
                    JavassistUtil.append(body, "{}.writeByte((byte)({}));\n", FieldBuilder.varNameByteBuf, valCode);
                }
                case 2 -> {
                    final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                    JavassistUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
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

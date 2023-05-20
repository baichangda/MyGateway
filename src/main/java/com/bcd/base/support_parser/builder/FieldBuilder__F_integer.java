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
        final String sourceValTypeName;
        final String fieldTypeName = fieldTypeClass.getName();

        switch (fieldTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
                sourceValTypeName = fieldTypeName;
            }
            default -> {
                if (fieldTypeClass.isEnum()) {
                    sourceValTypeName = "int";
                } else {
                    JavassistUtil.notSupport_fieldType(field, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final F_integer anno = context.field.getAnnotation(annoClass);
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

                final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

                if (Parser.logCollector_parse == null) {
                    JavassistUtil.append(body, "final long {}={}.read({},{});\n", varNameField, varNameBitBuf, bit, anno.bitUnsigned());
                } else {
                    context.varNameBitLog = varNameField + "_bitLog";
                    JavassistUtil.append(body, "final {} {}={}.read_log({},{});\n", BitBuf_reader.ReadLog.class.getName(), context.varNameBitLog, varNameBitBuf, bit, anno.bitUnsigned());
                    JavassistUtil.append(body, "final long {}={}.val;\n", varNameField, context.varNameBitLog);
                }

                if (context.bitEndWhenBitField_process) {
                    JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
                }
                if (fieldTypeClass.isEnum()) {
                    JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                } else {
                    JavassistUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
                }
            }
        } else {
            final boolean unsigned = anno.unsigned();
            final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
            String funcName;
            switch (anno.len()) {
                case 1 -> {
                    funcName = unsigned ? "readUnsignedByte" : "readByte";
                }
                case 2 -> {
                    funcName = unsigned ? "readUnsignedShort" : "readShort";
                    funcName += (bigEndian ? "" : "LE");
                }
                case 4 -> {
                    funcName = unsigned ? "readUnsignedInt" : "readInt";
                    funcName += (bigEndian ? "" : "LE");
                }
                case 8 -> {
                    funcName = bigEndian ? "readLong" : "readLongLE";
                }
                default -> {
                    JavassistUtil.notSupport_len(field, annoClass);
                    funcName = null;
                }
            }
            //读取原始数据
            JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", sourceValTypeName, varNameField, sourceValTypeName, FieldBuilder.varNameByteBuf, funcName);
            //表达式运算
            final String valCode = JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField);
            if (fieldTypeClass.isEnum()) {
                JavassistUtil.append(body, "{}.{}={}.fromInteger((int)({}));\n", varNameInstance, field.getName(), fieldTypeName, valCode);
            } else {
                JavassistUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), valCode);
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

                final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

                if (Parser.logCollector_deParse == null) {
                    JavassistUtil.append(body, "{}.write((long)({}),{},{});\n", varNameBitBuf, valCode, bit, anno.bitUnsigned());
                } else {
                    context.varNameBitLog = varNameField + "_bitLog";
                    JavassistUtil.append(body, "final {} {}={}.write_log((long)({}),{},{});\n", BitBuf_writer.WriteLog.class.getName(), context.varNameBitLog, varNameBitBuf, valCode, bit, anno.bitUnsigned());
                }

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

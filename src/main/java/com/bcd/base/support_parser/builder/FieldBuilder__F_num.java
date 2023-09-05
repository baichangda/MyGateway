package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_num extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_num> annoClass = F_num.class;
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
                    ParseUtil.notSupport_fieldType(context.clazz, field, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final F_num anno = context.field.getAnnotation(annoClass);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = ParseUtil.getFieldVarName(context);

        ParseUtil.check_numType(context.clazz, field, anno);

        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final NumType type = anno.type();
        String funcName;
        switch (type) {
            case uint8 -> {
                if (sourceValTypeName.equals("byte")) {
                    funcName = "readByte";
                } else {
                    funcName = "readUnsignedByte";
                }
            }
            case int8 -> {
                funcName = "readByte";
            }
            case uint16 -> {
                if (sourceValTypeName.equals("short")) {
                    funcName = "readShort";
                } else {
                    funcName = "readUnsignedShort";
                }
            }
            case int16 -> {
                funcName = "readShort";
            }
            case uint32 -> {
                if (sourceValTypeName.equals("int")) {
                    funcName = "readInt";
                } else {
                    funcName = "readUnsignedInt";
                }
            }
            case int32 -> {
                funcName = "readInt";
            }
            case uint64, int64 -> {
                funcName = "readLong";
            }
            case float32 -> {
                funcName = "readFloat";
            }
            case float64 -> {
                funcName = "readDouble";
            }
            default -> {
                ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                funcName = null;
            }
        }
        if (!bigEndian && type != NumType.uint8 && type != NumType.int8) {
            funcName += "LE";
        }
        //读取原始数据
        ParseUtil.append(body, "final {} {}=({}){}.{}();\n", sourceValTypeName, varNameField, sourceValTypeName, FieldBuilder.varNameByteBuf, funcName);
        //表达式运算
        final String valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField);
        if (fieldTypeClass.isEnum()) {
            ParseUtil.append(body, "{}.{}={}.fromInteger((int)({}));\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        } else {
            ParseUtil.append(body, "{}.{}=({}){};\n", varNameInstance, field.getName(), sourceValTypeName, valCode);
        }

        final char var = anno.var();
        if (var != '0') {
            context.varToFieldName.put(var, varNameField);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_num> annoClass = F_num.class;
        final Field field = context.field;
        final F_num anno = context.field.getAnnotation(annoClass);

        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final boolean isFloat = fieldType == float.class || fieldType == double.class;
        final char var = anno.var();
        String valCode;
        //先判断是否是枚举类型、如果是枚举转换为int
        if (fieldType.isEnum()) {
            valCode = ParseUtil.format("{}.toInteger()", varNameInstance + "." + fieldName);
        } else {
            valCode = varNameInstance + "." + fieldName;
        }

        //判断是否用到变量中、如果用到了、需要定义变量
        if (var != '0') {
            ParseUtil.append(body, "final {} {}={};\n", fieldType.getName(), varNameField, valCode);
            context.varToFieldName.put(var, varNameField);
        }

        //最后判断是否用了值表达式、如果用了、进行表达式处理
        if (!anno.valExpr().isEmpty()) {
            if (isFloat) {
                valCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            } else {
                valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            }
        }

        final NumType type = anno.type();
        String funcName;
        if (!bigEndian && type != NumType.uint8 && type != NumType.int8) {
            funcName = "LE";
        } else {
            funcName = "";
        }
        switch (type) {
            case uint8, int8 -> {
                funcName = "writeByte" + funcName;
                ParseUtil.append(body, "{}.{}((byte)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case uint16, int16 -> {
                funcName = "writeShort" + funcName;
                ParseUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case uint32, int32 -> {
                funcName = "writeInt" + funcName;
                ParseUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case uint64, int64 -> {
                funcName = "writeLong" + funcName;
                ParseUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case float32 -> {
                funcName = "writeFloat" + funcName;
                ParseUtil.append(body, "{}.{}((float)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case float64 -> {
                funcName = "writeDouble" + funcName;
                ParseUtil.append(body, "{}.{}((double)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            default -> {
                ParseUtil.notSupport_numType(context.clazz, field, annoClass);
            }
        }
    }
}

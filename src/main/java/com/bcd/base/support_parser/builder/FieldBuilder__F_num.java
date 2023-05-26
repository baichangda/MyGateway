package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_num;
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
                    ParseUtil.notSupport_fieldType(field, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final F_num anno = context.field.getAnnotation(annoClass);
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = ParseUtil.getFieldVarName(context);

        final boolean unsigned = anno.unsigned();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
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
                ParseUtil.notSupport_len(field, annoClass);
                funcName = null;
            }
        }
        //读取原始数据
        ParseUtil.append(body, "final {} {}=({}){}.{}();\n", sourceValTypeName, varNameField, sourceValTypeName, FieldBuilder.varNameByteBuf, funcName);
        //表达式运算
        final String valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField);
        if (fieldTypeClass.isEnum()) {
            ParseUtil.append(body, "{}.{}={}.fromInteger((int)({}));\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        } else {
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), valCode);
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
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
            if(isFloat){
                valCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            }else{
                valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), valCode);
            }
        }


        switch (anno.len()) {
            case 1 -> {
                ParseUtil.append(body, "{}.writeByte((int)({}));\n", FieldBuilder.varNameByteBuf, valCode);
            }
            case 2 -> {
                final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                ParseUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case 4 -> {
                final String funcName = bigEndian ? "writeInt" : "writeIntLE";
                ParseUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            case 8 -> {
                final String funcName = bigEndian ? "writeLong" : "writeLongLE";
                ParseUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, valCode);
            }
            default -> {
                ParseUtil.notSupport_len(field, annoClass);
            }
        }

    }
}

package com.bcd.root.support_parser.builder;

import com.bcd.root.exception.BaseRuntimeException;
import com.bcd.root.support_parser.anno.F_bit_num;
import com.bcd.root.support_parser.util.ParseUtil;
import com.bcd.root.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_num extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_bit_num> annoClass = F_bit_num.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_bit_num anno = field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.bitOrder);
        final boolean unsigned = anno.unsigned();

        ParseUtil.check_numType(context.clazz, field, anno);

        final String sourceValTypeName;
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

        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = ParseUtil.getFieldVarName(context);

        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        final String varNameBitBuf = context.getVarNameBitBuf_reader();

        ParseUtil.append(body, "final {} {}=({}){}.read({},{},{});\n", sourceValTypeName, varNameField, sourceValTypeName, varNameBitBuf, len, bigEndian, unsigned);
        if (context.bitEndWhenBitField_process) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

        String valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField);

        if (fieldTypeClass.isEnum()) {
            ParseUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        } else {
            ParseUtil.append(body, "{}.{}=({})({});\n", varNameInstance, field.getName(), fieldTypeName, valCode);
        }

        final char var = anno.var();
        if (var != '0') {
            context.varToFieldName.put(var, varNameField);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Class<F_bit_num> annoClass = F_bit_num.class;
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(annoClass);
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.bitOrder);
        final boolean unsigned = anno.unsigned();
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

        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        final String varNameBitBuf = context.getVarNameBitBuf_writer();

        ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, valCode, len, bigEndian, unsigned);
        if (context.bitEndWhenBitField_deProcess) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

    }
}

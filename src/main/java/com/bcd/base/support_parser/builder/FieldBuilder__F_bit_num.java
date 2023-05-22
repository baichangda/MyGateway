package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_num extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Class<F_bit_num> annoClass = F_bit_num.class;
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String fieldTypeName = fieldTypeClass.getName();
        final F_bit_num anno = field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();

        switch (fieldTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
            }
            default -> {
                if (fieldTypeClass.isEnum()) {
                } else {
                    JavassistUtil.notSupport_fieldType(field, annoClass);
                }
            }
        }

        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String varNameField = JavassistUtil.getFieldVarName(context);

        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

        if (Parser.logCollector_parse == null) {
            JavassistUtil.append(body, "final long {}={}.read({},{},{});\n", varNameField, varNameBitBuf, len, bigEndian, unsigned);
        } else {
            context.varNameBitLog = varNameField + "_bitLog";
            JavassistUtil.append(body, "final {} {}={}.read_log({},{},{});\n", BitBuf_reader.ReadLog.class.getName(), context.varNameBitLog, varNameBitBuf, len, bigEndian, unsigned);
            JavassistUtil.append(body, "final long {}={}.val3;\n", varNameField, context.varNameBitLog);
        }

        if (context.bitEndWhenBitField_process) {
            JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
        if (fieldTypeClass.isEnum()) {
            JavassistUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
        } else {
            JavassistUtil.append(body, "{}.{}=({})({});\n", varNameInstance, field.getName(), fieldTypeName, JavassistUtil.replaceValExprToCode(anno.valExpr(), varNameField));
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
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
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

        final int len = anno.len();
        if (len < 1 || len > 64) {
            throw BaseRuntimeException.getException("class[{}] field[{}] anno[{}] len[{}] must in range [1,64]", field.getDeclaringClass().getName(), field.getName(), annoClass.getName(), len);
        }

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

        if (Parser.logCollector_deParse == null) {
            JavassistUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, valCode, len, bigEndian, unsigned);
        } else {
            context.varNameBitLog = varNameField + "_bitLog";
            JavassistUtil.append(body, "final {} {}={}.write_log((long)({}),{},{},{});\n", BitBuf_writer.WriteLog.class.getName(), context.varNameBitLog, varNameBitBuf, valCode, len, bigEndian, unsigned);
        }

        if (context.bitEndWhenBitField_deProcess) {
            JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }


    }
}

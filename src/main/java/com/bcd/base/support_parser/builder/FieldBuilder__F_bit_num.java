package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.ParseUtil;
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();

        switch (fieldTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
            }
            default -> {
                if (fieldTypeClass.isEnum()) {
                } else {
                    ParseUtil.notSupport_fieldType(field, annoClass);
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

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

        if (Parser.logCollector_parse == null) {
            ParseUtil.append(body, "final long {}={}.read({},{},{});\n", varNameField, varNameBitBuf, len, bigEndian, unsigned);
            if (context.bitEndWhenBitField_process) {
                ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
            }
        } else {
            final String varNameReadBitLog = varNameField + "_readBitLog";
            ParseUtil.append(body, "final {} {}={}.read_log({},{},{});\n", BitBuf_reader.ReadLog.class.getName(), varNameReadBitLog, varNameBitBuf, len, bigEndian, unsigned);
            ParseUtil.append(body, "final long {}={}.val3;\n", varNameField, varNameReadBitLog);
            ParseUtil.appendBitLog_parse(context, varNameReadBitLog);
            if (context.bitEndWhenBitField_process) {
                final String varNameFinishBitLog = varNameField + "_finishBitLog";
                ParseUtil.append(body, "{} {}={}.finish_log();\n", BitBuf_reader.FinishLog.class.getName(), varNameFinishBitLog, varNameBitBuf);
                ParseUtil.appendBitLog_parse(context, varNameFinishBitLog);
            }
        }
        if (fieldTypeClass.isEnum()) {
            ParseUtil.append(body, "{}.{}={}.fromInteger((int){});\n", varNameInstance, field.getName(), fieldTypeName, ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField));
        } else {
            ParseUtil.append(body, "{}.{}=({})({});\n", varNameInstance, field.getName(), fieldTypeName, ParseUtil.replaceValExprToCode(anno.valExpr(), varNameField));
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
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

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

        if (Parser.logCollector_deParse == null) {
            ParseUtil.append(body, "{}.write((long)({}),{},{},{});\n", varNameBitBuf, valCode, len, bigEndian, unsigned);
            if (context.bitEndWhenBitField_deProcess) {
                ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
            }
        } else {
            final String varNameWriteBitLog = varNameField + "_writeBitLog";
            ParseUtil.append(body, "final {} {}={}.write_log((long)({}),{},{},{});\n", BitBuf_writer.WriteLog.class.getName(), varNameWriteBitLog, varNameBitBuf, valCode, len, bigEndian, unsigned);
            ParseUtil.appendBitLog_deParse(context, varNameWriteBitLog);
            if (context.bitEndWhenBitField_deProcess) {
                final String varNameFinishBitLog = varNameField + "_finishBitLog";
                ParseUtil.append(body, "{} {}={}.finish_log();\n", BitBuf_writer.FinishLog.class.getName(), varNameFinishBitLog, varNameBitBuf);
                ParseUtil.appendBitLog_deParse(context, varNameFinishBitLog);
            }
        }


    }
}

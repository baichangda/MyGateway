package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_bit_num extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(F_bit_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_bit_num> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldType = field.getType();
        final String goFieldTypeName;
        final String goReadTypeName;
        if (anno.len() >= 1 && anno.len() <= 8) {
            goReadTypeName = unsigned ? "uint8" : "int8";
        } else if (anno.len() >= 9 && anno.len() <= 16) {
            goReadTypeName = unsigned ? "uint16" : "int16";
        } else if (anno.len() >= 17 && anno.len() <= 32) {
            goReadTypeName = unsigned ? "uint32" : "int32";
        } else if (anno.len() >= 33 && anno.len() <= 64) {
            goReadTypeName = unsigned ? "uint64" : "int64";
        } else {
            ParseUtil.notSupport_len(field, annoClass);
            goReadTypeName = null;
        }
        if (fieldType == float.class) {
            goFieldTypeName = "float32";
        } else if (fieldType == double.class) {
            goFieldTypeName = "float64";
        } else {
            goFieldTypeName = goReadTypeName;
        }

        goField.goFieldTypeName = goFieldTypeName;
        goField.goReadTypeName = goReadTypeName;
        ParseUtil.append(body, "{} {}\n", goFieldName, goFieldTypeName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(F_bit_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String goReadTypeName = goField.goReadTypeName;
        final Class<? extends F_bit_num> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final int fieldIndex = context.fieldIndex;
        final Class<?> fieldType = field.getType();
        final String varNameBitBufReader = context.getVarNameBitBuf_reader();
        final String varNameReadVal = "v" + fieldIndex;
        final int len = anno.len();
        final String valExpr = anno.valExpr();

        ParseUtil.append(body, "{},err:={}.Read({},{},{})\n", varNameReadVal, varNameBitBufReader, len, bigEndian, unsigned);
        ParseUtil.append(body, "if err!=nil{\n");
        ParseUtil.append(body, "return nil,err\n");
        ParseUtil.append(body, "}\n");

        String valCode = varNameReadVal;
        if (!goReadTypeName.equals(goFieldTypeName)) {
            valCode = ParseUtil.replaceValExprToCode(valExpr, ParseUtil.format("{}({})", goFieldTypeName, valCode));
        }
        valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);
        ParseUtil.append(body, "{}{}.{}={}({})\n\n", GoFieldBuilder.space,GoFieldBuilder.varNameInstance, goFieldName, goFieldTypeName, valCode);
        if (anno.var() != '0') {
            if (valExpr.isEmpty()) {
                varToGoFieldName.put(anno.var(), varNameReadVal);
            } else {
                varToGoFieldName.put(anno.var(), ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName));
            }
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(F_bit_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String goReadTypeName = goField.goReadTypeName;
        final Class<? extends F_bit_num> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final String valExpr = anno.valExpr();
        final int len = anno.len();
        final String varNameBitBufWriter = context.getVarNameBitBuf_writer();
        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        if (!valExpr.isEmpty()) {
            valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        }
        if (!goReadTypeName.equals(goFieldTypeName)) {
            valCode = ParseUtil.format("{}(util.Round(float64({})))", goReadTypeName, valCode);
        }
        ParseUtil.append(body, "{}.Write(uint64({}),{},{},{})\n", varNameBitBufWriter, valCode, len, bigEndian, unsigned);

    }

}

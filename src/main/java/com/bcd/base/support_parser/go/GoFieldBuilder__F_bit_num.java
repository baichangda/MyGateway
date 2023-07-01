package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.NumType;
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
        final String jsonExt = goField.jsonExt;
        final Class<?> fieldType = field.getType();
        final int len = anno.len();
        final String goFieldTypeName;
        final NumType valType = anno.valType();
        switch (valType) {
            case uint8 -> {
                goFieldTypeName = "uint8";
            }
            case int8 -> {
                goFieldTypeName = "int8";
            }
            case uint16 -> {
                goFieldTypeName = "uint16";
            }
            case int16 -> {
                goFieldTypeName = "int16";
            }
            case uint32 -> {
                goFieldTypeName = "uint32";
            }
            case int32 -> {
                goFieldTypeName = "int32";
            }
            case uint64 -> {
                goFieldTypeName = "uint64";
            }
            case int64 -> {
                goFieldTypeName = "int64";
            }
            case float32 -> {
                goFieldTypeName = "float32";
            }
            case float64 -> {
                goFieldTypeName = "float64";
            }
            default -> {
                ParseUtil.notSupport_numType(field, annoClass);
                goFieldTypeName = null;
            }
        }

        goField.goFieldTypeName = goFieldTypeName;
        ParseUtil.append(body, "{} {} {}\n", goFieldName, goFieldTypeName, jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num anno = field.getAnnotation(F_bit_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_bit_num> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldType = field.getType();
        final String varNameBitBufReader = context.getVarNameBitBuf_reader();
        final String varNameReadVal = goFieldName + "_v";
        final int len = anno.len();
        final String valExpr = anno.valExpr();

        ParseUtil.append(body, "{}:={}.Read({},{},{})\n", varNameReadVal, varNameBitBufReader, len, bigEndian, unsigned);
        if (context.bitEndWhenBitField_process) {
            ParseUtil.append(body, "{}.Finish();\n", varNameBitBufReader);
        }
        String valCode = varNameReadVal;

        valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
        valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);

        ParseUtil.append(body, "{}.{}={}\n\n", GoFieldBuilder.varNameInstance, goFieldName, valCode);
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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final Class<? extends F_bit_num> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final String valExpr = anno.valExpr();
        final int len = anno.len();
        final String varNameBitBufWriter = context.getVarNameBitBuf_writer();
        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;


        valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        if (goFieldTypeName.equals("float32") || goFieldTypeName.equals("float64")) {
            valCode = ParseUtil.format("int64(parse.Round({}))", valCode);
        } else {
            valCode = ParseUtil.format("int64({})", valCode);
        }

        ParseUtil.append(body, "{}.Write({},{},{},{})\n", varNameBitBufWriter, valCode, len, bigEndian, unsigned);
        if (context.bitEndWhenBitField_deProcess) {
            ParseUtil.append(body, "{}.Finish();\n", varNameBitBufWriter);
        }

        if (anno.var() != '0') {
            varToGoFieldName.put(anno.var(), ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName));
        }

    }

}

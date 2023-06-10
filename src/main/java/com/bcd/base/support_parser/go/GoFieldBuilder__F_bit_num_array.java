package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bit_num_array;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_bit_num_array extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num_array anno = field.getAnnotation(F_bit_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_bit_num_array> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final Class<?> fieldType = field.getType();
        final int singleLen = anno.singleLen();
        final String goFieldTypeName;
        final String goReadTypeName;
        if (singleLen >= 1 && singleLen <= 8) {
            goReadTypeName = unsigned ? "uint8" : "int8";
        } else if (singleLen >= 9 && singleLen <= 16) {
            goReadTypeName = unsigned ? "uint16" : "int16";
        } else if (singleLen >= 17 && singleLen <= 32) {
            goReadTypeName = unsigned ? "uint32" : "int32";
        } else if (singleLen >= 33 && singleLen <= 64) {
            goReadTypeName = unsigned ? "uint64" : "int64";
        } else {
            ParseUtil.notSupport_singleLen(field, annoClass);
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
        ParseUtil.append(body, "{} []{}\n", goFieldName, goFieldTypeName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num_array anno = field.getAnnotation(F_bit_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goReadTypeName = goField.goReadTypeName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_bit_num_array> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final String valExpr = anno.valExpr();
        final String varNameBitBufReader = context.getVarNameBitBuf_reader();
        String varNameLen = goFieldName + "_len";
        final int len = anno.len();
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:=make([]{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
        ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
        ParseUtil.append(body, "e:={}.Read({},{},{})\n", varNameBitBufReader, singleLen, bigEndian, unsigned);
        String valCode = "e";
        valCode = ParseUtil.format("{}({})", goFieldTypeName, valCode);
        valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);
        ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, varNameArr);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num_array anno = field.getAnnotation(F_bit_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goReadTypeName = goField.goReadTypeName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_bit_num_array> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_bitOrder);
        final boolean unsigned = anno.unsigned();
        final int singleLen = anno.singleLen();
        final String valExpr = anno.valExpr();
        final String varNameBitBufWriter = context.getVarNameBitBuf_writer();

        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
        String valCode = ParseUtil.format("{}[i]", varNameArr);
        if (!valExpr.isEmpty()) {
            valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        }
        if (!goReadTypeName.equals(goFieldTypeName)) {
            valCode = ParseUtil.format("parse.Round(float64({}))", valCode);
        }
        ParseUtil.append(body, "{}.Write(int64({}),{},{},{})\n", varNameBitBufWriter, valCode, singleLen, bigEndian, unsigned);
        ParseUtil.append(body, "}\n");
    }

}

package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bit_num_array;
import com.bcd.base.support_parser.anno.NumType;
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
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.pkg_bitOrder);
        final boolean unsigned = anno.singleUnsigned();
        final int len = anno.len();
        final NumType valType = anno.singleValType();
        final Class<?> fieldType = field.getType();
        final int singleLen = anno.singleLen();
        final String jsonExt = goField.jsonExt;
        final String goFieldTypeName;
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
        if (len == 0) {
            if ("uint8".equals(goFieldTypeName)) {
                ParseUtil.append(body, "{} parse.JsonUint8Arr {}\n", goFieldName, jsonExt);
            } else {
                ParseUtil.append(body, "{} []{} {}\n", goFieldName, goFieldTypeName, jsonExt);
            }
        } else {
            ParseUtil.append(body, "{} [{}]{} {}\n", goFieldName, len, goFieldTypeName, jsonExt);
        }
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_num_array anno = field.getAnnotation(F_bit_num_array.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_bit_num_array> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.pkg_bitOrder);
        final boolean unsigned = anno.singleUnsigned();
        final int singleLen = anno.singleLen();
        final String valExpr = anno.singleValExpr();
        final String varNameBitBufReader = context.getVarNameBitBuf_reader();
        String varNameLen = goFieldName + "_len";
        final int len = anno.len();
        final String varNameArr = goFieldName + "_arr";
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            ParseUtil.append(body, "{}:=make([]{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
            ParseUtil.append(body, "{}:=[]{}{}\n", varNameArr, len, goFieldTypeName);
        }

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
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_bit_num_array> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.pkg_bitOrder);
        final boolean unsigned = anno.singleUnsigned();
        final int singleLen = anno.singleLen();
        final String valExpr = anno.singleValExpr();
        final String varNameBitBufWriter = context.getVarNameBitBuf_writer();

        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
        String valCode = ParseUtil.format("{}[i]", varNameArr);

        valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        if (goFieldTypeName.equals("float32") || goFieldTypeName.equals("float64")) {
            valCode = ParseUtil.format("int64(parse.Round({}))", valCode);
        } else {
            valCode = ParseUtil.format("int64({})", valCode);
        }

        ParseUtil.append(body, "{}.Write({},{},{},{})\n", varNameBitBufWriter, valCode, singleLen, bigEndian, unsigned);
        ParseUtil.append(body, "}\n");
    }

}

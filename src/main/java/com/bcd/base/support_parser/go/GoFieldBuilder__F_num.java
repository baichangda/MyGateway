package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.ByteOrder;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_num extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<? extends F_num> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean unsigned = anno.unsigned();
        final String goFiledTypeName;
        switch (anno.len()) {
            case 1 -> {
                goFiledTypeName = unsigned ? "uint8" : "int8";
            }
            case 2 -> {
                goFiledTypeName = unsigned ? "uint16" : "int16";
            }
            case 4 -> {
                goFiledTypeName = unsigned ? "uint32" : "int32";
            }
            case 8 -> {
                goFiledTypeName = unsigned ? "uint64" : "int64";
            }
            default -> {
                ParseUtil.notSupport_len(field, annoClass);
                goFiledTypeName = null;
            }
        }
        goField.goFieldTypeName = goFiledTypeName;
        ParseUtil.append(body, "  {} {}\n", goFieldName, goFiledTypeName);
    }

    @Override
    public void buildFunc(GoBuildContext context) {
        final Field field = context.field;
        final F_num anno = field.getAnnotation(F_num.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_num> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder parseFuncBody = context.parseFuncBody;
        final StringBuilder deParseFuncBody = context.deParseFuncBody;
        final boolean bigEndian = anno.order() != ByteOrder.SmallEndian;
        final boolean unsigned = anno.unsigned();
        final int fieldIndex = context.fieldIndex;

        String valCode;
        switch (anno.len()) {
            case 1 -> {
                ParseUtil.append(parseFuncBody, "  v{},err:=buf.Read_{}()\n", fieldIndex, goFieldTypeName);
            }
            case 2, 4, 8 -> {
                ParseUtil.append(parseFuncBody, "  v{},err:=buf.Read_{}({})\n", fieldIndex, goFieldTypeName, bigEndian);
            }
            default -> {
                ParseUtil.notSupport_len(field, annoClass);
                valCode = "";
            }
        }
        ParseUtil.append(parseFuncBody, "  if err!=nil{\n");
        ParseUtil.append(parseFuncBody, "    return nil,err\n");
        ParseUtil.append(parseFuncBody, "  }\n");
        valCode = ParseUtil.replaceValExprToCode(anno.valExpr(), "v" + fieldIndex);
        ParseUtil.append(parseFuncBody, "  e.{}={}\n", goFieldName, valCode);
        if (anno.var() != '0') {
            varToGoFieldName.put(anno.var(), goFieldName);
        }
    }

}

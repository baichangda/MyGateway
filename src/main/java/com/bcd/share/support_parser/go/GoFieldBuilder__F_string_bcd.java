package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_string_bcd;
import com.bcd.share.support_parser.anno.StringAppendMode;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_string_bcd extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final StringBuilder body = context.structBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String jsonExt = goField.jsonExt;
        ParseUtil.append(body, "{} string {}\n", goFieldName, jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        final Class<? extends F_string_bcd> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int len = anno.len();
        final String varNameLen = goFieldName + "_len";
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        final StringAppendMode stringAppendMode = anno.appendMode();
        switch (stringAppendMode) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}=parse.ReadBcd_string_noAppend({},{})\n", GoFieldBuilder.varNameInstance, goFieldName, varNameByteBuf, varNameLen);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.{}=parse.ReadBcd_string_lowAddressAppend({},{})\n", GoFieldBuilder.varNameInstance, goFieldName, varNameByteBuf, varNameLen);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.{}=parse.ReadBcd_string_highAddressAppend({},{})\n", GoFieldBuilder.varNameInstance, goFieldName, varNameByteBuf, varNameLen);
            }
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        final Class<? extends F_string_bcd> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int len = anno.len();
        final String varNameLen = goFieldName + "_len";
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }
        final String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        final StringAppendMode stringAppendMode = anno.appendMode();
        switch (stringAppendMode) {
            case noAppend -> {
                ParseUtil.append(body, "parse.WriteBcd_string_noAppend({},{})\n", GoFieldBuilder.varNameInstance, valCode);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "parse.WriteBcd_string_lowAddressAppend({},{},{})\n", GoFieldBuilder.varNameInstance, valCode, varNameLen);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "parse.WriteBcd_string_highAddressAppend({},{},{})\n", GoFieldBuilder.varNameInstance, valCode, varNameLen);
            }
        }
    }

}

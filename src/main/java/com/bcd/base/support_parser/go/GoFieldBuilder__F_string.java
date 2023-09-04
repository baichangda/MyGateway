package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.StringAppendMode;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GoFieldBuilder__F_string extends GoFieldBuilder {
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
        final F_string anno = field.getAnnotation(F_string.class);
        final Class<? extends F_string> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int len = anno.len();
        final String charset = anno.charset();

        if (Charset.forName(charset) != StandardCharsets.UTF_8) {
            ParseUtil.notSupport_charset(context.clazz, field, annoClass);
        }

        final String varNameLen = goFieldName + "_len";
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        final StringAppendMode stringAppendMode = anno.appendMode();
        final String varNameReadVal = goFieldName + "_v";
        ParseUtil.append(body, "{}:={}.Read_slice_uint8({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, varNameLen);
        switch (stringAppendMode) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}=string({})\n\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case highAddressAppend -> {
                final String varNameCount = goFieldName + "_count";
                ParseUtil.append(body, "{}:=0\n", varNameCount);
                ParseUtil.append(body, "for i:={}-1;i>=0;i--{\n", varNameLen);
                ParseUtil.append(body, "if {}[i]==0{\n", varNameReadVal);
                ParseUtil.append(body, "{}++\n", varNameCount);
                ParseUtil.append(body, "}else{\n");
                ParseUtil.append(body, "break\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=string({}[:({}-{})])\n\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal, varNameLen, varNameCount);
            }
            case lowAddressAppend -> {
                final String varNameCount = goFieldName + "_count";
                ParseUtil.append(body, "{}:=0\n", varNameCount);
                ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
                ParseUtil.append(body, "if {}[i]==0{\n", varNameReadVal);
                ParseUtil.append(body, "{}++\n", varNameCount);
                ParseUtil.append(body, "}else{\n");
                ParseUtil.append(body, "break\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=string({}[{}:])\n\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal, varNameCount);
            }
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final Class<? extends F_string> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_deParse;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int len = anno.len();
        final String charset = anno.charset();
        final StringAppendMode stringAppendMode = anno.appendMode();
        if (Charset.forName(charset) != StandardCharsets.UTF_8) {
            ParseUtil.notSupport_charset(context.clazz, field, annoClass);
        }

        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        if (stringAppendMode == StringAppendMode.noAppend) {
            ParseUtil.append(body, "{}.Write_string_utf8({})\n", GoFieldBuilder.varNameByteBuf, valCode);
        } else {
            final String varNameLen = goFieldName + "_len";
            if (len == 0) {
                ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            } else {
                ParseUtil.append(body, "{}:={}\n", varNameLen, len);
            }
            final String varNameBytes = goFieldName + "_v";
            switch (stringAppendMode) {
                case highAddressAppend -> {
                    ParseUtil.append(body, "{}:=[]byte({})\n", varNameBytes, valCode);
                    ParseUtil.append(body, "{}.Write_slice_uint8({})\n", GoFieldBuilder.varNameByteBuf, varNameBytes);
                    ParseUtil.append(body, "{}.Write_zero({}-len({}))\n", GoFieldBuilder.varNameByteBuf, varNameLen, varNameBytes);
                }
                case lowAddressAppend -> {
                    ParseUtil.append(body, "{}:=[]byte({})\n", varNameBytes, valCode);
                    ParseUtil.append(body, "{}.Write_zero({}-len({}))\n", GoFieldBuilder.varNameByteBuf, varNameLen, varNameBytes);
                    ParseUtil.append(body, "{}.Write_slice_uint8({})\n", GoFieldBuilder.varNameByteBuf, varNameBytes);
                }
            }
        }
    }

}

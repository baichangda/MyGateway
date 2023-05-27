package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.StringAppendMode;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

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
        ParseUtil.append(body, "{} string\n", goFieldName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_string anno = field.getAnnotation(F_string.class);
        final Class<? extends F_string> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int len = anno.len();
        final int fieldIndex = context.fieldIndex;
        final String charset = anno.charset();

        if (Charset.forName(charset) != StandardCharsets.UTF_8) {
            ParseUtil.notSupport_charset(field, annoClass);
        }

        final String varNameLen = ParseUtil.format("len{}", fieldIndex);
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        final StringAppendMode stringAppendMode = anno.appendMode();
        final String varNameReadVal = "v" + fieldIndex;
        ParseUtil.append(body, "{},err:={}.Read_bytes({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, varNameLen);
        ParseUtil.append(body, "if err!=nil{\n");
        ParseUtil.append(body, "return nil,err\n");
        ParseUtil.append(body, "}\n");
        switch (stringAppendMode) {
            case NoAppend -> {
                ParseUtil.append(body, "{}.{}=string({})\n\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case HighAddressAppend -> {
                final String varNameCount = "count" + fieldIndex;
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
            case LowAddressAppend -> {
                final String varNameCount = "count" + fieldIndex;
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
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int len = anno.len();
        final int fieldIndex = context.fieldIndex;
        final String charset = anno.charset();
        final StringAppendMode stringAppendMode = anno.appendMode();
        if (Charset.forName(charset) != StandardCharsets.UTF_8) {
            ParseUtil.notSupport_charset(field, annoClass);
        }

        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        if (stringAppendMode == StringAppendMode.NoAppend) {
            ParseUtil.append(body, "{}.Write_string_utf8({})\n", GoFieldBuilder.varNameByteBuf, valCode);
        } else {
            final String varNameLen = ParseUtil.format("len{}", fieldIndex);
            if (len == 0) {
                ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
            } else {
                ParseUtil.append(body, "{}:={}\n", varNameLen, len);
            }
            final String varNameBytes = "v" + fieldIndex;
            switch (stringAppendMode) {
                case HighAddressAppend -> {
                    ParseUtil.append(body, "{}:=[]byte({})\n", varNameBytes, valCode);
                    ParseUtil.append(body, "{}.Write_bytes({})\n", GoFieldBuilder.varNameByteBuf, varNameBytes);
                    ParseUtil.append(body, "{}.Write_zero({}-len({}))\n", GoFieldBuilder.varNameByteBuf, varNameLen, varNameBytes);
                }
                case LowAddressAppend -> {
                    ParseUtil.append(body, "{}:=[]byte({})\n", varNameBytes, valCode);
                    ParseUtil.append(body, "{}.Write_zero({}-len({}))\n", GoFieldBuilder.varNameByteBuf, varNameLen, varNameBytes);
                    ParseUtil.append(body, "{}.Write_bytes({})\n", GoFieldBuilder.varNameByteBuf, varNameBytes);
                }
            }
        }
    }

}

package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.anno.SkipMode;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_skip extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {

    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
        final Class<? extends F_skip> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.parseBody;
        final int len = anno.len();
        final int fieldIndex = context.fieldIndex;
        final SkipMode mode = anno.mode();


        final String varNameLen = ParseUtil.format("len{}", fieldIndex);
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        switch (mode) {
            case Skip -> {
                ParseUtil.append(body, "{}.Skip({})\n\n", GoFieldBuilder.varNameByteBuf, varNameLen);
            }
            case ReservedFromStart -> {
                final String varNameSkipLen = ParseUtil.format("skipLen{}", fieldIndex);
                ParseUtil.append(body, "{}:={}+{}-{}.ReaderIndex()\n", varNameSkipLen, varNameLen, GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(body, "if {}>0{\n", varNameSkipLen);
                ParseUtil.append(body, "{}.Skip({})\n", GoFieldBuilder.varNameByteBuf, varNameSkipLen);
                ParseUtil.append(body, "}\n\n");
            }
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_skip anno = field.getAnnotation(F_skip.class);
        final Class<? extends F_skip> annoClass = anno.getClass();
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName;
        final StringBuilder body = context.deParseBody;
        final int len = anno.len();
        final int fieldIndex = context.fieldIndex;
        final SkipMode mode = anno.mode();

        final String varNameLen = ParseUtil.format("len{}", fieldIndex);
        if (len == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.lenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, len);
        }

        switch (mode) {
            case Skip -> {
                ParseUtil.append(body, "{}.Write_zero({})\n\n", GoFieldBuilder.varNameByteBuf, varNameLen);
            }
            case ReservedFromStart -> {
                final String varNameSkipLen = ParseUtil.format("skipLen{}", fieldIndex);
                ParseUtil.append(body, "{}:={}+{}-{}.WriterIndex()\n", varNameSkipLen, varNameLen, GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(body, "if {}>0{\n", varNameSkipLen);
                ParseUtil.append(body, "{}.Write_zero({})\n", GoFieldBuilder.varNameByteBuf, varNameSkipLen);
                ParseUtil.append(body, "}\n\n");
            }
        }
    }

}

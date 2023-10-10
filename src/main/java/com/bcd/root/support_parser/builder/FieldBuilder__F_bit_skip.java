package com.bcd.root.support_parser.builder;


import com.bcd.root.support_parser.anno.F_bit_skip;
import com.bcd.root.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf_reader();
        ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
        if (context.bitEndWhenBitField_process) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }


    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf_writer();

        ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
        if (context.bitEndWhenBitField_deProcess) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

    }
}

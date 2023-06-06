package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bean extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String fieldTypeClassName = field.getType().getName();
        final String parserClassName = Parser.class.getName();
        final String processContextVarName = context.getProcessContextVarName();
        if (anno.passBitBuf()) {
            final String varNameBitBuf = context.getVarNameBitBuf_reader();
            ParseUtil.append(body, "{}.bitBuf_reader={};\n", processContextVarName, varNameBitBuf);
        }
        ParseUtil.append(body, "{}.{}=({}){}.parse({}.class,{},{});\n",
                FieldBuilder.varNameInstance,
                field.getName(),
                fieldTypeClassName,
                parserClassName,
                fieldTypeClassName,
                FieldBuilder.varNameByteBuf,
                processContextVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final String fieldName = field.getName();
        final String parserClassName = Parser.class.getName();
        final String processContextVarName = context.getProcessContextVarName();
        if (anno.passBitBuf()) {
            final String varNameBitBuf = context.getVarNameBitBuf_writer();
            ParseUtil.append(body, "{}.bitBuf_writer={};\n", processContextVarName, varNameBitBuf);
        }
        ParseUtil.append(body, "{}.deParse({},{},{});\n",
                parserClassName,
                FieldBuilder.varNameInstance + "." + fieldName,
                FieldBuilder.varNameByteBuf,
                processContextVarName);
    }
}

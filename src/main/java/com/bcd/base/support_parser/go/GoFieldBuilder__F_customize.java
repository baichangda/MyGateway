package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_customize extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final StringBuilder body = context.structBody;
        final Class<?> clazz = context.clazz;
        ParseUtil.append(body, "//todo Struct Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final StringBuilder body = context.parseBody;
        final Class<?> clazz = context.clazz;
        ParseUtil.append(body, "//todo Read Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final StringBuilder body = context.deParseBody;
        final Class<?> clazz = context.clazz;
        ParseUtil.append(body, "//todo Write Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
    }
}

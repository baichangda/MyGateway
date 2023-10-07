package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_bean extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = field.getType().getSimpleName();
        final String jsonExt = goField.jsonExt;
        goField.goFieldTypeName = goFieldTypeName;
        ParseUtil.append(body, "{} *{} {}\n", goFieldName, goFieldTypeName, jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String varNameParseContext;
        if (ParseUtil.checkChildrenHasAnno_F_customize(fieldType)) {
            varNameParseContext = context.getVarNameParseContext();
        } else {
            varNameParseContext = "nil";
        }
        final String valCode = ParseUtil.format("To_{}({},{})", goFieldTypeName, GoFieldBuilder.varNameByteBuf, varNameParseContext);
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, valCode);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String varNameDeParseContext;
        if (ParseUtil.checkChildrenHasAnno_F_customize(fieldType)) {
            varNameDeParseContext = context.getVarNameDeParseContext();
        } else {
            varNameDeParseContext = "nil";
        }
        final String valCode = ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName);
        ParseUtil.append(body, "{}.Write({},{})\n", valCode, GoFieldBuilder.varNameByteBuf, varNameDeParseContext);
    }

}

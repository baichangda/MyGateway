package com.bcd.root.support_parser.builder;

import com.bcd.root.support_parser.anno.F_bean;
import com.bcd.root.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bean extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final String fieldTypeClassName = fieldType.getName();
        final String processContextVarName;
        if (ParseUtil.checkChildrenHasAnno_F_customize(fieldType)) {
            processContextVarName = context.getProcessContextVarName();
        } else {
            processContextVarName = "null";
        }
        final String processorVarName = context.getProcessorVarName(fieldType);
        ParseUtil.append(body, "{}.{}=({}){}.process({},{});\n",
                varNameInstance,
                field.getName(),
                fieldTypeClassName,
                processorVarName,
                varNameByteBuf,
                processContextVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<?> fieldType = field.getType();
        final String fieldName = field.getName();
        final String processContextVarName;
        if (ParseUtil.checkChildrenHasAnno_F_customize(fieldType)) {
            processContextVarName = context.getProcessContextVarName();
        } else {
            processContextVarName = "null";
        }
        final String processorVarName = context.getProcessorVarName(fieldType);
        ParseUtil.append(body, "{}.deProcess({},{},{});\n",
                processorVarName,
                varNameByteBuf,
                processContextVarName,
                varNameInstance + "." + fieldName);
    }
}

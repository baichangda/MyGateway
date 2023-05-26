package com.bcd.base.support_parser.builder;


import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FieldBuilder__F_customize extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        final Class<?> builderClass = anno.builderClass();
        final Class<?> processorClass = anno.processorClass();
        if (builderClass == void.class) {
            if (processorClass == void.class) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have builderClass or processorClass", field.getDeclaringClass().getName(), field.getName(), F_customize.class.getName());
            } else {
                final StringBuilder body = context.body;
                final String varNameField = ParseUtil.getFieldVarName(context);
                final String processorClassVarName = ParseUtil.getProcessorVarName(processorClass);
                final String varNameInstance = FieldBuilder.varNameInstance;
                final String fieldTypeClassName = field.getType().getName();
                final String processContextVarName = context.getProcessContextVarName();
                if (anno.passBitBuf()) {
                    final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);
                    ParseUtil.append(body, "{}.bitBuf_reader={};\n", processContextVarName, varNameBitBuf);
                }
                final String unBoxing = ParseUtil.unBoxing(ParseUtil.format("{}.process({},{})", processorClassVarName, FieldBuilder.varNameByteBuf, processContextVarName), field.getType());
                if (anno.var() == '0') {
                    ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), unBoxing);
                } else {
                    ParseUtil.append(body, "final {} {}={};\n", fieldTypeClassName, varNameField, unBoxing);
                    ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
                    context.varToFieldName.put(anno.var(), varNameField);
                }
            }
        } else {
            BuilderContext.fieldBuilderCache.computeIfAbsent(builderClass, k -> {
                try {
                    return (FieldBuilder) builderClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw BaseRuntimeException.getException(e);
                }
            }).buildParse(context);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final F_customize anno = field.getAnnotation(F_customize.class);
        final Class<?> builderClass = anno.builderClass();
        final Class<?> processorClass = anno.processorClass();
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varInstanceName = FieldBuilder.varNameInstance;
        final String valCode;
        if (anno.var() == '0') {
            valCode = varInstanceName + "." + field.getName();
        } else {
            ParseUtil.append(body, "final {} {}={};\n", field.getType().getName(), varNameField, varInstanceName + "." + field.getName());
            valCode = varNameField;
        }
        if (builderClass == void.class) {
            if (processorClass == void.class) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have builderClass or processorClass", field.getDeclaringClass().getName(), field.getName(), F_customize.class.getName());
            } else {
                final String processContextVarName = context.getProcessContextVarName();
                if (anno.passBitBuf()) {
                    final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);
                    ParseUtil.append(body, "{}.bitBuf_writer={};\n", processContextVarName, varNameBitBuf);
                }
                final String processorClassVarName = ParseUtil.getProcessorVarName(processorClass);
                ParseUtil.append(body, "{}.deProcess({},{},{});\n", processorClassVarName, FieldBuilder.varNameByteBuf, processContextVarName, valCode);
            }
        } else {
            BuilderContext.fieldBuilderCache.computeIfAbsent(builderClass, k -> {
                try {
                    return (FieldBuilder) builderClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw BaseRuntimeException.getException(e);
                }
            }).buildDeParse(context);
        }
    }
}

package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

public class FieldBuilder__F_bean_list extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final Class<?> typeClass;
        final int fieldTypeFlag;
        if (fieldType.isArray()) {
            fieldTypeFlag = 1;
            typeClass = fieldType.getComponentType();
        } else if (List.class.isAssignableFrom(fieldType)) {
            fieldTypeFlag = 2;
            typeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            ParseUtil.notSupport_type(context.clazz, field, anno.getClass());
            fieldTypeFlag = 0;
            typeClass = null;
        }

        final String varNameField = ParseUtil.getFieldVarName(context);
        final String fieldVarNameListLen = varNameField + "_listLen";
        if (anno.listLen() == 0) {
            String listLenRes = ParseUtil.replaceLenExprToCode(anno.listLenExpr(), context.varToFieldName, field);
            ParseUtil.append(body, "final int {}={};\n", fieldVarNameListLen, listLenRes);
        } else {
            ParseUtil.append(body, "final int {}={};\n", fieldVarNameListLen, anno.listLen());
        }
        final String typeClassName = typeClass.getName();
        final String processorVarName = context.getProcessorVarName(typeClass);
        final String processContextVarName = context.getProcessContextVarName();
        if (anno.passBitBuf()) {
            final String varNameBitBuf = context.getVarNameBitBuf_reader();
            ParseUtil.append(body, "{}.bitBuf_reader={};\n", processContextVarName, varNameBitBuf);
        }

        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", typeClassName, varNameField, typeClassName, fieldVarNameListLen);
        //在for循环外构造复用对象
        ParseUtil.append(body, "for(int i=0;i<{};i++){\n", fieldVarNameListLen);
        ParseUtil.append(body, "{}[i]={}.process({},{});\n", varNameField, processorVarName, FieldBuilder.varNameByteBuf, processContextVarName);
        ParseUtil.append(body, "}\n");

        switch (fieldTypeFlag) {
            case 1 -> {
                ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), varNameField);
            }
            case 2 -> {
                ParseUtil.append(body, "{}.{}={}.asList({});\n", FieldBuilder.varNameInstance, field.getName(), Arrays.class.getName(), varNameField);
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Field field = context.field;
        final F_bean_list anno = context.field.getAnnotation(F_bean_list.class);
        final String fieldName = field.getName();
        final String valCode = FieldBuilder.varNameInstance + "." + fieldName;

        ParseUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);

        final Class<?> fieldType = field.getType();
        final Class<?> typeClass;
        final int fieldTypeFlag;
        if (fieldType.isArray()) {
            fieldTypeFlag = 1;
            typeClass = fieldType.getComponentType();
        } else if (List.class.isAssignableFrom(fieldType)) {
            fieldTypeFlag = 2;
            typeClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        } else {
            ParseUtil.notSupport_type(context.clazz, field, anno.getClass());
            fieldTypeFlag = 0;
            typeClass = null;
        }

        final String typeClassName = typeClass.getName();
        final String processorVarName = context.getProcessorVarName(typeClass);
        final String processContextVarName = context.getProcessContextVarName();
        if (anno.passBitBuf()) {
            final String varNameBitBuf = context.getVarNameBitBuf_writer();
            ParseUtil.append(body, "{}.bitBuf_writer={};\n", processContextVarName, varNameBitBuf);
        }
        final String fieldVarNameTemp = varNameField + "_temp";
        switch (fieldTypeFlag) {
            case 1 -> {
                ParseUtil.append(body, "final {}[] {}={};\n", typeClassName, varNameField, valCode);
                //在for循环外构造复用对象
                ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameField);
                ParseUtil.append(body, "final {} {}=({}){}[i];\n", typeClassName, fieldVarNameTemp, typeClassName, varNameField);
            }
            case 2 -> {
                final String listClassName = List.class.getName();
                ParseUtil.append(body, "final {} {}={};\n", listClassName, varNameField, valCode);
                //在for循环外构造复用对象
                ParseUtil.append(body, "for(int i=0;i<{}.size();i++){\n", varNameField);
                ParseUtil.append(body, "final {} {}=({}){}.get(i);\n", typeClassName, fieldVarNameTemp, typeClassName, varNameField);
            }
        }
        ParseUtil.append(body, "{}.deProcess({},{},{});\n", processorVarName, FieldBuilder.varNameByteBuf, processContextVarName, fieldVarNameTemp);
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "}\n");
    }
}

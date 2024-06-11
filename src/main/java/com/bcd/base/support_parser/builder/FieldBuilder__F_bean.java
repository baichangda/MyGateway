package com.bcd.base.support_parser.builder;

import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.anno.C_impl;
import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FieldBuilder__F_bean extends FieldBuilder {

    static String pkg = "com.bcd";

    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        final String fieldTypeClassName = fieldType.getName();
        final String processContextVarName = context.getProcessContextVarName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw MyException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={}", varNameField_implClassVal, implClassValCode);
            //找到其实现类子类
            try {
                List<Class<?>> implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg);
                implClassList = implClassList.stream().filter(e -> e.isAnnotationPresent(C_impl.class)).toList();
                for (Class<?> implClass : implClassList) {
                    C_impl c_impl = implClass.getAnnotation(C_impl.class);
                    final String implProcessorVarName;
                    if (c_impl.processorClass() == Void.class) {
                        implProcessorVarName = context.getProcessorVarName(implClass);
                    } else {
                        implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                    }
                    ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
                    ParseUtil.append(body, "case {}-> {}.{}=({}){}.process({},{});\n",
                            Arrays.toString(c_impl.val()),
                            varNameInstance,
                            field.getName(),
                            fieldTypeClassName,
                            implProcessorVarName,
                            varNameByteBuf,
                            processContextVarName);
                    ParseUtil.append(body, "}\n");
                }

            } catch (IOException | ClassNotFoundException e) {
                throw MyException.get(e);
            }
        } else {
            final String processorVarName = context.getProcessorVarName(fieldType);
            ParseUtil.append(body, "{}.{}=({}){}.process({},{});\n",
                    varNameInstance,
                    field.getName(),
                    fieldTypeClassName,
                    processorVarName,
                    varNameByteBuf,
                    processContextVarName);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<?> fieldType = field.getType();
        final String fieldName = field.getName();
        final String processContextVarName = context.getProcessContextVarName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw MyException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={}", varNameField_implClassVal, implClassValCode);
            try {
                List<Class<?>> implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg);
                implClassList = implClassList.stream().filter(e -> e.isAnnotationPresent(C_impl.class)).toList();
                for (Class<?> implClass : implClassList) {
                    C_impl c_impl = implClass.getAnnotation(C_impl.class);
                    final String implProcessorVarName;
                    if (c_impl.processorClass() == Void.class) {
                        implProcessorVarName = context.getProcessorVarName(implClass);
                    } else {
                        implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                    }
                    ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
                    ParseUtil.append(body, "case {}-> {}.deProcess({},{},{});\n",
                            Arrays.toString(c_impl.val()),
                            implProcessorVarName,
                            varNameByteBuf,
                            processContextVarName,
                            varNameInstance + "." + fieldName);
                    ParseUtil.append(body, "}\n");
                }

            } catch (IOException | ClassNotFoundException e) {
                throw MyException.get(e);
            }
        } else {
            final String processorVarName = context.getProcessorVarName(fieldType);
            ParseUtil.append(body, "{}.deProcess({},{},{});\n",
                    processorVarName,
                    varNameByteBuf,
                    processContextVarName,
                    varNameInstance + "." + fieldName);
        }
    }

}

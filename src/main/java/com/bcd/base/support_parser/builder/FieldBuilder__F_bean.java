package com.bcd.base.support_parser.builder;

import com.bcd.base.exception.MyException;
import com.bcd.base.support_parser.anno.C_impl;
import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class FieldBuilder__F_bean extends FieldBuilder {

    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final Class<?> fieldType = field.getType();
        String processContextVarName = context.getProcessContextVarName();
        String fieldTypeName = fieldType.getName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw MyException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={};\n", varNameField_implClassVal, implClassValCode);
            //找到其实现类子类
            try {
                String pkg = fieldTypeName.substring(0, fieldTypeName.lastIndexOf("."));
                List<Class<?>> implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg);
                implClassList = implClassList.stream().filter(e -> e.isAnnotationPresent(C_impl.class)).toList();
                ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
                for (Class<?> implClass : implClassList) {
                    C_impl c_impl = implClass.getAnnotation(C_impl.class);
                    final String implProcessorVarName;
                    if (c_impl.processorClass() == Void.class) {
                        implProcessorVarName = context.getProcessorVarName(implClass);
                    } else {
                        implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                    }
                    int[] value = c_impl.value();
                    for (int i = 0; i < value.length - 1; i++) {
                        ParseUtil.append(body, "case {}:{}\n", value[i]);
                    }
                    ParseUtil.append(body, "case {}:{\n{}.{}={}.process({},{});\nbreak;\n}\n",
                            value[value.length - 1],
                            varNameInstance,
                            field.getName(),
                            implProcessorVarName,
                            varNameByteBuf,
                            processContextVarName);
                }
                ParseUtil.append(body, "default:{\nthrow {}.get(\"class[{}] field[{}] implClass value[\"+" + varNameField_implClassVal + "+\"] not support\");\n}",
                        MyException.class.getName(),
                        field.getDeclaringClass().getName(),
                        field.getName());
                ParseUtil.append(body, "}\n");

            } catch (IOException | ClassNotFoundException e) {
                throw MyException.get(e);
            }
        } else {
            final String processorVarName = context.getProcessorVarName(fieldType);
            ParseUtil.append(body, "{}.{}=({}){}.process({},{});\n",
                    varNameInstance,
                    field.getName(),
                    fieldTypeName,
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
        String fieldTypeName = fieldType.getName();
        final String fieldName = field.getName();
        String processContextVarName = context.getProcessContextVarName();
        if (fieldType.isInterface()) {
            String implClassExpr = anno.implClassExpr();
            if (implClassExpr.isEmpty()) {
                throw MyException.get("class[{}] interface field[{}] anno[{}] implClassExpr must not be empty", field.getDeclaringClass().getName(), field.getName(), F_bean.class.getName());
            }
            String implClassValCode = ParseUtil.replaceExprToCode(implClassExpr, context);
            String varNameField_implClassVal = varNameField + "_implClassVal";
            ParseUtil.append(body, "int {}={};\n", varNameField_implClassVal, implClassValCode);
            try {
                String pkg = fieldTypeName.substring(0, fieldTypeName.lastIndexOf("."));
                List<Class<?>> implClassList = ClassUtil.getClassesByParentClass(fieldType, pkg);
                implClassList = implClassList.stream().filter(e -> e.isAnnotationPresent(C_impl.class)).toList();
                ParseUtil.append(body, "switch({}){\n", varNameField_implClassVal);
                for (Class<?> implClass : implClassList) {
                    C_impl c_impl = implClass.getAnnotation(C_impl.class);
                    final String implProcessorVarName;
                    final String castClassName = implClass.getName();
                    if (c_impl.processorClass() == Void.class) {
                        implProcessorVarName = context.getProcessorVarName(implClass);
                    } else {
                        implProcessorVarName = context.getCustomizeProcessorVarName(c_impl.processorClass(), c_impl.processorArgs());
                    }
                    int[] value = c_impl.value();
                    for (int i = 0; i < value.length - 1; i++) {
                        ParseUtil.append(body, "case {}:{}\n", value[i]);
                    }
                    ParseUtil.append(body, "case {}:{\n{}.deProcess({},{},({})({}));\nbreak;\n}\n",
                            value[value.length - 1],
                            implProcessorVarName,
                            varNameByteBuf,
                            processContextVarName,
                            castClassName,
                            varNameInstance + "." + fieldName);
                }
                ParseUtil.append(body, "default:{\nthrow {}.get(\"class[{}] field[{}] implClass value[\"+" + varNameField_implClassVal + "+\"] not support\");\n}",
                        MyException.class.getName(),
                        field.getDeclaringClass().getName(),
                        field.getName(),
                        fieldTypeName);
                ParseUtil.append(body, "}\n");

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

    public static void main(String[] args) throws CannotCompileException, IOException {
        CtClass cc = ClassPool.getDefault().makeClass("com.bcd.base.support_parser.builder.TestSwitch");
        String body = """
                public void test(int i){
                    switch(i){
                        case 1,3->java.lang.System.out.println(1);
                        case 2->java.lang.System.out.println(2);
                    }
                }
                """;
//        String body = """
//                public void test(int i){
//                    switch(i){
//                        case 1:{}
//                        case 3:{
//                            java.lang.System.out.println(1);
//                            break;
//                        }
//                        case 2:{
//                            java.lang.System.out.println(2);
//                            break;
//                        }
//                    }
//                }
//                """;
        CtMethod cm = CtNewMethod.make(body, cc);
        cc.addMethod(cm);
        cc.writeFile("src/main/java");
        Class<?> aClass = cc.toClass(FieldBuilder__F_bean.class);
        System.out.println(aClass.getName());
    }

}

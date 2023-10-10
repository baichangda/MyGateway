package com.bcd.root.support_parser.go;

import com.bcd.root.support_parser.anno.F_bean_list;
import com.bcd.root.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class GoFieldBuilder__F_bean_list extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_bean_list anno = field.getAnnotation(F_bean_list.class);
        final Class<? extends F_bean_list> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<?> fieldType = field.getType();
        final String jsonExt = goField.jsonExt;
        final int listLen = anno.listLen();
        final Class<?> goFieldType;
        if (fieldType.isArray()) {
            goFieldType = fieldType.getComponentType();
        } else {
            goFieldType = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
        }
        final String goFieldTypeName = GoParseUtil.toFirstUpperCase(goFieldType.getSimpleName());
        goField.goFieldTypeName = goFieldTypeName;
        if (listLen == 0) {
            ParseUtil.append(body, "{} []*{} {}\n", goFieldName, goFieldTypeName, jsonExt);
        } else {
            ParseUtil.append(body, "{} [{}]*{} {}\n", goFieldName, listLen, goFieldTypeName, jsonExt);
        }
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final Class<?> goFieldType;
        if (fieldType.isArray()) {
            goFieldType = fieldType.getComponentType();
        } else {
            goFieldType = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
        }
        final F_bean_list anno = field.getAnnotation(F_bean_list.class);
        final Class<? extends F_bean_list> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Map<Character, String> varToGoFieldName = context.varToGoFieldName_parse;
        final int listLen = anno.listLen();
        String varNameLen = goFieldName + "_len";
        if (listLen == 0) {
            ParseUtil.append(body, "{}:={}\n", varNameLen, ParseUtil.replaceLenExprToCode(anno.listLenExpr(), varToGoFieldName, field));
        } else {
            ParseUtil.append(body, "{}:={}\n", varNameLen, listLen);
        }
        final String varNameArr = goFieldName + "_arr";
        if (listLen == 0) {
            ParseUtil.append(body, "{}:=make([]*{},{},{})\n", varNameArr, goFieldTypeName, varNameLen, varNameLen);
        } else {
            ParseUtil.append(body, "{}:=[{}]*{}\n", varNameArr, varNameLen, goFieldTypeName);
        }
        final String varNameParseContext;
        if (ParseUtil.checkChildrenHasAnno_F_customize(goFieldType)) {
            varNameParseContext = context.getVarNameParseContext();
        } else {
            varNameParseContext = "nil";
        }
        ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
        final String valCode;
        valCode = ParseUtil.format("To_{}({},{})", goFieldTypeName, varNameByteBuf, varNameParseContext);
        ParseUtil.append(body, "{}[i]={}\n", varNameArr, valCode);
        ParseUtil.append(body, "}\n", varNameLen);
        ParseUtil.append(body, "{}.{}={}\n", varNameInstance, goFieldName, varNameArr);

    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final Class<?> goFieldType;
        if (fieldType.isArray()) {
            goFieldType = fieldType.getComponentType();
        } else {
            goFieldType = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
        }
        final F_bean_list anno = field.getAnnotation(F_bean_list.class);
        final Class<? extends F_bean_list> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String varNameDeParseContext;
        if (ParseUtil.checkChildrenHasAnno_F_customize(goFieldType)) {
            varNameDeParseContext = context.getVarNameDeParseContext();
        } else {
            varNameDeParseContext = "nil";
        }
        final int listLen = anno.listLen();
        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, varNameInstance, goFieldName);
        final Integer byteLen = GoParseUtil.unsafePointerStruct_byteLen.get(goFieldTypeName);
        ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
        ParseUtil.append(body, "{}[i].Write({},{})\n", varNameArr, varNameByteBuf, varNameDeParseContext);
        ParseUtil.append(body, "}\n");
    }

}

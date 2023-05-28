package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.util.ParseUtil;

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
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final Class<?> fieldType = field.getType();
        final Class<?> goFieldType;
        if (fieldType.isArray()) {
            goFieldType = fieldType.getComponentType();
        } else {
            goFieldType = ((Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]));
        }
        final String goFieldTypeName = GoUtil.toFirstUpperCase(goFieldType.getSimpleName());
        goField.goFieldTypeName = goFieldTypeName;
        ParseUtil.append(body, "{} []{}\n", goFieldName, goFieldTypeName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean_list anno = field.getAnnotation(F_bean_list.class);
        final Class<? extends F_bean_list> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final boolean passBitBuf = anno.passBitBuf();
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
        final String varNameVal = goFieldName + "_v";
        ParseUtil.append(body, "{}:=make([]{},{})\n", varNameArr, goFieldTypeName, varNameLen);
        final String varNameBitBufReader;
        if (passBitBuf) {
            varNameBitBufReader = context.getVarNameBitBuf_reader();
        } else {
            varNameBitBufReader = null;
        }
        ParseUtil.append(body, "for i:=0;i<{};i++{\n", varNameLen);
        if (GoUtil.bitStructSet.contains(goFieldTypeName)) {
            if (passBitBuf) {
                ParseUtil.append(body, "{},err:=To{}({},{})\n", varNameVal, goFieldTypeName, GoFieldBuilder.varNameByteBuf, varNameBitBufReader);
            } else {
                ParseUtil.append(body, "{},err:=To{}({},nil)\n", varNameVal, goFieldTypeName, GoFieldBuilder.varNameByteBuf);
            }
        } else {
            ParseUtil.append(body, "{},err:=To{}({})\n", varNameVal, goFieldTypeName, GoFieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(body, "if err!=nil{\n");
        ParseUtil.append(body, "return nil,err\n");
        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "{}[i]=*{}\n", varNameArr, varNameVal);
        ParseUtil.append(body, "}\n", varNameLen);
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, varNameArr);

    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean_list anno = field.getAnnotation(F_bean_list.class);
        final Class<? extends F_bean_list> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;

        final String varNameArr = goFieldName + "_arr";
        ParseUtil.append(body, "{}:={}.{}\n", varNameArr, GoFieldBuilder.varNameInstance, goFieldName);
        final String varNameBitBufWriter;
        if (passBitBuf) {
            varNameBitBufWriter = context.getVarNameBitBuf_writer();
        } else {
            varNameBitBufWriter = null;
        }
        ParseUtil.append(body, "for i:=0;i<len({});i++{\n", varNameArr);
        if (GoUtil.bitStructSet.contains(goFieldTypeName)) {
            if (passBitBuf) {
                ParseUtil.append(body, "{}[i].Write({},{})\n", varNameArr, GoFieldBuilder.varNameByteBuf, varNameBitBufWriter);
            } else {
                ParseUtil.append(body, "{}[i].Write({},nil)\n", varNameArr, GoFieldBuilder.varNameByteBuf);
            }
        } else {
            ParseUtil.append(body, "{}[i].Write({})\n", varNameArr, GoFieldBuilder.varNameByteBuf);
        }
        ParseUtil.append(body, "}\n");
    }

}

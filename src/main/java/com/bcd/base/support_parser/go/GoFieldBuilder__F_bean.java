package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_bean;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_bean extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = field.getType().getSimpleName();
        goField.goFieldTypeName = goFieldTypeName;
        ParseUtil.append(body, "{} *{}\n", goFieldName, goFieldTypeName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String varNameVal = goFieldName + "_v";
        if (GoUtil.bitStructSet.contains(goFieldTypeName)) {
            if (passBitBuf) {
                final String varNameBitBufReader = context.getVarNameBitBuf_reader();
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
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, varNameVal);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final String varNameVal = goFieldName + "_v";
        ParseUtil.append(body, "{}:={}.{}\n", varNameVal, GoFieldBuilder.varNameInstance, goFieldName);
        if (GoUtil.bitStructSet.contains(goFieldTypeName)) {
            if (passBitBuf) {
                final String varNameBitBufWriter = context.getVarNameBitBuf_writer();
                ParseUtil.append(body, "{}.Write({},{})\n", varNameVal, GoFieldBuilder.varNameByteBuf, varNameBitBufWriter);
            } else {
                ParseUtil.append(body, "{}.Write({},nil)\n", varNameVal, GoFieldBuilder.varNameByteBuf);
            }
        } else {
            ParseUtil.append(body, "{}.Write({})\n", varNameVal, GoFieldBuilder.varNameByteBuf);
        }
    }

}

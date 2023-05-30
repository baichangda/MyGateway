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
        final String varNameParseContext = context.getVarNameParseContext();
        if (passBitBuf) {
            final String varNameBitBuf = context.getVarNameBitBuf_reader();
            ParseUtil.append(body, "{}.BitBuf_reader={}\n", varNameParseContext, varNameBitBuf);
        }
        final String valCode = ParseUtil.format("To{}({},{})", goFieldTypeName, GoFieldBuilder.varNameByteBuf, varNameParseContext);
        ParseUtil.append(body, "{}.{}={}\n", GoFieldBuilder.varNameInstance, goFieldName, valCode);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bean anno = field.getAnnotation(F_bean.class);
        final Class<? extends F_bean> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean passBitBuf = anno.passBitBuf();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String varNameParseContext = context.getVarNameDeParseContext();
        final String valCode = ParseUtil.format("{}.{}", GoFieldBuilder.varNameInstance, goFieldName);
        if (passBitBuf) {
            final String varNameBitBuf = context.getVarNameBitBuf_writer();
            ParseUtil.append(body, "{}.BitBuf_writer={}\n", varNameParseContext, varNameBitBuf);
        }
        ParseUtil.append(body, "{}.Write({},{})\n", valCode, GoFieldBuilder.varNameByteBuf, varNameParseContext);
    }

}

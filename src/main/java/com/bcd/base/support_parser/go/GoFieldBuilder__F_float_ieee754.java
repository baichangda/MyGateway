package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_float_ieee754;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.FloatType_ieee754;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class GoFieldBuilder__F_float_ieee754 extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754 anno = field.getAnnotation(F_float_ieee754.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String jsonExt = goField.jsonExt;
        final Class<? extends F_float_ieee754> annoClass = anno.getClass();
        final StringBuilder body = context.structBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final FloatType_ieee754 type = anno.type();
        final String goFieldTypeName;
        switch (type) {
            case Float32 -> {
                goFieldTypeName = "float32";
            }
            case Float64 -> {
                goFieldTypeName = "float64";
            }
            default -> {
                goFieldTypeName = null;
            }
        }
        goField.goFieldTypeName = goFieldTypeName;
        ParseUtil.append(body, "{} {} {}\n", goFieldName, goFieldTypeName,jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754 anno = field.getAnnotation(F_float_ieee754.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_float_ieee754> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String varNameReadVal = goFieldName + "_v";
        final String valExpr = anno.valExpr();
        final FloatType_ieee754 type = anno.type();
        ParseUtil.append(body, "{}:={}.Read_{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goFieldTypeName, bigEndian));
        String valCode = varNameReadVal;
        valCode = ParseUtil.replaceValExprToCode(valExpr, valCode);
        ParseUtil.append(body, "{}.{}={}\n\n", GoFieldBuilder.varNameInstance, goFieldName, valCode);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_float_ieee754 anno = field.getAnnotation(F_float_ieee754.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String goFieldTypeName = goField.goFieldTypeName;
        final Class<? extends F_float_ieee754> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String valExpr = anno.valExpr();
        String valCode = GoFieldBuilder.varNameInstance + "." + goFieldName;
        if (!valExpr.isEmpty()) {
            valCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(valExpr), valCode);
        }
        ParseUtil.append(body, "{}.Write_{}({})\n", GoFieldBuilder.varNameByteBuf, GoParseUtil.wrapTypeNameFunc(goFieldTypeName, bigEndian), valCode);
    }

}

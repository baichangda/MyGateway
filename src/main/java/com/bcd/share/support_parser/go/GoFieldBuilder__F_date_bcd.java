package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_date_bcd;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_date_bcd extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final StringBuilder body = context.structBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final String jsonExt = goField.jsonExt;
        ParseUtil.append(body, "{} time.Time {}\n", goFieldName, jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date_bcd anno = field.getAnnotation(F_date_bcd.class);
        final Class<? extends F_date_bcd> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int baseYear = anno.baseYear();
        final String zoneId = anno.zoneId();
        final String varNameLocation = context.getVarNameLocation(zoneId);
        ParseUtil.append(body, "{}.{}=parse.ReadBcd_date({},{},{})\n",
                GoFieldBuilder.varNameInstance, goFieldName,
                varNameByteBuf, baseYear,varNameLocation);
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date_bcd anno = field.getAnnotation(F_date_bcd.class);
        final Class<? extends F_date_bcd> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int baseYear = anno.baseYear();
        final String zoneId = anno.zoneId();
        final String varNameLocation = context.getVarNameLocation(zoneId);
        final String valCode=GoFieldBuilder.varNameInstance+"."+goFieldName;
        ParseUtil.append(body, "{}.{}=parse.WriteBcd_date({},{},{},{})\n",
                GoFieldBuilder.varNameInstance, goFieldName,
                varNameByteBuf,valCode, baseYear,varNameLocation);

    }

}

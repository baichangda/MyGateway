package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_date_ts;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_date_ts extends GoFieldBuilder {
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
        final F_date_ts anno = field.getAnnotation(F_date_ts.class);
        final Class<? extends F_date_ts> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int mode = anno.mode();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String bigEndianSuffix = bigEndian ? "" : "_le";
        switch (mode) {
            case 1 -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{}:={}.Read_int64{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
                ParseUtil.append(body, "{}.{}=time.UnixMilli({})\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case 2 -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{}:={}.Read_int64{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
                ParseUtil.append(body, "{}.{}=time.UnixMilli({}*1000)\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case 3 -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{}:={}.Read_int32{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({})*1000)\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case 4 -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{}:={}.Read_float64{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({}))\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case 5 -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{}:={}.Read_float64{}()\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({}*1000))\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
        }

    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date_ts anno = field.getAnnotation(F_date_ts.class);
        final Class<? extends F_date_ts> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int mode = anno.mode();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String varNameVal = goFieldName + "_v";
        final String bigEndianSuffix = bigEndian ? "" : "_le";
        ParseUtil.append(body, "{}:={}.{}\n", varNameVal, GoFieldBuilder.varNameInstance, goFieldName);
        switch (mode) {
            case 1 -> {
                ParseUtil.append(body, "{}.Write_int64{}({}.UnixMilli())\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
            }
            case 2 -> {
                ParseUtil.append(body, "{}.Write_int64{}({}.UnixMilli()/1000)\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
            }
            case 3 -> {
                ParseUtil.append(body, "{}.Write_int32{}(int32({}.UnixMilli()/1000))\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
            }
            case 4 -> {
                ParseUtil.append(body, "{}.Write_float64{}(float64({}.UnixMilli()))\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
            }
            case 5 -> {
                ParseUtil.append(body, "{}.Write_float64{}(float64({}.UnixMilli())/1000)\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
            }
        }

    }

}

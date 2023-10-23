package com.bcd.share.support_parser.builder;

import com.bcd.share.support_parser.anno.F_date_bytes_7;
import com.bcd.share.support_parser.anno.F_date_ts;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FieldBuilder__F_date_ts extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date_ts anno = context.field.getAnnotation(F_date_ts.class);
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        //先转换为毫秒
        switch (anno.mode()) {
            case 1 -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                ParseUtil.append(body, "final long {}={}.{}();\n", varNameLongField, varNameByteBuf, readFuncName);
            }
            case 2 -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                ParseUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, varNameByteBuf, readFuncName);
            }
            case 3 -> {
                final String readFuncName = bigEndian ? "readUnsignedInt" : "readUnsignedIntLE";
                ParseUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, varNameByteBuf, readFuncName);
            }
            case 4 -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                ParseUtil.append(body, "final long {}=(long){}.{}();\n", varNameLongField, varNameByteBuf, readFuncName);
            }
            case 5 -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                ParseUtil.append(body, "final long {}=(long)({}.{}()*1000);\n", varNameLongField, varNameByteBuf, readFuncName);
            }
        }
        //根据字段类型格式化
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            final String dateClassName = Date.class.getName();
            ParseUtil.append(body, "{}.{}=new {}({});\n", varNameInstance, field.getName(), dateClassName, varNameLongField);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameLongField);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "{}.{}=(int)({}/1000);\n", varNameInstance, field.getName(), varNameLongField);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameStringZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.stringZoneId());
            final String dateTimeFormatterVarName = ParseUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameStringZoneId);
            ParseUtil.append(body, "{}.{}={}.ofInstant({}.ofEpochMilli({}),{}).format({});\n",
                    varNameInstance,
                    field.getName(),
                    zoneDateTimeClassName,
                    Instant.class.getName(),
                    varNameLongField,
                    varNameStringZoneId,
                    dateTimeFormatterVarName);
        } else {
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date_ts.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date_ts anno = context.field.getAnnotation(F_date_ts.class);
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = varNameInstance + "." + field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        //根据字段类型获取long
        if (Date.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={}.getTime();\n", varNameLongField, valCode);
        } else if (long.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}={};\n", varNameLongField, valCode);
        } else if (int.class.isAssignableFrom(fieldTypeClass)) {
            ParseUtil.append(body, "final long {}=(long)({})*1000L;\n", varNameLongField, valCode);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            final String varNameStringZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.stringZoneId());
            final String dateTimeFormatterVarName = ParseUtil.defineClassVar(context, DateTimeFormatter.class, "{}.ofPattern(\"{}\").withZone({})", DateTimeFormatter.class.getName(), anno.stringFormat(), varNameStringZoneId);
            ParseUtil.append(body, "final long {}={}.parse({},{}).toInstant().toEpochMilli();\n", varNameLongField, zoneDateTimeClassName, valCode, dateTimeFormatterVarName);
        } else {
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date_ts.class);
        }

        //先转换为毫秒
        switch (anno.mode()) {
            case 1 -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                ParseUtil.append(body, "{}.{}({});\n", varNameByteBuf, writeFuncName, varNameLongField);
            }
            case 2 -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                ParseUtil.append(body, "{}.{}({}/1000L);\n", varNameByteBuf, writeFuncName, varNameLongField);
            }
            case 3 -> {
                final String writeFuncName = bigEndian ? "writeInt" : "writeIntLE";
                ParseUtil.append(body, "{}.{}((int)({}/1000L));\n", varNameByteBuf, writeFuncName, varNameLongField);
            }
            case 4 -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                ParseUtil.append(body, "{}.{}((double)({}));\n", varNameByteBuf, writeFuncName, varNameLongField);
            }
            case 5 -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                ParseUtil.append(body, "{}.{}((double){}/1000d);\n", varNameByteBuf, writeFuncName, varNameLongField);
            }
        }
    }

    @Override
    public Class<F_date_ts> annoClass() {
        return F_date_ts.class;
    }
}

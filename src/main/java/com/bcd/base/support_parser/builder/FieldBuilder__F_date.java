package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FieldBuilder__F_date extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        //先转换为毫秒
        switch (anno.mode()) {
            case bytes_yyMMddHHmmss -> {
                ParseUtil.append(body, "final long {}={}.of({}+{}.readUnsignedByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                        varNameLongField, zoneDateTimeClassName, anno.baseYear()
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, varNameZoneId);
            }
            case bytes_yyyyMMddHHmmss -> {
                final String readFuncName = bigEndian ? "readUnsignedShort" : "readUnsignedShortLE";
                ParseUtil.append(body, "final long {}={}.of({}.{}(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                        varNameLongField, zoneDateTimeClassName
                        , FieldBuilder.varNameByteBuf, readFuncName, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf
                        , FieldBuilder.varNameByteBuf, FieldBuilder.varNameByteBuf, varNameZoneId);
            }
            case uint64_millisecond -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                ParseUtil.append(body, "final long {}={}.{}();\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case uint64_second -> {
                final String readFuncName = bigEndian ? "readLong" : "readLongLE";
                ParseUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case uint32_second -> {
                final String readFuncName = bigEndian ? "readUnsignedInt" : "readUnsignedIntLE";
                ParseUtil.append(body, "final long {}={}.{}()*1000;\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case float64_millisecond -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                ParseUtil.append(body, "final long {}=(long){}.{}();\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
            }
            case float64_second -> {
                final String readFuncName = bigEndian ? "readDouble" : "readDoubleLE";
                ParseUtil.append(body, "final long {}=(long)({}.{}()*1000);\n", varNameLongField, FieldBuilder.varNameByteBuf, readFuncName);
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
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date anno = context.field.getAnnotation(F_date.class);
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = FieldBuilder.varNameInstance + "." + field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
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
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date.class);
        }

        //先转换为毫秒
        switch (anno.mode()) {
            case bytes_yyMMddHHmmss -> {
                final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
                ParseUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                        zoneDateTimeClassName,
                        varNameZoneDateTimeField,
                        zoneDateTimeClassName,
                        Instant.class.getName(),
                        varNameLongField,
                        varNameZoneId);
                ParseUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getYear()-{}),(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                        FieldBuilder.varNameByteBuf,
                        varNameZoneDateTimeField,
                        anno.baseYear(),
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField);
            }
            case bytes_yyyyMMddHHmmss -> {
                final String writeFuncName = bigEndian ? "writeShort" : "writeShortLE";
                final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
                ParseUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                        zoneDateTimeClassName,
                        varNameZoneDateTimeField,
                        zoneDateTimeClassName,
                        Instant.class.getName(),
                        varNameLongField,
                        varNameZoneId);
                ParseUtil.append(body, "{}.{}((short){}.getYear());\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameZoneDateTimeField);
                ParseUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                        FieldBuilder.varNameByteBuf,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField,
                        varNameZoneDateTimeField);
            }
            case uint64_millisecond -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                ParseUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case uint64_second -> {
                final String writeFuncName = bigEndian ? "writeLong" : "writeLongLE";
                ParseUtil.append(body, "{}.{}({}/1000L);\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case uint32_second -> {
                final String writeFuncName = bigEndian ? "writeInt" : "writeIntLE";
                ParseUtil.append(body, "{}.{}((int)({}/1000L));\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case float64_millisecond -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                ParseUtil.append(body, "{}.{}((double)({}));\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
            case float64_second -> {
                final String writeFuncName = bigEndian ? "writeDouble" : "writeDoubleLE";
                ParseUtil.append(body, "{}.{}((double){}/1000d);\n", FieldBuilder.varNameByteBuf, writeFuncName, varNameLongField);
            }
        }
    }
}

package com.bcd.share.support_parser.builder;

import com.bcd.share.support_parser.anno.F_date_bytes_6;
import com.bcd.share.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FieldBuilder__F_date_bytes_6 extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date_bytes_6 anno = context.field.getAnnotation(F_date_bytes_6.class);
        final Field field = context.field;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final Class<?> fieldTypeClass = field.getType();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        //先转换为毫秒
        ParseUtil.append(body, "final long {}={}.of({}+{}.readUnsignedByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),{}.readByte(),0,{}).toInstant().toEpochMilli();\n",
                varNameLongField, zoneDateTimeClassName, anno.baseYear()
                , varNameByteBuf, varNameByteBuf, varNameByteBuf, varNameByteBuf
                , varNameByteBuf, varNameByteBuf, varNameZoneId);

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
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date_bytes_6.class);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final F_date_bytes_6 anno = context.field.getAnnotation(F_date_bytes_6.class);
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String valCode = varNameInstance + "." + field.getName();
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameZoneId = ParseUtil.defineClassVar(context, ZoneId.class, "{}.of(\"{}\")", ZoneId.class.getName(), anno.zoneId());
        final String varNameLongField = varNameField + "_long";
        final String zoneDateTimeClassName = ZonedDateTime.class.getName();
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
            ParseUtil.notSupport_fieldType(context.clazz, field, F_date_bytes_6.class);
        }


        final String varNameZoneDateTimeField = varNameField + "zoneDateTime";
        ParseUtil.append(body, "{} {}={}.ofInstant({}.ofEpochMilli({}),{});\n",
                zoneDateTimeClassName,
                varNameZoneDateTimeField,
                zoneDateTimeClassName,
                Instant.class.getName(),
                varNameLongField,
                varNameZoneId);
        ParseUtil.append(body, "{}.writeBytes(new byte[]{(byte)({}.getYear()-{}),(byte)({}.getMonthValue()),(byte)({}.getDayOfMonth()),(byte)({}.getHour()),(byte)({}.getMinute()),(byte)({}.getSecond())});\n",
                varNameByteBuf,
                varNameZoneDateTimeField,
                anno.baseYear(),
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField,
                varNameZoneDateTimeField);
    }

    public static long readBcd_6(ByteBuf byteBuf, ZoneId zoneId, int baseYear) {
        byte b1 = byteBuf.readByte();
        byte b2 = byteBuf.readByte();
        byte b3 = byteBuf.readByte();
        byte b4 = byteBuf.readByte();
        byte b5 = byteBuf.readByte();
        byte b6 = byteBuf.readByte();
        int year = ((b1 >> 4) & 0x0f) * 10 + (b1 & 0x0f);
        int month = ((b2 >> 4) & 0x0f) * 10 + (b2 & 0x0f);
        int day = ((b3 >> 4) & 0x0f) * 10 + (b3 & 0x0f);
        int hour = ((b4 >> 4) & 0x0f) * 10 + (b4 & 0x0f);
        int minute = ((b5 >> 4) & 0x0f) * 10 + (b5 & 0x0f);
        int second = ((b6 >> 4) & 0x0f) * 10 + (b6 & 0x0f);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(baseYear + year, month, day, hour, minute, second, 0, zoneId);
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static void writeBcd_6(ByteBuf byteBuf, long ts, ZoneId zoneId, int baseYear) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), zoneId);
        int year = zonedDateTime.getYear() - baseYear;
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        int hour = zonedDateTime.getHour();
        int minute = zonedDateTime.getMinute();
        int second = zonedDateTime.getSecond();
        byte b1 = (byte) (((year / 10) << 4) | (year % 10));
        byte b2 = (byte) (((month / 10) << 4) | (month % 10));
        byte b3 = (byte) (((day / 10) << 4) | (day % 10));
        byte b4 = (byte) (((hour / 10) << 4) | (hour % 10));
        byte b5 = (byte) (((minute / 10) << 4) | (minute % 10));
        byte b6 = (byte) (((second / 10) << 4) | (second % 10));
        byteBuf.writeBytes(new byte[]{b1, b2, b3, b4, b5, b6});
    }
}

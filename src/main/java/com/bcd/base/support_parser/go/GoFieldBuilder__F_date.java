package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.DateMode;
import com.bcd.base.support_parser.anno.F_date;
import com.bcd.base.support_parser.anno.F_string;
import com.bcd.base.support_parser.anno.StringAppendMode;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GoFieldBuilder__F_date extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
        final StringBuilder body = context.structBody;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        ParseUtil.append(body, "{} time.Time\n", goFieldName);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date anno = field.getAnnotation(F_date.class);
        final Class<? extends F_date> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int baseYear = anno.baseYear();
        final DateMode mode = anno.mode();
        final String zoneId = anno.zoneId();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String varNameLocation = goFieldName + "_location";
        if (mode == DateMode.Bytes_yyMMddHHmmss || mode == DateMode.Bytes_yyyyMMddHHmmss) {
            ParseUtil.append(body, "{},err:=time.LoadLocation(\"{}\")", varNameLocation, zoneId);
            ParseUtil.append(body, "if err!=nil{\n");
            ParseUtil.append(body, "return nil,err\n");
            ParseUtil.append(body, "}\n");
        }

        switch (mode) {
            case Bytes_yyMMddHHmmss -> {
                final String varNameBytes = goFieldName + "_bytes";
                ParseUtil.append(body, "{},err:={}.Read_bytes(6)\n", varNameBytes, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.Date({}+int({}[0]),time.Month(int({}[1])),int({}[2]),int({}[3]),int({}[4]),int({}[5]),0,{})\n",
                        GoFieldBuilder.varNameInstance, goFieldName,
                        baseYear, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes,
                        varNameLocation);
            }
            case Bytes_yyyyMMddHHmmss -> {
                final String varNameYear = goFieldName + "_year";
                final String varNameBytes = goFieldName + "_bytes";
                ParseUtil.append(body, "{},err:={}.Read_uint16({})\n", varNameYear, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{},err:={}.Read_bytes(5)\n", varNameBytes, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.Date({},{}[0],{}[1],{}[2],{}[3],{}[4],0,{})\n",
                        GoFieldBuilder.varNameInstance, goFieldName,
                        varNameYear, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes,
                        varNameLocation);
            }
            case Uint64_millisecond -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{},err:={}.Read_int64({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.UnixMilli({})\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case Uint64_second -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{},err:={}.Read_int64({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.UnixMilli({}*1000)\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case Uint32_second -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{},err:={}.Read_int32({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({})*1000)\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case Float64_millisecond -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{},err:={}.Read_float64({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({}))\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
            case Float64_second -> {
                final String varNameReadVal = goFieldName + "_v";
                ParseUtil.append(body, "{},err:={}.Read_float64({})\n", varNameReadVal, GoFieldBuilder.varNameByteBuf, bigEndian);
                ParseUtil.append(body, "if err!=nil{\n");
                ParseUtil.append(body, "return nil,err\n");
                ParseUtil.append(body, "}\n");
                ParseUtil.append(body, "{}.{}=time.UnixMilli(int64({}*1000))\n", GoFieldBuilder.varNameInstance, goFieldName, varNameReadVal);
            }
        }

    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date anno = field.getAnnotation(F_date.class);
        final Class<? extends F_date> annoClass = anno.getClass();
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final int baseYear = anno.baseYear();
        final DateMode mode = anno.mode();
        final String zoneId = anno.zoneId();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.pkg_byteOrder);
        final String varNameVal = goFieldName + "_v";
        ParseUtil.append(body, "{}:={}.{}\n", varNameVal, GoFieldBuilder.varNameInstance, goFieldName);
        switch (mode) {
            case Bytes_yyMMddHHmmss -> {
                ParseUtil.append(body, "{}.Write_bytes([]byte{byte({}.Year()-2000),byte({}.Month()),byte({}.Day()),byte({}.Hour()),byte({}.Minute()),byte({}.Second())})\n",
                        GoFieldBuilder.varNameByteBuf,
                        varNameVal, varNameVal, varNameVal, varNameVal, varNameVal, varNameVal
                );
            }
            case Bytes_yyyyMMddHHmmss -> {
                ParseUtil.append(body, "{}.Write_uint16(uint16({}.Year()),{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
                ParseUtil.append(body, "{}.Write_bytes([]byte{byte({}.Month()),byte({}.Day()),byte({}.Hour()),byte({}.Minute()),byte({}.Second())})\n",
                        GoFieldBuilder.varNameByteBuf,
                        varNameVal, varNameVal, varNameVal, varNameVal, varNameVal, varNameVal
                );
            }
            case Uint64_millisecond -> {
                ParseUtil.append(body, "{}.Write_int64({}.UnixMilli(),{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
            }
            case Uint64_second -> {
                ParseUtil.append(body, "{}.Write_int64({}.UnixMilli()/1000,{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
            }
            case Uint32_second -> {
                ParseUtil.append(body, "{}.Write_int32(int32({}.UnixMilli()/1000),{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
            }
            case Float64_millisecond -> {
                ParseUtil.append(body, "{}.Write_float64(float64({}.UnixMilli()),{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
            }
            case Float64_second -> {
                ParseUtil.append(body, "{}.Write_float64(float64({}.UnixMilli())/1000,{})\n", GoFieldBuilder.varNameByteBuf, varNameVal, bigEndian);
            }
        }

    }

}

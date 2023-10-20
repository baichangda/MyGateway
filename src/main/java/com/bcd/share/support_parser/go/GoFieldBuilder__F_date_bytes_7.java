package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_date_bytes_7;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_date_bytes_7 extends GoFieldBuilder {
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
        final F_date_bytes_7 anno = field.getAnnotation(F_date_bytes_7.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final String zoneId = anno.zoneId();
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String varNameLocation = context.getVarNameLocation(zoneId);

        final String bigEndianSuffix = bigEndian ? "" : "_le";

        final String varNameYear = goFieldName + "_year";
        final String varNameBytes = goFieldName + "_bytes";
        ParseUtil.append(body, "{}:={}.Read_uint16{}()\n", varNameYear, GoFieldBuilder.varNameByteBuf, bigEndianSuffix);
        ParseUtil.append(body, "{},err:={}.Read_slice_uint8(5)\n", varNameBytes, GoFieldBuilder.varNameByteBuf);
        ParseUtil.append(body, "if err!=nil{\n");
        ParseUtil.append(body, "return nil,err\n");
        ParseUtil.append(body, "}\n");
        ParseUtil.append(body, "{}.{}=time.Date({},{}[0],{}[1],{}[2],{}[3],{}[4],0,{})\n",
                GoFieldBuilder.varNameInstance, goFieldName,
                varNameYear, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes,
                varNameLocation);


    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date_bytes_7 anno = field.getAnnotation(F_date_bytes_7.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.byteOrder);
        final String varNameVal = goFieldName + "_v";
        final String bigEndianSuffix = bigEndian ? "" : "_le";
        ParseUtil.append(body, "{}:={}.{}\n", varNameVal, GoFieldBuilder.varNameInstance, goFieldName);
        ParseUtil.append(body, "{}.Write_uint16{}(uint16({}.Year()))\n", GoFieldBuilder.varNameByteBuf, bigEndianSuffix, varNameVal);
        ParseUtil.append(body, "{}.Write_slice_uint8([]byte{byte({}.Month()),byte({}.Day()),byte({}.Hour()),byte({}.Minute()),byte({}.Second())})\n",
                GoFieldBuilder.varNameByteBuf,
                varNameVal, varNameVal, varNameVal, varNameVal, varNameVal, varNameVal
        );
    }

}

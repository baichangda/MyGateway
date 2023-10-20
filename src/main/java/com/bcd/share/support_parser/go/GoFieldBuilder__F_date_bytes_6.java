package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_date_bytes_6;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_date_bytes_6 extends GoFieldBuilder {
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
        final F_date_bytes_6 anno = field.getAnnotation(F_date_bytes_6.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.parseBody;
        final int baseYear = anno.baseYear();
        final String zoneId = anno.zoneId();
        final String varNameLocation = context.getVarNameLocation(zoneId);
        final String varNameBytes = goFieldName + "_bytes";
        ParseUtil.append(body, "{}:={}.Read_slice_uint8(6)\n", varNameBytes, GoFieldBuilder.varNameByteBuf);
        ParseUtil.append(body, "{}.{}=time.Date({}+int({}[0]),time.Month(int({}[1])),int({}[2]),int({}[3]),int({}[4]),int({}[5]),0,{})\n",
                GoFieldBuilder.varNameInstance, goFieldName,
                baseYear, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes, varNameBytes,
                varNameLocation);

    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_date_bytes_6 anno = field.getAnnotation(F_date_bytes_6.class);
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final StringBuilder body = context.deParseBody;
        final String varNameVal = goFieldName + "_v";
        ParseUtil.append(body, "{}:={}.{}\n", varNameVal, GoFieldBuilder.varNameInstance, goFieldName);
        ParseUtil.append(body, "{}.Write_slice_uint8([]byte{byte({}.Year()-2000),byte({}.Month()),byte({}.Day()),byte({}.Hour()),byte({}.Minute()),byte({}.Second())})\n",
                GoFieldBuilder.varNameByteBuf,
                varNameVal, varNameVal, varNameVal, varNameVal, varNameVal, varNameVal
        );

    }

}

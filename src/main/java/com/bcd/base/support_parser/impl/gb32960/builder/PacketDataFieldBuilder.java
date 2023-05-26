package com.bcd.base.support_parser.impl.gb32960.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.builder.BuilderContext;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.impl.gb32960.data.*;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;


public class PacketDataFieldBuilder extends FieldBuilder {

    @Override
    public void buildParse(final BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldTypeClassName = field.getType().getName();
        final String parserClassName = Parser.class.getName();

        ParseUtil.append(body, "{} {}=null;\n", fieldTypeClassName, varNameField);

        final String parseContextVarName = context.getProcessContextVarName();

        ParseUtil.append(body, "switch ({}.flag){\n", varNameInstance);
        ParseUtil.append(body, "case 1:{\n", varNameInstance);
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleLoginData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 2:{\n");
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleRunData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 3:{\n");
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleSupplementData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 4:{\n");
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, VehicleLogoutData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 5:{\n");
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, PlatformLoginData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 6:{\n");
        ParseUtil.append(body, "{}= {}.parse({}.class, {},{});\n", varNameField, parserClassName, PlatformLogoutData.class.getName(), FieldBuilder.varNameByteBuf, parseContextVarName);
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 7:{\n");
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "case 8:{\n");
        ParseUtil.append(body, "break;\n");
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "{}.{}={};\n", varNameInstance, field.getName(), varNameField);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        super.buildDeParse(context);
    }
}

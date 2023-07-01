package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class GoUtil {
    public final static Map<String, String> zoneId_varNameLocation = new HashMap<>();

    public final static GoFieldBuilder__F_num fieldBuilder__f_num = new GoFieldBuilder__F_num();
    public final static GoFieldBuilder__F_num_array fieldBuilder__f_num_array = new GoFieldBuilder__F_num_array();
    public final static GoFieldBuilder__F_bit_num fieldBuilder__f_bit_num = new GoFieldBuilder__F_bit_num();
    public final static GoFieldBuilder__F_bit_num_array fieldBuilder__f_bit_num_array = new GoFieldBuilder__F_bit_num_array();
    public final static GoFieldBuilder__F_string fieldBuilder__f_string = new GoFieldBuilder__F_string();
    public final static GoFieldBuilder__F_skip fieldBuilder__f_skip = new GoFieldBuilder__F_skip();
    public final static GoFieldBuilder__F_date fieldBuilder__f_date = new GoFieldBuilder__F_date();
    public final static GoFieldBuilder__F_bit_skip fieldBuilder__f_bit_skip = new GoFieldBuilder__F_bit_skip();
    public final static GoFieldBuilder__F_bean fieldBuilder__f_bean = new GoFieldBuilder__F_bean();
    public final static GoFieldBuilder__F_bean_list fieldBuilder__f_bean_list = new GoFieldBuilder__F_bean_list();
    public final static GoFieldBuilder__F_customize fieldBuilder__f_customize = new GoFieldBuilder__F_customize();


    public final static void toSourceCode(String pkg, ByteOrder byteOrder, BitOrder bitOrder, String goFilePath) {
        final StringBuilder body = new StringBuilder();
        final StringBuilder customizeBody = new StringBuilder();
        final List<Class<?>> classes;
        try {
            classes = ClassUtil.getClasses(pkg);
        } catch (IOException | ClassNotFoundException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        for (Class<?> clazz : classes) {
            GoParseUtil.initUnsafePointerStructSet(clazz, byteOrder);
            GoParseUtil.initNoPointerStructSet(clazz);
        }

        final StringBuilder globalBody = new StringBuilder();
        for (Class<?> clazz : classes) {
            final List<Field> parseFields = ParseUtil.getParseFields(clazz);
            if (parseFields.isEmpty()) {
                continue;
            }
            final boolean hasFieldSkipModeReserved = GoParseUtil.hasFieldSkipModeReserved(parseFields);
            final StringBuilder structBody = new StringBuilder();
            final StringBuilder parseBody = new StringBuilder();
            final StringBuilder deParseBody = new StringBuilder();
            final GoBuildContext context = new GoBuildContext(clazz, byteOrder, bitOrder, globalBody, structBody, parseBody, deParseBody, customizeBody);
            final String goStructName = context.goStructName;
            ParseUtil.append(structBody, "type {} struct{\n", goStructName);
            if (GoParseUtil.noPointerStructSet.contains(goStructName)) {
                ParseUtil.append(parseBody, "func To_{}({} *parse.ByteBuf,{} *parse.ParseContext) {}{\n",
                        goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName);
                ParseUtil.append(deParseBody, "func({} {})Write({} *parse.ByteBuf,{} *parse.ParseContext){\n",
                        GoFieldBuilder.varNameInstance, goStructName,
                        GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            } else {
                ParseUtil.append(parseBody, "func To_{}({} *parse.ByteBuf,{} *parse.ParseContext) *{}{\n",
                        goStructName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext, goStructName);
                ParseUtil.append(deParseBody, "func(_{} *{})Write({} *parse.ByteBuf,{} *parse.ParseContext){\n",
                        GoFieldBuilder.varNameInstance, goStructName,
                        GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
                ParseUtil.append(deParseBody, "{}:= *_{}\n", GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameInstance);
            }


            ParseUtil.append(parseBody, "{}:={}{}\n", GoFieldBuilder.varNameInstance, goStructName);
            if (hasFieldSkipModeReserved) {
                ParseUtil.append(parseBody, "{}:={}.ReaderIndex()\n", GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
                ParseUtil.append(deParseBody, "{}:={}.WriterIndex()\n", GoFieldBuilder.varNameStartIndex, GoFieldBuilder.varNameByteBuf);
            }


            for (int i = 0; i < parseFields.size(); i++) {
                final Field field = parseFields.get(i);
                context.setField(field);
                GoParseUtil.bitEndWhenBitField(parseFields, i, context);
                GoFieldBuilder goFieldBuilder = null;
                if (field.isAnnotationPresent(F_num.class)) {
                    goFieldBuilder = fieldBuilder__f_num;
                } else if (field.isAnnotationPresent(F_num_array.class)) {
                    goFieldBuilder = fieldBuilder__f_num_array;
                } else if (field.isAnnotationPresent(F_bit_num.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_num;
                } else if (field.isAnnotationPresent(F_bit_num_array.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_num_array;
                } else if (field.isAnnotationPresent(F_string.class)) {
                    goFieldBuilder = fieldBuilder__f_string;
                } else if (field.isAnnotationPresent(F_skip.class)) {
                    goFieldBuilder = fieldBuilder__f_skip;
                } else if (field.isAnnotationPresent(F_date.class)) {
                    goFieldBuilder = fieldBuilder__f_date;
                } else if (field.isAnnotationPresent(F_bit_skip.class)) {
                    goFieldBuilder = fieldBuilder__f_bit_skip;
                } else if (field.isAnnotationPresent(F_bean.class)) {
                    goFieldBuilder = fieldBuilder__f_bean;
                } else if (field.isAnnotationPresent(F_bean_list.class)) {
                    goFieldBuilder = fieldBuilder__f_bean_list;
                } else if (field.isAnnotationPresent(F_customize.class)) {
                    goFieldBuilder = fieldBuilder__f_customize;
                }
                if (goFieldBuilder != null) {
                    goFieldBuilder.buildStruct(context);

                    goFieldBuilder.buildParse(context);
                    goFieldBuilder.buildDeParse(context);
                }
            }


            ParseUtil.append(structBody, "}\n");
            if (GoParseUtil.noPointerStructSet.contains(goStructName)) {
                ParseUtil.append(parseBody, "return {}\n", GoFieldBuilder.varNameInstance);
            } else {
                ParseUtil.append(parseBody, "return &{}\n", GoFieldBuilder.varNameInstance);
            }
            ParseUtil.append(parseBody, "}\n");
            ParseUtil.append(deParseBody, "}\n");

            body.append(structBody);
            body.append("\n");

            //针对只包含基本类型、可以用unsafe直接转换的结构体、不使用通用的解析方法
            final Integer structByteLen = GoParseUtil.unsafePointerStruct_byteLen.get(goStructName);
            if (structByteLen == -1) {
                body.append(parseBody);
                body.append("\n");
                body.append(deParseBody);
                body.append("\n");
            } else {
                GoParseUtil.appendUnsafeStructFunc(body,goStructName,structByteLen);
            }
        }

        body.insert(0, globalBody);

        final Path path = Paths.get(goFilePath);
        final Path parent = path.getParent();
        final Path goPkg = parent.getName(parent.getNameCount() - 1);
        try {
            Files.createDirectories(parent);
            try (final BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bw.write("package " + goPkg);
                bw.newLine();
                bw.write(body.toString());
                bw.flush();
            }
        } catch (IOException ex) {
            throw BaseRuntimeException.getException(ex);
        }
        final Path customizePath = Paths.get(parent + "/customize.go");
        if (!customizeBody.isEmpty() && !Files.exists(customizePath)) {
            try (final BufferedWriter bw = Files.newBufferedWriter(customizePath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                bw.write("package " + goPkg);
                bw.newLine();
                bw.write(customizeBody.toString());
                bw.flush();
            } catch (IOException ex) {
                throw BaseRuntimeException.getException(ex);
            }
        }

    }


    public static void main(String[] args) {
//        final String s = "com.bcd.base.support_parser.impl.gb32960.data";
//        toSourceCode(s, ByteOrder.bigEndian, BitOrder.bigEndian, "D:/work/bcd/MyGateway_go/support_parse/gb32960/java.go");
//        final String s = "com.bcd.base.support_parser.impl.immotors.ep33.data";
//        toSourceCode(s, ByteOrder.bigEndian, BitOrder.bigEndian, "D:/work/bcd/MyGateway_go/support_parse/immotors/ep33/java.go");
        final String s = "com.bcd.base.support_parser.impl.immotors.ep33.data";
        toSourceCode(s, ByteOrder.bigEndian, BitOrder.bigEndian, "D:/work/bcd/MyGateway_go/support_parse/immotors/ep33_noValExpr/java.go");
//        final String s = "com.bcd.base.support_parser.impl.icd.data";
//        toSourceCode(s, ByteOrder.bigEndian, BitOrder.bigEndian, "D:/work/bcd/MyGateway_go/support_parse/icd/java.go");
    }
}

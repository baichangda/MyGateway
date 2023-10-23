package com.bcd.share.support_parser.builder;


import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.anno.F_string_bcd;
import com.bcd.share.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

public class FieldBuilder__F_string_bcd extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        final Class<? extends F_string_bcd> annoClass = anno.getClass();
        if (fieldType != String.class) {
            ParseUtil.notSupport_type(context.clazz, field, annoClass);
        }
        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_noAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_lowAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.{}={}.read_highAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, lenRes);
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final F_string_bcd anno = field.getAnnotation(F_string_bcd.class);
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);
        final String varNameFieldVal = varNameField + "_val";

        ParseUtil.append(body, "final String {};\n", varNameFieldVal);
        ParseUtil.append(body, "if({}==null){\n", valCode);
        ParseUtil.append(body, "{}=\"\";\n", varNameFieldVal);
        ParseUtil.append(body, "}else{\n", valCode);
        ParseUtil.append(body, "{}={};\n", varNameFieldVal, valCode);
        ParseUtil.append(body, "}\n", valCode);

        final String lenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                lenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            lenRes = anno.len() + "";
        }

        switch (anno.appendMode()) {
            case noAppend -> {
                ParseUtil.append(body, "{}.write_noAppend({},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal);
            }
            case lowAddressAppend -> {
                ParseUtil.append(body, "{}.write_lowAddressAppend({},{},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
            }
            case highAddressAppend -> {
                ParseUtil.append(body, "{}.write_highAddressAppend({},{},{});\n", FieldBuilder__F_string_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
            }
        }
    }


    public static String read_noAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            byte b1 = (byte) ((b >> 4) & 0x0F);
            byte b2 = (byte) (b & 0x0F);
            sb.append(b1).append(b2);
        }
        return sb.toString();
    }

    public static String read_lowAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        StringBuilder sb = new StringBuilder();
        boolean ok = false;
        for (int i = 0; i < len; i++) {
            byte b = bytes[i];
            byte b1 = (byte) ((b >> 4) & 0x0F);
            byte b2 = (byte) (b & 0x0F);
            if (ok) {
                sb.append(b1).append(b2);
            } else {
                if (b1 == 0) {
                    if (b2 != 0) {
                        sb.append(b2);
                        ok = true;
                    }
                } else {
                    sb.append(b1).append(b2);
                    ok = true;
                }
            }
        }
        return sb.toString();
    }

    public static String read_highAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            byte b1 = (byte) ((b >> 4) & 0x0F);
            if (b1 == 0) {
                break;
            } else {
                byte b2 = (byte) (b & 0x0F);
                if (b2 == 0) {
                    sb.append(b1);
                    break;
                } else {
                    sb.append(b1).append(b2);
                }
            }
        }
        return sb.toString();
    }

    public static void write_noAppend(ByteBuf byteBuf, String s) {
        char[] charArray = s.toCharArray();
        int actualLen = charArray.length >> 1;
        byte[] res = new byte[actualLen];
        for (int i = 0; i < actualLen; i++) {
            byte b1 = (byte) (Character.getNumericValue(charArray[i << 1]) << 4);
            byte b2 = (byte) Character.getNumericValue(charArray[(i << 1) + 1]);
            res[i] = (byte) (b1 | b2);
        }
        byteBuf.writeBytes(res);
    }

    public static void write_lowAddressAppend(ByteBuf byteBuf, String s, int len) {
        byte[] res = new byte[len];
        char[] charArray = s.toCharArray();
        int actualLen = charArray.length >> 1;
        for (int i = actualLen - 1, j = len - 1; i >= 0; i--, j--) {
            byte b1 = (byte) (Character.getNumericValue(charArray[i << 1]) << 4);
            byte b2 = (byte) Character.getNumericValue(charArray[(i << 1) + 1]);
            res[j] = (byte) (b1 | b2);
        }
        byteBuf.writeBytes(res);
    }

    public static void write_highAddressAppend(ByteBuf byteBuf, String s, int len) {
        byte[] res = new byte[len];
        char[] charArray = s.toCharArray();
        int actualLen = charArray.length >> 1;
        for (int i = 0; i < actualLen; i++) {
            byte b1 = (byte) (Character.getNumericValue(charArray[i << 1]) << 4);
            byte b2 = (byte) Character.getNumericValue(charArray[(i << 1) + 1]);
            res[i] = (byte) (b1 | b2);
        }
        byteBuf.writeBytes(res);
    }

    @Override
    public Class<F_string_bcd> annoClass() {
        return F_string_bcd.class;
    }
}

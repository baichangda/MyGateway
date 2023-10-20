package com.bcd.share.support_parser.builder;


import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.anno.F_bcd;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class FieldBuilder__F_bcd extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_bcd anno = field.getAnnotation(F_bcd.class);
        final Class<? extends F_bcd> annoClass = anno.getClass();
        if (fieldType != String.class && fieldType != byte[].class) {
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

        if (fieldType == byte[].class) {
            ParseUtil.append(body, "{}.{}={}.readBytes({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_bcd.class.getName(), varNameByteBuf, lenRes);
        } else {
            switch (anno.appendMode()) {
                case noAppend -> {
                    ParseUtil.append(body, "{}.{}={}.readString_noAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_bcd.class.getName(), varNameByteBuf, lenRes);
                }
                case lowAddressAppend -> {
                    ParseUtil.append(body, "{}.{}={}.readString_lowAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_bcd.class.getName(), varNameByteBuf, lenRes);
                }
                case highAddressAppend -> {
                    ParseUtil.append(body, "{}.{}={}.readString_highAddressAppend({},{});\n", varNameInstance, field.getName(), FieldBuilder__F_bcd.class.getName(), varNameByteBuf, lenRes);
                }
            }
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final F_string anno = field.getAnnotation(F_string.class);
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
        if (fieldType == byte[].class) {
            ParseUtil.append(body, "{}.writeBytes({},{});\n", FieldBuilder__F_bcd.class.getName(), varNameByteBuf, varNameFieldVal);
        } else {
            switch (anno.appendMode()) {
                case noAppend -> {
                    ParseUtil.append(body, "{}.writeString_noAppend({},{});\n", FieldBuilder__F_bcd.class.getName(), varNameByteBuf, varNameFieldVal);
                }
                case lowAddressAppend -> {
                    ParseUtil.append(body, "{}.writeString_lowAddressAppend({},{},{});\n", FieldBuilder__F_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
                }
                case highAddressAppend -> {
                    ParseUtil.append(body, "{}.writeString_highAddressAppend({},{},{});\n", FieldBuilder__F_bcd.class.getName(), varNameByteBuf, varNameFieldVal, lenRes);
                }
            }
        }
    }


    public static byte[] readBytes(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        byte[] res = new byte[len << 1];
        int i = 0;
        for (byte b : bytes) {
            res[i] = (byte) ((b >> 4) & 0x0F);
            res[i + 1] = (byte) (b & 0x0F);
            i += 2;
        }
        return res;
    }

    public static String readString_noAppend(ByteBuf byteBuf, int len) {
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

    public static String readString_lowAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; i--) {
            byte b = bytes[i];
            byte b2 = (byte) (b & 0x0F);
            if (b2 == 0) {
                break;
            } else {
                byte b1 = (byte) ((b >> 4) & 0x0F);
                if (b1 == 0) {
                    sb.append(b2);
                    break;
                } else {
                    sb.append(b2).append(b1);
                }
            }
        }
        return sb.reverse().toString();
    }

    public static String readString_highAddressAppend(ByteBuf byteBuf, int len) {
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

    public static void writeBytes(ByteBuf byteBuf, byte[] bytes) {
        byte[] res = new byte[bytes.length >> 1];
        for (int i = 0; i < bytes.length; i += 2) {
            res[i >> 1] = (byte) ((bytes[i] << 4) | bytes[i]);
        }
        byteBuf.writeBytes(res);
    }

    public static void writeString_noAppend(ByteBuf byteBuf, String s) {
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

    public static void writeString_lowAddressAppend(ByteBuf byteBuf, String s, int len) {
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

    public static void writeString_highAddressAppend(ByteBuf byteBuf, String s, int len) {
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
}

package com.bcd.base.support_parser.builder;


import com.bcd.base.exception.BaseRuntimeException;
import com.bcd.base.support_parser.anno.F_string_bcd;
import com.bcd.base.support_parser.util.BcdUtil;
import com.bcd.base.support_parser.util.ParseUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

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
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string_bcd.class.getName());
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
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_string_bcd.class.getName());
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
        char[] chars = new char[len << 1];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            System.arraycopy(BcdUtil.BCD_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
        }
        return new String(chars);
    }

    public static String read_lowAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        char[] chars = new char[len << 1];
        int startIndex = -1;
        for (int i = 0; i < len; i++) {
            byte b = bytes[i];
            if (startIndex == -1) {
                if (b != 0) {
                    if (((b >> 4) & 0x0F) == 0) {
                        startIndex = (i << 1) + 1;
                    } else {
                        startIndex = i << 1;
                    }
                    System.arraycopy(BcdUtil.BCD_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
                }
            } else {
                System.arraycopy(BcdUtil.BCD_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
            }
        }
        return new String(chars, startIndex, chars.length - startIndex);
    }

    public static String read_highAddressAppend(ByteBuf byteBuf, int len) {
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        char[] chars = new char[len << 1];
        int endIndex = -1;
        for (int i = len - 1; i >= 0; i--) {
            byte b = bytes[i];
            if (endIndex == -1) {
                if (b != 0) {
                    if ((b & 0x0F) == 0) {
                        endIndex = i << 1;
                    } else {
                        endIndex = (i << 1) + 1;
                    }
                    System.arraycopy(BcdUtil.BCD_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
                }
            } else {
                System.arraycopy(BcdUtil.BCD_DUMP_TABLE, (b & 0xff) << 1, chars, i << 1, 2);
            }
        }
        return new String(chars, 0, endIndex+1);
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
        int sLen = charArray.length;
        int actualLen = sLen >> 1;
        int offset = sLen & 1;
        for (int i = len - 1, j = actualLen - 1; j >= 0; i--, j--) {
            int index = (j << 1) + offset;
            byte b1 = (byte) (Character.getNumericValue(charArray[index]) << 4);
            byte b2 = (byte) Character.getNumericValue(charArray[index + 1]);
            res[i] = (byte) (b1 | b2);
        }
        if (offset == 1) {
            res[len - actualLen - 1] = (byte) Character.getNumericValue(charArray[0]);
        }
        byteBuf.writeBytes(res);
    }


    public static void write_highAddressAppend(ByteBuf byteBuf, String s, int len) {
        byte[] res = new byte[len];
        char[] charArray = s.toCharArray();
        int sLen = charArray.length;
        int actualLen = charArray.length >> 1;
        for (int i = 0; i < actualLen; i++) {
            byte b1 = (byte) (Character.getNumericValue(charArray[i << 1]) << 4);
            byte b2 = (byte) Character.getNumericValue(charArray[(i << 1) + 1]);
            res[i] = (byte) (b1 | b2);
        }
        if ((sLen & 1) == 1) {
            res[actualLen] = (byte) (Character.getNumericValue(charArray[actualLen << 1]) << 4);
        }
        byteBuf.writeBytes(res);
    }

    public static void main(String[] args) {
        ByteBuf buffer1 = Unpooled.buffer();
//        write_lowAddressAppend(buffer1, "17299841738", 10);
        write_highAddressAppend(buffer1, "17299841738", 10);
        System.out.println(ByteBufUtil.hexDump(buffer1));
        ByteBuf buffer2 = Unpooled.buffer();
//        write_lowAddressAppend(buffer2, "117299841738", 10);
        write_highAddressAppend(buffer2, "117299841738", 10);
        System.out.println(ByteBufUtil.hexDump(buffer2));
    }
}

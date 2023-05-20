package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_integer_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;
import java.util.Arrays;

public class FieldBuilder__F_integer_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final String arrayElementType = fieldTypeClass.componentType().getName();
        final Class<F_integer_array> annoClass = F_integer_array.class;
        final F_integer_array anno = context.field.getAnnotation(annoClass);
        switch (arrayElementType) {
            case "byte", "short", "int", "long" -> {
            }
            default -> {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
        }
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                arrLenRes = JavassistUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            arrLenRes = anno.len() + "";
        }


        final int singleLen = anno.singleLen();
        final String valExpr = anno.valExpr();
        final StringBuilder body = context.body;
        final String varNameField = JavassistUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        if (anno.bit() == 0) {
            if (byte[].class.isAssignableFrom(fieldTypeClass)) {
                JavassistUtil.append(body, "final byte[] {}=new byte[{}];\n", arrVarName, arrLenRes);
                switch (singleLen) {
                    case 1: {
                        if (valExpr.isEmpty()) {
                            JavassistUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
                        } else {
                            final String varNameArrayElement = varNameField + "_arrEle";
                            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
                            JavassistUtil.append(body, "byte {}={}.readByte();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                            JavassistUtil.append(body, "{}[i]=(byte)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
                            JavassistUtil.append(body, "}\n");
                        }
                        break;
                    }
                    default: {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
            } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
                JavassistUtil.append(body, "final short[] {}=new short[{}];\n", arrVarName, arrLenRes);
                final String varNameArrayElement = varNameField + "_arrEle";
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
                switch (singleLen) {
                    case 1 -> {
                        JavassistUtil.append(body, "final short {}={}.readUnsignedByte();\n", varNameArrayElement, FieldBuilder.varNameByteBuf);
                    }
                    case 2 -> {
                        final String funcName = bigEndian ? "readShort" : "readShortLE";
                        JavassistUtil.append(body, "final short {}={}.{}();\n", varNameArrayElement, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}[i]=(short)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
                JavassistUtil.append(body, "}\n");
            } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
                JavassistUtil.append(body, "final int[] {}=new int[{}];\n", arrVarName, arrLenRes);
                final String varNameArrayElement = varNameField + "_arrEle";
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
                switch (singleLen) {
                    case 2 -> {
                        final String funcName = bigEndian ? "readUnsignedShort" : "readUnsignedShortLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameArrayElement, FieldBuilder.varNameByteBuf, funcName);
                    }
                    case 4 -> {
                        final String funcName = bigEndian ? "readInt" : "readIntLE";
                        JavassistUtil.append(body, "final int {}={}.{}();\n", varNameArrayElement, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}[i]=(int)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
                JavassistUtil.append(body, "}\n");
            } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
                JavassistUtil.append(body, "final long[] {}=new long[{}];\n", arrVarName, arrLenRes);
                final String varNameArrayElement = varNameField + "_arrEle";
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
                switch (singleLen) {
                    case 4 -> {
                        final String funcName = bigEndian ? "readUnsignedInt" : "readUnsignedIntLE";
                        JavassistUtil.append(body, "final long {}={}.{}();\n", varNameArrayElement, FieldBuilder.varNameByteBuf, funcName);
                    }
                    case 8 -> {
                        final String funcName = bigEndian ? "readLong" : "readLongLE";
                        JavassistUtil.append(body, "final long {}={}.{}();\n", varNameArrayElement, FieldBuilder.varNameByteBuf, funcName);
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "{}[i]=(long)({});\n", arrVarName, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
                JavassistUtil.append(body, "}\n");

            } else {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
        } else {
            final String varNameArrayElement = varNameField + "_arrEle";
            final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

            JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, arrLenRes);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            JavassistUtil.append(body, "final {} {}=({}){}.read({},{});\n", arrayElementType, varNameArrayElement, arrayElementType, varNameBitBuf, anno.bit(), anno.bitUnsigned());
            JavassistUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementType, JavassistUtil.replaceValExprToCode(valExpr, varNameArrayElement));
            JavassistUtil.append(body, "}\n");

            if (context.bitEndWhenBitField_process) {
                JavassistUtil.append(body, "{}.finish();\n", context.varNameBitBuf);
            }
        }
        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_integer_array> annoClass = F_integer_array.class;
        final F_integer_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        final String valCode = varNameInstance + "." + fieldName;
        final String varNameField = JavassistUtil.getFieldVarName(context);

        if (anno.bit() == 0) {
            JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
            if (byte[].class.isAssignableFrom(fieldTypeClass)) {
                if (singleLen == 1) {
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, valCode);
                    } else {
                        String varNameFieldArr = varNameField + "_arr";
                        String varNameFieldRes = varNameField + "_res";
                        JavassistUtil.append(body, "final byte[] {}={};\n", varNameFieldArr, valCode);
                        JavassistUtil.append(body, "final byte[] {}=new byte[{}.length];\n", varNameFieldRes, varNameFieldArr);
                        JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldRes);
                        JavassistUtil.append(body, "{}[i]={};\n", varNameFieldRes, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        JavassistUtil.append(body, "}\n");
                        JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameFieldRes);
                    }
                } else {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            } else if (short[].class.isAssignableFrom(fieldTypeClass)) {
                String varNameFieldArr = varNameField + "_arr";
                JavassistUtil.append(body, "final short[] {}={};\n", varNameFieldArr, valCode);
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                switch (singleLen) {
                    case 1 -> {
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.writeByte((byte){});\n", FieldBuilder.varNameByteBuf, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.writeByte((byte)({}));\n", FieldBuilder.varNameByteBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    case 2 -> {
                        final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "}\n");
            } else if (int[].class.isAssignableFrom(fieldTypeClass)) {
                String varNameFieldArr = varNameField + "_arr";
                JavassistUtil.append(body, "final int[] {}={};\n", varNameFieldArr, valCode);
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                switch (singleLen) {
                    case 2 -> {
                        final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.{}((short){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.{}((short)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    case 4 -> {
                        final String funcName = bigEndian ? "writeInt" : "writeIntLE";
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "}\n");
            } else if (long[].class.isAssignableFrom(fieldTypeClass)) {
                String varNameFieldArr = varNameField + "_arr";
                JavassistUtil.append(body, "final long[] {}={};\n", varNameFieldArr, valCode);
                JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                switch (singleLen) {
                    case 4 -> {
                        final String funcName = bigEndian ? "writeInt" : "writeIntLE";
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.{}((int){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    case 8 -> {
                        final String funcName = bigEndian ? "writeLong" : "writeLongLE";
                        if (anno.valExpr().isEmpty()) {
                            JavassistUtil.append(body, "{}.{}({});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                        } else {
                            JavassistUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                        }
                    }
                    default -> {
                        JavassistUtil.notSupport_singleLen(field, annoClass);
                    }
                }
                JavassistUtil.append(body, "}\n");
            } else {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
            JavassistUtil.append(body, "}\n");
        } else {
            final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);
            JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
            final String varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final {}[] {}={};\n", fieldTypeClass.componentType(), varNameFieldArr, valCode);
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            if (anno.valExpr().isEmpty()) {
                JavassistUtil.append(body, "{}.write((long)({}),{},{});\n", varNameBitBuf, varNameFieldArr + "[i]", anno.bit(), anno.bitUnsigned());
            } else {
                JavassistUtil.append(body, "{}.write((long)({}),{},{});\n", varNameBitBuf, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"), anno.bit(), anno.bitUnsigned());
            }
            JavassistUtil.append(body, "}\n");

            if (context.bitEndWhenBitField_deProcess) {
                JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
            }
            JavassistUtil.append(body, "}\n");
        }

    }
}

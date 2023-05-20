package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_float_integer_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;
import com.bcd.base.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_float_integer_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_float_integer_array> annoClass = F_float_integer_array.class;
        final F_float_integer_array anno = context.field.getAnnotation(annoClass);
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

        final Class<?> fieldTypeClass = field.getType();
        final String arrayElementType=fieldTypeClass.componentType().getName();

        switch (arrayElementType) {
            case "float", "double" -> {
            }
            default -> {
                JavassistUtil.notSupport_fieldType(field, annoClass);
            }
        }

        final StringBuilder body = context.body;

        final String varNameField = JavassistUtil.getFieldVarName(context);
        final String arrVarName = varNameField + "_arr";
        JavassistUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementType, arrVarName, arrayElementType, arrLenRes);
        final String varNameArrayElement = varNameField + "_arrEle";
        if (anno.bit() == 0) {
            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            switch (anno.singleLen()) {
                case 1: {
                    final String readFuncName = anno.unsigned() ? "readUnsignedByte" : "readByte";
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf, readFuncName);
                    break;
                }
                case 2: {
                    final String funcName;
                    if (bigEndian) {
                        funcName = anno.unsigned() ? "readUnsignedShort" : "readShort";
                    } else {
                        funcName = anno.unsigned() ? "readUnsignedShortLE" : "readShortLE";
                    }
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                case 4: {
                    final String funcName;
                    if (bigEndian) {
                        funcName = anno.unsigned() ? "readUnsignedInt" : "readInt";
                    } else {
                        funcName = anno.unsigned() ? "readUnsignedIntLE" : "readIntLE";
                    }
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
                case 8: {
                    final String funcName = bigEndian ? "readLong" : "readLongLE";
                    JavassistUtil.append(body, "final {} {}=({}){}.{}();\n", arrayElementType, varNameArrayElement, arrayElementType, FieldBuilder.varNameByteBuf, funcName);
                    break;
                }
            }
        } else {
            final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

            JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            JavassistUtil.append(body, "final {} {}=({}){}.read({},{});\n", arrayElementType, varNameArrayElement, arrayElementType, varNameBitBuf, anno.bit(), anno.bitUnsigned());
        }
        if (anno.valPrecision() == -1) {
            JavassistUtil.append(body, "{}[i]={};\n", arrVarName, JavassistUtil.replaceValExprToCode(anno.valExpr(), JavassistUtil.format("(({}){})", arrayElementType, varNameArrayElement)));
        } else {
            JavassistUtil.append(body, "{}[i]=({}){}.format((double){},{});\n", arrVarName, arrayElementType, JavassistUtil.class.getName(), JavassistUtil.replaceValExprToCode(anno.valExpr(), JavassistUtil.format("(({}){})", arrayElementType, varNameArrayElement)), anno.valPrecision());
        }

        body.append("}\n");

        if (context.bitEndWhenBitField_process) {
            JavassistUtil.append(body, "{}.finish();\n", context.varNameBitBuf);
        }
        JavassistUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_float_integer_array> annoClass = F_float_integer_array.class;
        final F_float_integer_array anno = context.field.getAnnotation(annoClass);
        final boolean bigEndian = JavassistUtil.bigEndian(anno.order(), context.clazz);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final StringBuilder body = context.body;
        final String fieldName = field.getName();
        final String valCode = FieldBuilder.varNameInstance + "." + fieldName;
        final String varNameField = JavassistUtil.getFieldVarName(context);

        final String varNameFieldArr;
        if (float[].class.isAssignableFrom(fieldTypeClass)) {
            varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final float[] {}={};\n", varNameFieldArr, valCode);
        } else if (double[].class.isAssignableFrom(fieldTypeClass)) {
            varNameFieldArr = varNameField + "_arr";
            JavassistUtil.append(body, "final double[] {}={};\n", varNameFieldArr, valCode);
        } else {
            JavassistUtil.notSupport_fieldType(field, annoClass);
            varNameFieldArr = "";
        }
        if (anno.bit() == 0) {
            JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);
            switch (singleLen) {
                case 1 -> {
                    String varNameFieldRes = varNameField + "_res";
                    JavassistUtil.append(body, "final byte[] {}=new byte[{}.length];\n", varNameFieldRes, varNameFieldArr);
                    JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldRes);
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}[i]=(int){};\n", varNameFieldRes, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}[i]=(int)({});\n", varNameFieldRes, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    JavassistUtil.append(body, "}\n");
                    JavassistUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, varNameFieldRes);
                }
                case 2 -> {
                    final String funcName = bigEndian ? "writeShort" : "writeShortLE";
                    JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}((int){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    JavassistUtil.append(body, "}\n");
                }
                case 4 -> {
                    final String funcName = bigEndian ? "writeInt" : "writeIntLE";
                    JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}((int){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((int)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    JavassistUtil.append(body, "}\n");
                }
                case 8 -> {
                    final String funcName = bigEndian ? "writeLong" : "writeLongLE";
                    JavassistUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
                    if (anno.valExpr().isEmpty()) {
                        JavassistUtil.append(body, "{}.{}((long){});\n", FieldBuilder.varNameByteBuf, funcName, varNameFieldArr + "[i]");
                    } else {
                        JavassistUtil.append(body, "{}.{}((long)({}));\n", FieldBuilder.varNameByteBuf, funcName, JavassistUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), varNameFieldArr + "[i]"));
                    }
                    JavassistUtil.append(body, "}\n");
                }
                default -> {
                    JavassistUtil.notSupport_singleLen(field, annoClass);
                }
            }
            JavassistUtil.append(body, "}\n");
        } else {
            final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

            JavassistUtil.append(body, "if({}!=null){\n", FieldBuilder.varNameInstance, valCode);

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

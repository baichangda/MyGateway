package com.bcd.share.support_parser.builder;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.util.ParseUtil;
import com.bcd.share.support_parser.util.RpnUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_num_array extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final Field field = context.field;
        final Class<?> fieldTypeClass = field.getType();
        final Class<?> arrayElementType = fieldTypeClass.componentType();
        final String arrayElementTypeName = arrayElementType.getName();
        final String sourceValTypeName;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);

        switch (arrayElementTypeName) {
            case "byte", "short", "int", "long", "float", "double" -> {
                sourceValTypeName = arrayElementTypeName;
            }
            default -> {
                if (arrayElementType.isEnum()) {
                    sourceValTypeName = "int";
                } else {
                    ParseUtil.notSupport_fieldType(context.clazz, field, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_num_array.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            arrLenRes = String.valueOf(anno.len());
        }


        final NumType singleType = anno.singleType();
        final String singleValExpr = anno.singleValExpr();
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
        final int singleSkip = anno.singleSkip();
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        //优化处理 byte[]数组解析
        if (byte[].class.isAssignableFrom(fieldTypeClass) && singleType == NumType.int8 && singleValExpr.isEmpty() && singleSkip == 0) {
            ParseUtil.append(body, "{}.readBytes({});\n", FieldBuilder.varNameByteBuf, arrVarName);
        } else {
            String funcName;
            switch (singleType) {
                case uint8 -> {
                    if (sourceValTypeName.equals("byte")) {
                        funcName = "readByte";
                    } else {
                        funcName = "readUnsignedByte";
                    }
                }
                case int8 -> {
                    funcName = "readByte";
                }
                case uint16 -> {
                    if (sourceValTypeName.equals("short")) {
                        funcName = "readShort";
                    } else {
                        funcName = "readUnsignedShort";
                    }
                }
                case int16 -> {
                    funcName = "readShort";
                }
                case uint32 -> {
                    if (sourceValTypeName.equals("int")) {
                        funcName = "readInt";
                    } else {
                        funcName = "readUnsignedInt";
                    }
                }
                case int32 -> {
                    funcName = "readInt";
                }
                case uint64, int64 -> {
                    funcName = "readLong";
                }
                case float32 -> {
                    funcName = "readFloat";
                }
                case float64 -> {
                    funcName = "readDouble";
                }
                default -> {
                    ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                    funcName = null;
                }
            }
            if (!bigEndian && singleType != NumType.uint8 && singleType != NumType.int8) {
                funcName += "LE";
            }

            ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
            final String varNameArrayElement = varNameField + "_arrEle";
            ParseUtil.append(body, "final {} {}=({}){}.{}();\n", sourceValTypeName, varNameArrayElement, sourceValTypeName, FieldBuilder.varNameByteBuf, funcName);
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.skipBytes({});\n", varNameByteBuf, singleSkip);
            }
            //表达式运算
            final String valCode = ParseUtil.replaceValExprToCode(singleValExpr, varNameArrayElement);
            if (arrayElementType.isEnum()) {
                ParseUtil.append(body, "{}[i]={}.fromInteger((int)({}));\n", arrVarName, arrayElementTypeName, valCode);
            } else {
                ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, valCode);
            }
            ParseUtil.append(body, "}\n");
        }

        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);
        final Class<?> fieldTypeClass = field.getType();
        final NumType singleType = anno.singleType();
        final int singleSkip = anno.singleSkip();
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        final String singleValExpr = anno.singleValExpr();
        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);

        ParseUtil.append(body, "if({}!=null){\n", valCode);

        if (byte[].class.isAssignableFrom(fieldTypeClass) && singleType == NumType.int8 && singleValExpr.isEmpty() && singleSkip == 0) {
            ParseUtil.append(body, "{}.writeBytes({});\n", FieldBuilder.varNameByteBuf, valCode);
        } else {
            final Class<?> arrayElementType = fieldTypeClass.componentType();
            final boolean isFloat = arrayElementType == float.class || arrayElementType == double.class;
            final String arrayElementTypeName = arrayElementType.getName();

            String varNameFieldArr = varNameField + "_arr";
            ParseUtil.append(body, "final {}[] {}={};\n", arrayElementTypeName, varNameFieldArr, valCode);
            ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", varNameFieldArr);
            String varNameFieldArrEle = varNameField + "_arrEle";
            ParseUtil.append(body, "final {} {}={}[i];\n", arrayElementTypeName, varNameFieldArrEle, varNameFieldArr);
            String arrEleValCode = varNameFieldArrEle;
            if (arrayElementType.isEnum()) {
                arrEleValCode = ParseUtil.format("({}).toInteger()", arrEleValCode);
            }
            if (!singleValExpr.isEmpty()) {
                if (isFloat) {
                    arrEleValCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(singleValExpr), arrEleValCode);
                } else {
                    arrEleValCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(singleValExpr), arrEleValCode);
                }
            }

            final boolean bigEndian = ParseUtil.bigEndian(anno.singleOrder(), context.byteOrder);
            final String writeCastTypeName;
            String funcName;
            if (!bigEndian && singleType != NumType.uint8 && singleType != NumType.int8) {
                funcName = "LE";
            } else {
                funcName = "";
            }
            switch (singleType) {
                case uint8, int8 -> {
                    writeCastTypeName = "int";
                    funcName = "writeByte" + funcName;
                }
                case uint16, int16 -> {
                    writeCastTypeName = "int";
                    funcName = "writeShort" + funcName;
                }
                case uint32, int32 -> {
                    writeCastTypeName = "int";
                    funcName = "writeInt" + funcName;
                }
                case uint64, int64 -> {
                    writeCastTypeName = "long";
                    funcName = "writeLong" + funcName;
                }
                case float32 -> {
                    writeCastTypeName = "float";
                    funcName = "writeFloat" + funcName;
                }
                case float64 -> {
                    writeCastTypeName = "double";
                    funcName = "writeDouble" + funcName;
                }
                default -> {
                    ParseUtil.notSupport_numType(context.clazz, field, annoClass);
                    writeCastTypeName = null;
                }
            }
            ParseUtil.append(body, "{}.{}(({})({}));\n", FieldBuilder.varNameByteBuf, funcName, writeCastTypeName, arrEleValCode);
            if (singleSkip > 0) {
                ParseUtil.append(body, "{}.writeZero({});\n", varNameByteBuf, singleSkip);
            }
            ParseUtil.append(body, "}\n");
        }
        ParseUtil.append(body, "}\n");
    }

    @Override
    public Class<F_num_array> annoClass() {
        return F_num_array.class;
    }
}

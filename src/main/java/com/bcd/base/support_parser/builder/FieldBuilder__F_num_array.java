package com.bcd.base.support_parser.builder;

import com.bcd.base.support_parser.anno.F_num_array;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.ParseUtil;
import com.bcd.base.support_parser.util.RpnUtil;

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
                    ParseUtil.notSupport_fieldType(field, annoClass);
                    sourceValTypeName = null;
                }
            }
        }

        final String arrLenRes;
        if (anno.len() == 0) {
            if (anno.lenExpr().isEmpty()) {
                throw BaseRuntimeException.getException("class[{}] field[{}] anno[] must have len or lenExpr", field.getDeclaringClass().getName(), field.getName(), F_skip.class.getName());
            } else {
                arrLenRes = ParseUtil.replaceLenExprToCode(anno.lenExpr(), context.varToFieldName, field);
            }
        } else {
            arrLenRes = anno.len() + "";
        }


        final int singleLen = anno.singleLen();
        final String valExpr = anno.valExpr();
        final StringBuilder body = context.body;
        final String varNameField = ParseUtil.getFieldVarName(context);
        String arrVarName = varNameField + "_arr";
        final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
        final boolean unsigned = anno.unsigned();
        final int singleSkip = anno.singleSkip();
        ParseUtil.append(body, "final {}[] {}=new {}[{}];\n", arrayElementTypeName, arrVarName, arrayElementTypeName, arrLenRes);
        String funcName;
        switch (singleLen) {
            case 1 -> {
                funcName = unsigned ? "readUnsignedByte" : "readByte";
            }
            case 2 -> {
                funcName = unsigned ? "readUnsignedShort" : "readShort";
                funcName += (bigEndian ? "" : "LE");
            }
            case 4 -> {
                funcName = unsigned ? "readUnsignedInt" : "readInt";
                funcName += (bigEndian ? "" : "LE");
            }
            case 8 -> {
                funcName = bigEndian ? "readLong" : "readLongLE";
            }
            default -> {
                ParseUtil.notSupport_singleLen(field, annoClass);
                funcName = null;
            }
        }

        ParseUtil.append(body, "for(int i=0;i<{}.length;i++){\n", arrVarName);
        final String varNameArrayElement = varNameField + "_arrEle";
        ParseUtil.append(body, "final {} {}=({}){}.{}();\n", sourceValTypeName, varNameArrayElement, sourceValTypeName, FieldBuilder.varNameByteBuf, funcName);
        if (singleSkip > 0) {
            ParseUtil.append(body, "{}.skipBytes({});\n", varNameByteBuf, singleSkip);
        }
        //表达式运算
        final String valCode = ParseUtil.replaceValExprToCode(valExpr, varNameArrayElement);
        if (arrayElementType.isEnum()) {
            ParseUtil.append(body, "{}[i]={}.fromInteger((int)({}));\n", arrVarName, arrayElementTypeName, valCode);
        } else {
            ParseUtil.append(body, "{}[i]=({})({});\n", arrVarName, arrayElementTypeName, valCode);
        }
        ParseUtil.append(body, "}\n");

        ParseUtil.append(body, "{}.{}={};\n", FieldBuilder.varNameInstance, field.getName(), arrVarName);
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final Field field = context.field;
        final Class<F_num_array> annoClass = F_num_array.class;
        final F_num_array anno = context.field.getAnnotation(annoClass);
        final Class<?> fieldTypeClass = field.getType();
        final int singleLen = anno.singleLen();
        final int singleSkip = anno.singleSkip();
        final StringBuilder body = context.body;
        final String varNameInstance = FieldBuilder.varNameInstance;
        final String fieldName = field.getName();
        String valCode = varNameInstance + "." + fieldName;
        final String varNameField = ParseUtil.getFieldVarName(context);

        ParseUtil.append(body, "if({}!=null){\n", valCode);

        if (byte[].class.isAssignableFrom(fieldTypeClass) && singleLen == 1 && anno.valExpr().isEmpty()) {
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
            if (!anno.valExpr().isEmpty()) {
                if(isFloat){
                    arrEleValCode = ParseUtil.replaceValExprToCode_round(RpnUtil.reverseExpr(anno.valExpr()), arrEleValCode);
                }else{
                    arrEleValCode = ParseUtil.replaceValExprToCode(RpnUtil.reverseExpr(anno.valExpr()), arrEleValCode);
                }
            }

            final boolean bigEndian = ParseUtil.bigEndian(anno.order(), context.clazz);
            final String funcName;
            final String writeCastTypeName;
            switch (singleLen) {
                case 1 -> {
                    funcName = "writeByte";
                    writeCastTypeName = "int";
                }
                case 2 -> {
                    funcName = bigEndian ? "writeShort" : "writeShortLE";
                    writeCastTypeName = "int";
                }
                case 4 -> {
                    funcName = bigEndian ? "writeInt" : "writeIntLE";
                    writeCastTypeName = "int";
                }
                case 8 -> {
                    funcName = bigEndian ? "writeLong" : "writeLongLE";
                    writeCastTypeName = "long";
                }
                default -> {
                    ParseUtil.notSupport_singleLen(field, annoClass);
                    funcName = null;
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
}

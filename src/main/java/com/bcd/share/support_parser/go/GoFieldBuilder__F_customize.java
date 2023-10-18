package com.bcd.share.support_parser.go;

import com.bcd.share.support_parser.anno.F_customize;
import com.bcd.share.support_parser.util.ClassUtil;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class GoFieldBuilder__F_customize extends GoFieldBuilder {

    static Set<Class<?>> customizeClassSet = new HashSet<>();
    static Map<Class<?>, String> javaType_goType = new HashMap<>();

    static {
        javaType_goType.put(byte.class, "int8");
        javaType_goType.put(Byte.class, "int8");
        javaType_goType.put(short.class, "int16");
        javaType_goType.put(Short.class, "int16");
        javaType_goType.put(int.class, "int32");
        javaType_goType.put(Integer.class, "int32");
        javaType_goType.put(long.class, "int64");
        javaType_goType.put(Long.class, "int64");
        javaType_goType.put(float.class, "float32");
        javaType_goType.put(Float.class, "float32");
        javaType_goType.put(double.class, "float64");
        javaType_goType.put(Double.class, "float64");

        javaType_goType.put(byte[].class, "[]int8");
        javaType_goType.put(Byte[].class, "[]int8");
        javaType_goType.put(short[].class, "[]int16");
        javaType_goType.put(Short[].class, "[]int16");
        javaType_goType.put(int[].class, "[]int32");
        javaType_goType.put(Integer[].class, "[]int32");
        javaType_goType.put(long[].class, "[]int64");
        javaType_goType.put(Long[].class, "[]int64");
        javaType_goType.put(float[].class, "[]float32");
        javaType_goType.put(Float[].class, "[]float32");
        javaType_goType.put(double[].class, "[]float64");
        javaType_goType.put(Double[].class, "[]float64");

        javaType_goType.put(String.class, "string");
        javaType_goType.put(Date.class, "time.Time");
    }

    private String getGoFieldType(Class<?> fieldType) {
        if (ClassUtil.isBeanType(fieldType)) {
            return GoParseUtil.toFirstUpperCase(fieldType.getSimpleName());
        } else {
            if (fieldType.isEnum()) {
                return "int";
            } else if (fieldType.isArray()) {
                final String arrEleType = getGoFieldType(fieldType.getComponentType());
                if (arrEleType == null) {
                    return null;
                }
                return "[]" + arrEleType;
            } else {
                return javaType_goType.get(fieldType);
            }
        }
    }


    @Override
    public void buildStruct(GoBuildContext context) {
        final Field field = context.field;
        final StringBuilder body = context.structBody;
        final StringBuilder customizeBody = context.customizeBody;
        final Class<?> clazz = context.clazz;
        final Class<?> fieldType = field.getType();
        final GoField goField = context.goField;
        final String jsonExt = goField.jsonExt;
        final String goFieldName = goField.goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            if (ClassUtil.isBeanType(fieldType)) {
                final List<Field> fields = ClassUtil.getAllFields(fieldType);
                if (!fields.isEmpty()) {
                    ParseUtil.append(customizeBody, "type {} struct{\n", goFieldName);
                    for (Field f : fields) {
                        final Class<?> fType = f.getType();
                        String goFType = null;
                        final String goFName = GoParseUtil.toFirstUpperCase(f.getName());
                        if (List.class.isAssignableFrom(fType)) {
                            final Class<?> actualTypeArgument = (Class<?>) (((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                            final String temp = getGoFieldType(actualTypeArgument);
                            if (temp != null) {
                                goFType = "[]" + temp;
                            }
                        } else {
                            goFType = getGoFieldType(fType);
                        }

                        if (goFType != null) {
                            ParseUtil.append(customizeBody, "{} {}\n", goFName, goFType);
                        }
                    }
                    ParseUtil.append(customizeBody, "}\n\n");
                }
            }

        }
        ParseUtil.append(body, "{} any {}\n", goFieldName, jsonExt);
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final StringBuilder body = context.parseBody;
        final StringBuilder customizeBody = context.customizeBody;
        final Class<?> clazz = context.clazz;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final F_customize f_customize = field.getAnnotation(F_customize.class);
        final String toFunName = "To_" + goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            ParseUtil.append(customizeBody, "func {}({} *parse.ByteBuf,{} *parse.ParseContext,args ...any)any{\n", toFunName, varNameByteBuf, varNameParentParseContext);
            ParseUtil.append(customizeBody, "//todo Read Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
            ParseUtil.append(customizeBody, "return nil\n");
            ParseUtil.append(customizeBody, "}\n\n");
        }
        final String varNameParseContext = context.getVarNameParseContext();
        String processorArgs = f_customize.processorArgs();
        ParseUtil.append(body, "{}.{}={}({},{},{})\n", varNameInstance, goFieldName,
                toFunName, varNameByteBuf, varNameParseContext, processorArgs.isEmpty() ? "" : ("," + processorArgs));
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final Class<?> fieldType = field.getType();
        final StringBuilder body = context.deParseBody;
        final StringBuilder customizeBody = context.customizeBody;
        final Class<?> clazz = context.clazz;
        final GoField goField = context.goField;
        final String goFieldName = goField.goFieldName;
        final F_customize f_customize = field.getAnnotation(F_customize.class);
        final String writeFunName = "Write_" + goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            ParseUtil.append(customizeBody, "func {}({} *parse.ByteBuf,_{} any,{} *parse.ParseContext,args ...any){\n",
                    writeFunName, varNameByteBuf,
                    varNameInstance,
                    varNameParentParseContext);
            ParseUtil.append(customizeBody, "//todo Write Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
            ParseUtil.append(customizeBody, "}\n\n");
        }

        final String varNameParseContext = context.getVarNameDeParseContext();
        String processorArgs = f_customize.processorArgs();
        ParseUtil.append(body, "{}({},{}.{},{},{})\n",
                writeFunName, varNameByteBuf, varNameInstance, goFieldName, varNameParseContext, processorArgs.isEmpty() ? "" : ("," + processorArgs));

        customizeClassSet.add(fieldType);
    }
}
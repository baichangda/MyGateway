package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.F_customize;
import com.bcd.base.support_parser.util.ClassUtil;
import com.bcd.base.support_parser.util.ParseUtil;

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
            return GoUtil.toFirstUpperCase(fieldType.getSimpleName());
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
        final String goFieldName = goField.goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            if (ClassUtil.isBeanType(fieldType)) {
                final List<Field> fields = ClassUtil.getAllFields(fieldType);
                if (!fields.isEmpty()) {
                    ParseUtil.append(customizeBody, "type {} struct{\n", goFieldName);
                    for (Field f : fields) {
                        final Class<?> fType = f.getType();
                        String goFType = null;
                        final String goFName = GoUtil.toFirstUpperCase(f.getName());
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
        ParseUtil.append(body, "{} any\n", goFieldName);
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
        final boolean passBitBuf = f_customize.passBitBuf();
        final String toFunName = "To" + goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            ParseUtil.append(customizeBody, "func {}({} *parse.ByteBuf,{} *parse.ParseContext)any{\n", toFunName, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            if (passBitBuf) {
                ParseUtil.append(customizeBody, "{}:=parse.GetBitBuf_reader({},{})\n"
                        , GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            }
            ParseUtil.append(customizeBody, "//todo Read Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
            ParseUtil.append(customizeBody, "return nil\n");
            ParseUtil.append(customizeBody, "}\n\n");
        }

        final String varNameParseContext = context.getVarNameParseContext();
        if (passBitBuf) {
            final String varNameBitBuf = context.getVarNameBitBuf_reader();
            ParseUtil.append(body, "{}.BitBuf_reader={}\n", varNameParseContext, varNameBitBuf);
        }
        ParseUtil.append(body, "{}.{}={}({},{})\n", GoFieldBuilder.varNameInstance, goFieldName,
                toFunName, GoFieldBuilder.varNameByteBuf, varNameParseContext);
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
        final boolean passBitBuf = f_customize.passBitBuf();
        final String writeFunName = "Write" + goFieldName;
        if (!customizeClassSet.contains(fieldType)) {
            ParseUtil.append(customizeBody, "func {}({} *parse.ByteBuf,_{} any,{} *parse.ParseContext){\n",
                    writeFunName, GoFieldBuilder.varNameByteBuf,
                    GoFieldBuilder.varNameInstance,
                    GoFieldBuilder.varNameParentParseContext);
            if (passBitBuf) {
                ParseUtil.append(customizeBody, "{}:=parse.GetBitBuf_writer({},{})\n"
                        , GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            }
            ParseUtil.append(customizeBody, "//todo Write Implement F_customize[{}#{}]\n\n", clazz.getName(), field.getName());
            ParseUtil.append(customizeBody, "}\n\n");
        }

        final String varNameParseContext = context.getVarNameDeParseContext();
        if (passBitBuf) {
            final String varNameBitBuf = context.getVarNameBitBuf_writer();
            ParseUtil.append(body, "{}.BitBuf_writer={}\n", varNameParseContext, varNameBitBuf);
        }
        ParseUtil.append(body, "{}({},{}.{},{})\n",
                writeFunName, GoFieldBuilder.varNameByteBuf,GoFieldBuilder.varNameInstance, goFieldName, varNameParseContext);

        customizeClassSet.add(fieldType);
    }
}

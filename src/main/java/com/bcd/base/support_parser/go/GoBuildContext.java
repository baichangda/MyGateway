package com.bcd.base.support_parser.go;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GoBuildContext {
    public final Class<?> clazz;
    public final String goStructName;

    public final StringBuilder structBody;
    public final StringBuilder parseFuncBody;
    public final StringBuilder deParseFuncBody;

    public Field field;
    public GoField goField;

    public int fieldIndex;


    /**
     * 当前字段所属class中的变量名称对应字段名称
     */
    public final Map<Character, String> varToGoFieldName = new HashMap<>();

    public GoBuildContext(Class<?> clazz, StringBuilder structBody, StringBuilder parseFuncBody, StringBuilder deParseFuncBody) {
        this.clazz = clazz;
        this.goStructName = GoUtil.toFirstUpperCase(clazz.getSimpleName());
        this.structBody = structBody;
        this.parseFuncBody = parseFuncBody;
        this.deParseFuncBody = deParseFuncBody;
    }

    public void setField(Field field,int index) {
        this.field = field;
        this.goField = new GoField(field);
        this.fieldIndex=index;
    }
}

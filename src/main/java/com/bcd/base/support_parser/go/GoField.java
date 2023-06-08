package com.bcd.base.support_parser.go;

import java.lang.reflect.Field;

public class GoField {
    public final Field field;
    public final String goFieldName;
    public String goFieldTypeName;
    public String goReadTypeName;

    //是否是数组、否则切片
    public boolean goFieldIsArray;
    public GoField(Field field) {
        this.field = field;
        this.goFieldName = GoUtil.toFirstUpperCase(field.getName());
    }
}

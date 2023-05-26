package com.bcd.base.support_parser.go;

import java.lang.reflect.Field;

public class GoField {
    public final Field field;
    public final String goFieldName;

    public String goFieldTypeName;

    public GoField(Field field) {
        this.field = field;
        this.goFieldName = GoUtil.toFirstUpperCase(field.getName());
    }
}

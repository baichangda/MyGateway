package com.bcd.root.support_parser.go;

import java.lang.reflect.Field;

public class GoField {
    public final Field field;
    public final String goFieldName;
    public final String jsonExt;
    public String goFieldTypeName;
    public String goReadTypeName;

    public GoField(Field field) {
        this.field = field;
        this.jsonExt = "`json:\"" + field.getName() + "\"`";
        this.goFieldName = "F_" + field.getName();
    }
}

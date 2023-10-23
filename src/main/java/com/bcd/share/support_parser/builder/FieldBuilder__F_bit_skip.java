package com.bcd.share.support_parser.builder;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_bit_num_array;
import com.bcd.share.support_parser.anno.F_bit_skip;
import com.bcd.share.support_parser.anno.F_skip;
import com.bcd.share.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.util.List;

public class FieldBuilder__F_bit_skip extends FieldBuilder {

    private boolean finish(BuilderContext context) {
        List<Field> fieldList = context.fieldList;
        if (context.fieldIndex == fieldList.size() - 1) {
            return true;
        } else {
            Field next = fieldList.get(context.fieldIndex + 1);
            F_bit_num next_f_bit_num = next.getAnnotation(F_bit_num.class);
            F_bit_skip next_f_bit_skip = next.getAnnotation(F_bit_skip.class);
            F_bit_num_array next_f_bit_num_array = next.getAnnotation(F_bit_num_array.class);
            return next_f_bit_num == null && next_f_bit_skip == null && next_f_bit_num_array == null;
        }
    }

    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = FieldBuilder__F_bit_num.getBitBuf_parse(context);
        ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }


    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = FieldBuilder__F_bit_num.getBitBuf_deParse(context);

        ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
        if (finish(context)) {
            ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

    }

    @Override
    public Class<F_bit_skip> annoClass() {
        return F_bit_skip.class;
    }
}

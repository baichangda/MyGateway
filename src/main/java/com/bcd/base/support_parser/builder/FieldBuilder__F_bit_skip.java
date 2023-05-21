package com.bcd.base.support_parser.builder;


import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_bit_skip;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.exception.BaseRuntimeException;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.JavassistUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);

        if (Parser.logCollector_parse == null) {
            JavassistUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
        } else {
            context.varNameBitLog = varNameField + "_bitLog";
            JavassistUtil.append(body, "final {} {}={}.skip_log({});\n", BitBuf_reader.SkipLog.class.getName(), context.varNameBitLog, varNameBitBuf, anno.len());
        }
        if (context.bitEndWhenBitField_process) {
            JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }
    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = JavassistUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

        if (Parser.logCollector_deParse == null) {
            JavassistUtil.append(body, "{}.skip_log({});\n", varNameBitBuf, anno.len());
        } else {
            context.varNameBitLog = varNameField + "_bitLog";
            JavassistUtil.append(body, "final {} {}={}.skip_log({});\n", BitBuf_writer.SkipLog.class.getName(), context.varNameBitLog, varNameBitBuf, anno.len());
        }

        if (context.bitEndWhenBitField_deProcess) {
            JavassistUtil.append(body, "{}.finish();\n", varNameBitBuf);
        }

    }
}

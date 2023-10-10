package com.bcd.root.support_parser.go;

import com.bcd.root.support_parser.anno.F_bit_skip;
import com.bcd.root.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class GoFieldBuilder__F_bit_skip extends GoFieldBuilder {
    @Override
    public void buildStruct(GoBuildContext context) {
    }

    @Override
    public void buildParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final Class<? extends F_bit_skip> annoClass = anno.getClass();
        final StringBuilder body = context.parseBody;
        final String varNameBitBufReader = context.getVarNameBitBuf_reader();
        final int len = anno.len();
        ParseUtil.append(body, "{}.Skip({});\n", varNameBitBufReader, len);
        if (context.bitEndWhenBitField_process) {
            ParseUtil.append(body, "{}.Finish();\n", varNameBitBuf);
        }
    }

    public void buildDeParse(GoBuildContext context) {
        final Field field = context.field;
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final Class<? extends F_bit_skip> annoClass = anno.getClass();
        final StringBuilder body = context.deParseBody;
        final String varNameBitBufWriter = context.getVarNameBitBuf_writer();
        final int len = anno.len();
        ParseUtil.append(body, "{}.Skip({});\n", varNameBitBufWriter, len);
        if (context.bitEndWhenBitField_deProcess) {
            ParseUtil.append(body, "{}.Finish();\n", varNameBitBufWriter);
        }

    }

}

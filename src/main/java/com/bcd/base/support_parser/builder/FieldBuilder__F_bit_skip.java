package com.bcd.base.support_parser.builder;


import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.F_bit_skip;
import com.bcd.base.support_parser.util.BitBuf_reader;
import com.bcd.base.support_parser.util.BitBuf_writer;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;

public class FieldBuilder__F_bit_skip extends FieldBuilder {
    @Override
    public void buildParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_reader.class);
        if (Parser.logCollector_parse == null) {
            ParseUtil.append(body, "{}.skip({});\n", varNameBitBuf, anno.len());
            if (context.bitEndWhenBitField_process) {
                ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
            }
        } else {
            final String varNameSkipBitLog = varNameField + "_skipBitLog";
            ParseUtil.append(body, "final {} {}={}.skip_log({});\n", BitBuf_reader.SkipLog.class.getName(), varNameSkipBitLog, varNameBitBuf, anno.len());
            ParseUtil.appendBitLog_parse(context, varNameSkipBitLog);
            if (context.bitEndWhenBitField_process) {
                final String varNameFinishBitLog = varNameField + "_finishBitLog";
                ParseUtil.append(body, "{} {}={}.finish_log();\n", BitBuf_reader.FinishLog.class.getName(), varNameFinishBitLog, varNameBitBuf);
                ParseUtil.appendBitLog_parse(context, varNameFinishBitLog);
            }
        }

    }

    @Override
    public void buildDeParse(BuilderContext context) {
        final StringBuilder body = context.body;
        final Field field = context.field;
        final String fieldName = field.getName();
        final F_bit_skip anno = field.getAnnotation(F_bit_skip.class);
        final String varNameField = ParseUtil.getFieldVarName(context);

        final String varNameBitBuf = context.getVarNameBitBuf(BitBuf_writer.class);

        if (Parser.logCollector_deParse == null) {
            ParseUtil.append(body, "{}.skip_log({});\n", varNameBitBuf, anno.len());
            if (context.bitEndWhenBitField_deProcess) {
                ParseUtil.append(body, "{}.finish();\n", varNameBitBuf);
            }
        } else {
            final String varNameSkipBitLog = varNameField + "_skipBitLog";
            ParseUtil.append(body, "final {} {}={}.skip_log({});\n", BitBuf_writer.SkipLog.class.getName(), varNameSkipBitLog, varNameBitBuf, anno.len());
            ParseUtil.appendBitLog_deParse(context, varNameSkipBitLog);
            if (context.bitEndWhenBitField_deProcess) {
                final String varNameFinishBitLog = varNameField + "_finishBitLog";
                ParseUtil.append(body, "{} {}={}.finish_log();\n", BitBuf_writer.FinishLog.class.getName(), varNameFinishBitLog, varNameBitBuf);
                ParseUtil.appendBitLog_deParse(context, varNameFinishBitLog);
            }
        }
    }
}

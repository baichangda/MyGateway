package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.BitOrder;
import com.bcd.base.support_parser.anno.ByteOrder;
import com.bcd.base.support_parser.builder.FieldBuilder;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GoBuildContext {
    public final Class<?> clazz;
    public final String goStructName;


    public Field field;
    public GoField goField;


    public final Map<Character, String> varToGoFieldName_parse = new HashMap<>();
    public final Map<Character, String> varToGoFieldName_deParse = new HashMap<>();

    public final ByteOrder pkg_byteOrder;
    public final BitOrder pkg_bitOrder;

    public final StringBuilder structBody;
    public final StringBuilder parseBody;
    public final StringBuilder deParseBody;

    public String varNameBitBuf_reader;
    public String varNameBitBuf_writer;

    public boolean bitEndWhenBitField_process;
    public boolean bitEndWhenBitField_deProcess;

    public String prevSkipReservedIndexVarName = FieldBuilder.varNameStartIndex;

    public GoBuildContext(Class<?> clazz, ByteOrder pkg_byteOrder, BitOrder pkg_bitOrder, StringBuilder structBody
            , StringBuilder parseBody, StringBuilder deParseBody) {
        this.clazz = clazz;
        this.goStructName = GoUtil.toFirstUpperCase(clazz.getSimpleName());
        this.pkg_byteOrder = pkg_byteOrder;
        this.pkg_bitOrder = pkg_bitOrder;
        this.structBody = structBody;
        this.parseBody = parseBody;
        this.deParseBody = deParseBody;
    }

    public void setField(Field field) {
        this.field = field;
        this.goField = new GoField(field);
    }


    public final String getVarNameBitBuf_reader() {
        if (varNameBitBuf_reader == null) {
            ParseUtil.append(parseBody, "  if {}==nil{\n",GoFieldBuilder.varNameBitBuf);
            ParseUtil.append(parseBody, "    {}=util.ToBitBuf_reader({})\n", GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf);
            ParseUtil.append(parseBody, "  }\n");
            varNameBitBuf_reader = GoFieldBuilder.varNameBitBuf;
        }
        return varNameBitBuf_reader;
    }

    public final String getVarNameBitBuf_writer() {
        if (varNameBitBuf_writer == null) {
            ParseUtil.append(deParseBody, "  if {}==nil{\n",GoFieldBuilder.varNameBitBuf);
            ParseUtil.append(deParseBody, "    {}=util.ToBitBuf_writer({})\n", GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf);
            ParseUtil.append(deParseBody, "  }\n");
            varNameBitBuf_writer = GoFieldBuilder.varNameBitBuf;
        }
        return varNameBitBuf_writer;
    }
}

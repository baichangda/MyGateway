package com.bcd.base.support_parser.go;

import com.bcd.base.support_parser.anno.BitOrder;
import com.bcd.base.support_parser.anno.ByteOrder;
import com.bcd.base.support_parser.util.ParseUtil;

import java.lang.reflect.Field;
import java.time.ZoneId;
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

    public final StringBuilder globalBody;
    public final StringBuilder structBody;
    public final StringBuilder parseBody;
    public final StringBuilder deParseBody;
    public final StringBuilder customizeBody;

    public String varNameBitBuf_reader;
    public String varNameBitBuf_writer;
    public String varNameParseContext;
    public String varNameDeParseContext;

    public boolean bitEndWhenBitField_process;
    public boolean bitEndWhenBitField_deProcess;


    public GoBuildContext(Class<?> clazz, ByteOrder pkg_byteOrder, BitOrder pkg_bitOrder, StringBuilder globalBody, StringBuilder structBody
            , StringBuilder parseBody, StringBuilder deParseBody, StringBuilder customizeBody) {
        this.clazz = clazz;
        this.goStructName = GoUtil.toFirstUpperCase(clazz.getSimpleName());
        this.pkg_byteOrder = pkg_byteOrder;
        this.pkg_bitOrder = pkg_bitOrder;
        this.globalBody = globalBody;
        this.structBody = structBody;
        this.parseBody = parseBody;
        this.deParseBody = deParseBody;
        this.customizeBody = customizeBody;
    }

    public void setField(Field field) {
        this.field = field;
        this.goField = new GoField(field);
    }


    public final String getVarNameBitBuf_reader() {
        if (varNameBitBuf_reader == null) {
            ParseUtil.append(parseBody, "{}:=parse.GetBitBuf_reader({},{})\n"
                    , GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            varNameBitBuf_reader = GoFieldBuilder.varNameBitBuf;
        }
        return varNameBitBuf_reader;
    }

    public final String getVarNameBitBuf_writer() {
        if (varNameBitBuf_writer == null) {
            ParseUtil.append(deParseBody, "{}:=parse.GetBitBuf_writer({},{})\n"
                    , GoFieldBuilder.varNameBitBuf, GoFieldBuilder.varNameByteBuf, GoFieldBuilder.varNameParentParseContext);
            varNameBitBuf_writer = GoFieldBuilder.varNameBitBuf;
        }
        return varNameBitBuf_writer;
    }

    public final String getVarNameParseContext() {
        if (varNameParseContext == null) {
            if(GoUtil.noPointerStructSet.contains(goStructName)) {
                ParseUtil.append(parseBody, "{}:=parse.ToParseContext({},{})\n", GoFieldBuilder.varNameParseContext, GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameParentParseContext);
            }else {
                ParseUtil.append(parseBody, "{}:=parse.ToParseContext(&{},{})\n", GoFieldBuilder.varNameParseContext, GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameParentParseContext);
            }
            varNameParseContext = GoFieldBuilder.varNameParseContext;
        }
        return varNameParseContext;
    }

    public final String getVarNameDeParseContext() {
        if (varNameDeParseContext == null) {
            if(GoUtil.noPointerStructSet.contains(goStructName)){
                ParseUtil.append(deParseBody, "{}:=parse.ToParseContext({},{})\n", GoFieldBuilder.varNameParseContext, GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameParentParseContext);
            }else{
                ParseUtil.append(deParseBody, "{}:=parse.ToParseContext(_{},{})\n", GoFieldBuilder.varNameParseContext, GoFieldBuilder.varNameInstance, GoFieldBuilder.varNameParentParseContext);
            }
            varNameDeParseContext = GoFieldBuilder.varNameParseContext;
        }
        return varNameDeParseContext;
    }


    public final String getVarNameLocation(String zoneId) {
        final ZoneId of = ZoneId.of(zoneId);
        String varName = GoUtil.zoneId_varNameLocation.get(zoneId);
        if (varName == null) {
            final int size = GoUtil.zoneId_varNameLocation.size();
            varName = "_Location" + size;
            ParseUtil.append(globalBody, "var {},_=time.LoadLocation(\"{}\")\n", varName, of.getId());
            GoUtil.zoneId_varNameLocation.put(zoneId, varName);
        }
        return varName;
    }

}

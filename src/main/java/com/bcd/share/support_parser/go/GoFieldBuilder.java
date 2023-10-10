package com.bcd.share.support_parser.go;


public abstract class GoFieldBuilder {

    public final static String varNameInstance = "_instance";
    public final static String varNameByteBuf = "_byteBuf";
    public final static String varNameBitBuf = "_bitBuf";
    public final static String varNameParentParseContext = "_parentParseContext";
    public final static String varNameParseContext = "_parseContext";

    public final static String varNameStartIndex = "_start_index";


    public abstract void buildStruct(final GoBuildContext context);

    public abstract void buildParse(final GoBuildContext context);

    public abstract void buildDeParse(final GoBuildContext context);

}

package com.bcd.base.support_parser.go;


public abstract class GoFieldBuilder {

    public final static String varNameInstance = "_instance";
    public final static String varNameByteBuf = "_byteBuf";
    public final static String varNameBitBuf = "_bitBuf";

    public final static String space="    ";


    public abstract void buildStruct(final GoBuildContext context);

    public abstract void buildParse(final GoBuildContext context);

    public abstract void buildDeParse(final GoBuildContext context);

}

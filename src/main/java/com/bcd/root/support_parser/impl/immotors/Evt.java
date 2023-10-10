package com.bcd.root.support_parser.impl.immotors;

import com.bcd.root.support_parser.anno.F_num;
import com.bcd.root.support_parser.anno.NumType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Evt {
    @F_num(type = NumType.uint16)
    public int evtId;
}

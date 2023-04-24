package com.bcd.base.support_parser.impl.gb32960;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;


public class Parser_gb32960_test {
    @Test
    public void test(){
//        Parser.withDefaultLogCollector_parse();
//        Parser.withDefaultLogCollector_deParse();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        String data = "232303FE4C534A41323430333048533139323936390101351403190F0507010203010000000469B00EE5271055020F1FFF000002010103424E1E4E2045FFFF2710050006BE437001CF306A060160FFFF0101FFFF0118FF01010E070000000000000000000801010EE527100060000160FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF09010100180EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFED";
        byte [] bytes= ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf= Unpooled.wrappedBuffer(bytes);
        Packet packet = Parser.parse(Packet.class, byteBuf, null);
        ByteBuf dest=Unpooled.buffer();
        Parser.deParse(packet, dest,null);
        System.out.println(data.toUpperCase());
        System.out.println(ByteBufUtil.hexDump(dest).toUpperCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }
}

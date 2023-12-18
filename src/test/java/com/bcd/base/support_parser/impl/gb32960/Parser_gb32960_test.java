package com.bcd.base.support_parser.impl.gb32960;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.gb32960.data.Packet;
import com.bcd.share.support_parser.util.PerformanceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Parser_gb32960_test {

    static Logger logger = LoggerFactory.getLogger(Parser_gb32960_test.class);


    @Test
    public void test() {
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
//        String data = "232302fe4c534a4533363039364d53313430343935010141170a1b0e1a0d010103010040000003520f2827811c012e2000000002010101594fdb4e2f4a0f3227100500073944e501dd620a0601090e1b01370e14010145010444070300021387000000000801010f282781006c00016c0e180e190e1a0e190e190e180e180e1a0e1b0e180e190e1a0e180e180e190e1a0e1a0e190e180e1a0e180e1a0e1a0e180e170e190e170e190e170e190e1b0e190e190e190e180e180e170e170e180e170e170e170e190e170e180e170e190e170e170e170e180e180e190e190e140e180e180e170e170e150e160e160e180e190e170e180e170e180e170e180e170e160e190e150e180e160e180e170e160e160e170e150e170e170e140e170e160e160e170e170e170e170e160e170e160e170e140e170e170e160e160e170e170e170e160e160e160e16090101000c454545444544444445444544f5";
//        String data = "232303FE4C534A413234303330485331393239363901013507E403190F0507010203010000000469B00EE5271055020F1FFF000002010103424E1E4E2045FFFF2710050006BE437001CF306A060160FFFF0101FFFF0118FF01010E070000000000000000000801010EE527100060000160FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF09010100180EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFED";
//        String data = "232305013230303030304c534a535130303030303001000617060a09262042";
//        String data = "23 23 03 01 4C 53 4A 41 32 34 30 33 30 48 53 31 39 32 39 36 39 01 00 00 FE";
//        String data = "232301fe4c534a4533363039364d5331343034393501001e170b01110f1900013030303030303030303030303030303030303030000000";
        String data = "232302FE4C534A57543430393150533939313935360101F9170B1D0D1E16010303010000000014B41D2C271525010F2000000002010104384E204E20371D2E27100500073942A601DD5FAC0601030E4E01440E48010239010138070000000000000000000801011D2C271500CC0001C80E4A0E4A0E4E0E4C0E4A0E4C0E4B0E4A0E4A0E490E4B0E4C0E4C0E4B0E4D0E4C0E4A0E4B0E4A0E4B0E4C0E4A0E4A0E4B0E4B0E4B0E4B0E4C0E4B0E4D0E4C0E4C0E4C0E4A0E4A0E4A0E4A0E490E4B0E4A0E4A0E4A0E4A0E4A0E490E4A0E4B0E4E0E4A0E4B0E4C0E4A0E4B0E4A0E4C0E4A0E4A0E490E4B0E490E4C0E490E4C0E4C0E490E490E4C0E480E4A0E4B0E4B0E4B0E4A0E4A0E4A0E4C0E4B0E490E4C0E4A0E4A0E4B0E4A0E4C0E4B0E4B0E4C0E4B0E4A0E4B0E4A0E4E0E4B0E4B0E4B0E4A0E4C0E4A0E4C0E4D0E4A0E4A0E4C0E4A0E4B0E4E0E4B0E4A0E4C0E4C0E4B0E4A0E4A0E4C0E4D0E4D0E4B0E4B0E4C0E4B0E4C0E4B0E4B0E4D0E4D0E4A0E4C0E4C0E4C0E4C0E4C0E4B0E4D0E4A0E4C0E4A0E4B0E4C0E4B0E4B0E4C0E4C0E4C0E4A0E4B0E4B0E4A0E4B0E4C0E4D0E4A0E4A0E490E4A0E4A0E4B0E490E4A0E4A0E4A0E4B0E4B0E4D0E4D0E4A0E4D0E4B0E4C0E4D0E4B0E490E490E490E490E490E480E490E490E4B0E4B0E4B0E4C0E4B0E4C0E4B0E4B0E4C0E4B0E4A0E4A0E4B0E4B0E4A0E4B0E4B0E4B0E4C0E4A0E4D0E4A090101000C3839383938393839383938394E";
//        String data = "232304fe4c534a4533363039364d53313430343935010008170b01100f1e000100";
        data = data.replaceAll(" ", "");
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        Packet packet = Packet.read(byteBuf);
        ByteBuf dest = Unpooled.buffer();
        packet.write(dest);
        logger.info(data.toUpperCase());
        logger.info(ByteBufUtil.hexDump(dest).toUpperCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

    @Test
    public void test_performance() {
        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        String data = "232303FE4C534A41323430333048533139323936390101351403190F0507010203010000000469B00EE5271055020F1FFF000002010103424E1E4E2045FFFF2710050006BE437001CF306A060160FFFF0101FFFF0118FF01010E070000000000000000000801010EE527100060000160FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF09010100180EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFED";
        int threadNum = 1;
        logger.info("param threadNum[{}]", threadNum);
        int num = 1000000000;
        PerformanceUtil.testMultiThreadPerformance(ByteBufUtil.decodeHexDump(data), threadNum, num, Packet::read, (buf, instance) -> instance.write(buf), true);
    }
}

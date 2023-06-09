package com.bcd.base.support_parser.impl.someip;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.impl.immotors.ep33.Parser_immotors_ep33_test;
import com.bcd.base.support_parser.impl.someip.data.Packet;
import com.bcd.base.support_parser.util.PerformanceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser_someip_test {

    static Logger logger = LoggerFactory.getLogger(Parser_someip_test.class);


    @Test
    public void test() {
//        Parser.withDefaultLogCollector_parse();
//        Parser.withDefaultLogCollector_deParse();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        String data = "000100e4000000ac0009000a0304000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a1";
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        final Packet packet = Parser.parse(Packet.class, byteBuf, null);
        ByteBuf dest = Unpooled.buffer();
        Parser.deParse(packet, dest, null);
        logger.info(data.toUpperCase());
        logger.info(ByteBufUtil.hexDump(dest).toLowerCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

    public void test_performance() {
        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        String hex = "000100e4000000ac0009000a0304000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a1";

        int threadNum = 1;
        logger.info("param threadNum[{}]", threadNum);
        int num = 1000000000;

        PerformanceUtil.testMultiThreadPerformance(ByteBufUtil.decodeHexDump(hex), Packet.class, threadNum, num, true);

    }
}

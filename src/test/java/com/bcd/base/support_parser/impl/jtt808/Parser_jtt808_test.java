package com.bcd.base.support_parser.impl.jtt808;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.impl.jtt808.data.Packet;
import com.bcd.share.support_parser.processor.Processor;
import com.bcd.share.support_parser.util.PerformanceUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Parser_jtt808_test {

    static Logger logger = LoggerFactory.getLogger(Parser_jtt808_test.class);


    @Test
    public void test() {
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        Parser.enableGenerateClassFile();
        Parser.enablePrintBuildLog();
        String data = "7e0200407c0100000000017299841738ffff000004000000080006eeb6ad02633df701380003006320070719235901040000000b02020016030200210402002c051e3737370000000000000000000000000000000000000000000000000000001105420000004212064d0000004d4d1307000000580058582504000000632a02000a2b040000001430011e310128637e";
        data = data.replaceAll(" ", "");
        byte[] bytes = ByteBufUtil.decodeHexDump(data);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        byteBuf = Packet.unEscapeAndXor(byteBuf);
        final Processor<Packet> processor = Parser.getProcessor(Packet.class);
        Packet packet = processor.process(byteBuf);
        ByteBuf dest = Unpooled.buffer();
        processor.deProcess(dest, packet);
        logger.info(data.toUpperCase());
        logger.info(ByteBufUtil.hexDump(dest).toUpperCase());
        assert data.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

    @Test
    public void test_performance() {
        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        String data = "7e0200407c0100000000017299841738ffff000004000000080006eeb6ad02633df701380003006320070719235901040000000b02020016030200210402002c051e3737370000000000000000000000000000000000000000000000000000001105420000004212064d0000004d4d1307000000580058582504000000632a02000a2b040000001430011e310128637e";
        int threadNum = 1;
        logger.info("param threadNum[{}]", threadNum);
        int num = 1000000000;
        PerformanceUtil.testMultiThreadPerformance(ByteBufUtil.decodeHexDump(data), Packet.class, threadNum, num, true);
    }
}
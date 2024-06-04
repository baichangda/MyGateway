package com.bcd.base.support_parser.impl.icd;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.BitOrder;
import com.bcd.base.support_parser.anno.ByteOrder;
import com.bcd.base.support_parser.impl.icd.data.Msg;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Parser_icd_test {

    static Logger logger= LoggerFactory.getLogger(Parser_icd_test.class);

    @Test
    public void test() {
//        Parser.enablePrintBuildLog();
        Parser.enableGenerateClassFile();
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        Processor<Msg> processor = Parser.getProcessor(Msg.class, ByteOrder.smallEndian, BitOrder.bigEndian);
        String hex="C3C3C3C380000802000001000000010104D708140E2BCE009643F336F97DD941070026DD9A469B2F021593070000BC7C0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000565A57540165746831000000000000000000000000170AA8C000FFFFFF010AA8C0F6970400A826000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        byte[] bytes = ByteBufUtil.decodeHexDump(hex);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        Msg msg = processor.process(byteBuf);
        logger.info("{}",msg);
        ByteBuf dest=Unpooled.buffer();
        processor.deProcess(dest,msg);
        String s = ByteBufUtil.hexDump(dest);
        logger.info(hex.toUpperCase());
        logger.info(s.toUpperCase());
        assert s.equalsIgnoreCase(hex);
    }
}

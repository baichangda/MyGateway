package com.bcd.share.support_parser.impl.icd.data;

import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.anno.*;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.List;

public class Msg_body_trigger_statistics_info implements Msg_body {
    @F_num(type = NumType.uint16, var = 'a')
    public int src_count;
    @F_num(type = NumType.uint8, var = 'b')
    @F_skip(lenAfter = 29)
    public short lane_count;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Lane_info_trigger> lane_info_array;

    public static void main(String[] args) {
        final String hex = "c3c3c3c380003c00000003030001000128d7081472e64000fed458cc9a0fd9410a000000000000000000000000004a0100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100010000000000000000000000000000000000000000000000000000000000010000108200000011c409d80401000000000000401a8f7d3c3c3c3c";
        final ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hex));
        final boolean b = Msg.check_sum(byteBuf);
        System.out.println(b);
//        Parser.enablePrintBuildLog();
//        Parser.enableGenerateClassFile();
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        Processor<Msg> processor = Parser.getProcessor(Msg.class);
        final Msg msg = processor.process(byteBuf, null);
        ByteBuf dest = Unpooled.buffer();
        processor.deProcess(dest, null, msg);
        System.out.println(hex.toUpperCase());
        System.out.println(ByteBufUtil.hexDump(dest).toUpperCase());
        assert hex.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }

}

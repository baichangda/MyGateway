package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.List;

public class Msg_body_queue_statistics_info implements Msg_body{
    @F_num(type = NumType.uint16,var = 'a')
    public int src_count;
    @F_num(type = NumType.uint8,var = 'b')
    public short lane_count;
    @F_skip(len = 32,mode = SkipMode.reservedFromStart)
    public byte reserved;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Lane_info_queue> lane_info_array;

    public static void main(String[] args) {
        final String hex="c3c3c3c380006c00000004030001000128d70814b4f2400042609dfb9a0fd9410a000000000000000000000000004a010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010004000000000000000000000000000000000000000000000000000000000001000010010000000000000000000000000000000200000000000000000000000000000003b608de08dc0f040000000000000000046e14ba097a1c070000000000000000d1b71ca93c3c3c3c";
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
        processor.deProcess(dest, null,msg);
        System.out.println(hex.toUpperCase());
        System.out.println(ByteBufUtil.hexDump(dest).toUpperCase());
        assert hex.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }
}

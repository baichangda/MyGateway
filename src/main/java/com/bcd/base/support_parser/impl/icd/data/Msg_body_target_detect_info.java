package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import com.bcd.base.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.List;

public class Msg_body_target_detect_info implements Msg_body {
    @F_num(type = NumType.uint32)
    public long frame_id;
    public double frame_timestamp;
    @F_num(type = NumType.uint16, var = 'a')
    public int src_count;
    @F_num(type = NumType.uint16, var = 'b')
    public int target_count;

    @F_skip(len = 64, mode = SkipMode.reservedFromStart)
    public byte[] reserved;
    @F_num_array(lenExpr = "a", singleType = NumType.uint32)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Target_info> target_info_array;

    public static void main(String[] args) {
        final String hex="c3c3c3c380004c0100000002000100011ed7081432c83d009cc4b0db990fd9410a000000000000000000000000004a01000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000033c83d009cc4b0db990fd94101000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000100000a801000000000000000000000000000000000000c70d0000b81200000000000050462b0000000000000000000000e803b4000000536406003b000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010055010000000000000000000000000000000000004d110000921a0000000000005046740100000000000000000000b801b400000052640400510000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008263be5f3c3c3c3c";
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

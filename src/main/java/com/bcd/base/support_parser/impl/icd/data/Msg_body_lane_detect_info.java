package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.Parser;
import com.bcd.base.support_parser.anno.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.util.List;

public class Msg_body_lane_detect_info implements Msg_body {
    @F_integer(len = 4)
    public long frame_id;
    @F_float_ieee754(type = FloatType_ieee754.Float64)
    public double frame_timestamp;
    @F_integer(len = 2, var = 'a')
    public int src_count;
    @F_integer(len = 4, var = 'b')
    public long road_count;
    @F_skip(len = 32, mode = SkipMode.ReservedFromStart)
    public byte[] reserved;
    @F_integer_array(lenExpr = "a", singleLen = 4)
    public long[] src_array;
    @F_bean_list(listLenExpr = "b")
    public List<Road_info> road_info_array;

    public static void main(String[] args) {
        final String hex="c3c3c3c380005a0000000102000100011ed70814e0d03d0052b806ff990fd9410a000000000000000000000000004a010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e1d03d0052b806ff990fd94101000100000000000000000000000000000000000100001001000000010000000100000001005501000000000000000000000000000000000000000000000000000000000000523add353c3c3c3c";
        final ByteBuf byteBuf = Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(hex));
        final boolean b = Msg.check_sum(byteBuf);
        System.out.println(b);
//        Parser.enablePrintBuildLog();
//        Parser.enableGenerateClassFile();
        Parser.withDefaultLogCollector_parse();
        Parser.withDefaultLogCollector_deParse();
        final Msg msg = Parser.parse(Msg.class, byteBuf, null);
        ByteBuf dest = Unpooled.buffer();
        Parser.deParse(msg, dest, null);
        System.out.println(hex.toUpperCase());
        System.out.println(ByteBufUtil.hexDump(dest).toUpperCase());
        assert hex.equalsIgnoreCase(ByteBufUtil.hexDump(dest));
    }
}

package com.bcd.share.support_parser.impl.icd.data;


import com.bcd.share.support_parser.anno.F_bean;
import com.bcd.share.support_parser.anno.F_customize;
import com.bcd.share.support_parser.impl.icd.processor.Msg_body_processor;
import com.bcd.share.support_parser.util.CrcUtil;
import com.bcd.share.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;

public class Msg {
    @F_bean
    public Msg_header msg_header;
    @F_customize(processorClass = Msg_body_processor.class)
    public Msg_body msg_body;
    @F_bean
    public Msg_tailer msg_tailer;

    public static void main(String[] args) throws JsonProcessingException {
        Msg msg = new Msg();
        msg.msg_header = new Msg_header();
        msg.msg_body = new Msg_body_system_runtime_info();
        final String json = JsonUtil.toJson(msg);
        System.out.println(json);
        final Msg msg1 = JsonUtil.GLOBAL_OBJECT_MAPPER.readValue(json, Msg.class);
        System.out.println(msg1);
    }

    public static boolean check_sum(ByteBuf byteBuf) {
        final int checkLen = byteBuf.readableBytes() - 8;
        final long except = byteBuf.getUnsignedIntLE(checkLen);
        final long actual = CrcUtil.crc32_mpeg_2(byteBuf, 0, checkLen);
        return actual == except;
    }
}

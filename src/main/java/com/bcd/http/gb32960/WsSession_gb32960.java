package com.bcd.http.gb32960;

import com.bcd.http.TcpClientHandler;
import com.bcd.http.WsSession;
import com.bcd.base.support_parser.impl.gb32960.data.Packet;
import com.bcd.base.support_parser.impl.gb32960.data.VehicleRunData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Date;

public class WsSession_gb32960 extends WsSession<Packet> {
    private final static String sample = "232302FE4C534A4533363039364D53313430343935010141170608100A10010103010040000003520F2827811C012E2000000002010101594FDB4E2F4A0F3227100500073944E501DD620A0601090E1B01370E14010145010444070300021387000000000801010F282781006C00016C0E180E190E1A0E190E190E180E180E1A0E1B0E180E190E1A0E180E180E190E1A0E1A0E190E180E1A0E180E1A0E1A0E180E170E190E170E190E170E190E1B0E190E190E190E180E180E170E170E180E170E170E170E190E170E180E170E190E170E170E170E180E180E190E190E140E180E180E170E170E150E160E160E180E190E170E180E170E180E170E180E170E160E190E150E180E160E180E170E160E160E170E150E170E170E140E170E160E160E170E170E170E170E160E170E160E170E140E170E170E160E160E170E170E170E160E160E160E16090101000C454545444544444445444544F5";

    public WsSession_gb32960(io.helidon.websocket.WsSession ws,Object ... args) {
        super(ws,args);
    }

    public void initSocketChannel(SocketChannel sc){
        sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(10 * 1024, 22, 2, 1, 0));
        sc.pipeline().addLast(new TcpClientHandler(this));
    }

    public Packet initSample(Object ... args){
        byte[] bytes = ByteBufUtil.decodeHexDump(sample);
        Packet packet = HttpServerBuilder_gb32960.processor.process(Unpooled.wrappedBuffer(bytes));
        packet.vin = args[0].toString();
        return packet;
    }

    public ByteBuf toByteBuf(Packet sample, long ts){
        ByteBuf buffer = Unpooled.buffer();
        ((VehicleRunData) sample.data).collectTime = new Date(ts);
        HttpServerBuilder_gb32960.processor.deProcess(buffer, sample);
        return buffer;
    }
}

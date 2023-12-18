package com.bcd.share.support_parser.impl.immotors.ep33.data;

import com.bcd.share.exception.BaseRuntimeException;
import com.bcd.share.support_parser.Parser;
import com.bcd.share.support_parser.processor.Processor;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.ArrayList;
import java.util.List;

public class Packet {
    public Evt_0001 evt_0001;
    public Evt_0004 evt_0004;
    public Evt_0005 evt_0005;
    public Evt_0006 evt_0006;
    public Evt_0007 evt_0007;
    public Evt_0008 evt_0008;
    public Evt_0009 evt_0009;
    public Evt_000A evt_000A;
    public Evt_0800 evt_0800;
    public Evt_0801 evt_0801;
    public Evt_0802 evt_0802;
    public Evt_0803 evt_0803;
    public Evt_D006 evt_D006;
    public Evt_D008 evt_D008;
    public Evt_D009 evt_D009;
    public Evt_D00A evt_D00A;
    public Evt_D00B evt_D00B;
    public Evt_D00C evt_D00C;
    public Evt_D00D evt_D00D;
    public Evt_D00E evt_D00E;
    public Evt_D00F evt_D00F;
    public Evt_D01D evt_D01D;
    public Evt_2_6_unknown[] evt_2_6_unknown;
    public Evt_4_x_unknown[] evt_4_x_unknown;
    public Evt_FFFF evt_FFFF;

    final static Processor<Evt_0001> processor_Evt_0001 = Parser.getProcessor(Evt_0001.class);
    final static Processor<Evt_0004> processor_Evt_0004 = Parser.getProcessor(Evt_0004.class);
    final static Processor<Evt_0005> processor_Evt_0005 = Parser.getProcessor(Evt_0005.class);
    final static Processor<Evt_0006> processor_Evt_0006 = Parser.getProcessor(Evt_0006.class);
    final static Processor<Evt_0007> processor_Evt_0007 = Parser.getProcessor(Evt_0007.class);
    final static Processor<Evt_0008> processor_Evt_0008 = Parser.getProcessor(Evt_0008.class);
    final static Processor<Evt_0009> processor_Evt_0009 = Parser.getProcessor(Evt_0009.class);
    final static Processor<Evt_000A> processor_Evt_000A = Parser.getProcessor(Evt_000A.class);
    final static Processor<Evt_0800> processor_Evt_0800 = Parser.getProcessor(Evt_0800.class);
    final static Processor<Evt_0801> processor_Evt_0801 = Parser.getProcessor(Evt_0801.class);
    final static Processor<Evt_0802> processor_Evt_0802 = Parser.getProcessor(Evt_0802.class);
    final static Processor<Evt_0803> processor_Evt_0803 = Parser.getProcessor(Evt_0803.class);
    final static Processor<Evt_D006> processor_Evt_D006 = Parser.getProcessor(Evt_D006.class);
    final static Processor<Evt_D008> processor_Evt_D008 = Parser.getProcessor(Evt_D008.class);
    final static Processor<Evt_D009> processor_Evt_D009 = Parser.getProcessor(Evt_D009.class);
    final static Processor<Evt_D00A> processor_Evt_D00A = Parser.getProcessor(Evt_D00A.class);
    final static Processor<Evt_D00E> processor_Evt_D00E = Parser.getProcessor(Evt_D00E.class);
    final static Processor<Evt_D00F> processor_Evt_D00F = Parser.getProcessor(Evt_D00F.class);
    final static Processor<Evt_D01D> processor_Evt_D01D = Parser.getProcessor(Evt_D01D.class);
    final static Processor<Evt_FFFF> processor_Evt_FFFF = Parser.getProcessor(Evt_FFFF.class);
    final static Processor<Evt_2_6_unknown> processor_Evt_2_6_unknown = Parser.getProcessor(Evt_2_6_unknown.class);
    final static Processor<Evt_4_x_unknown> processor_Evt_4_x_unknown = Parser.getProcessor(Evt_4_x_unknown.class);

    public static Packet read(ByteBuf data) {
        Packet packet = new Packet();
        List<Evt_2_6_unknown> evt_2_6_unknown = new ArrayList<>();
        List<Evt_4_x_unknown> evt_4_x_unknown = new ArrayList<>();
        A:
        while (data.isReadable()) {
            final int evtId = data.getUnsignedShort(data.readerIndex());
            switch (evtId) {
                case 0x0001 -> {
                    if (packet.evt_0001 != null) {
                        break A;
                    }
                    packet.evt_0001 = processor_Evt_0001.process(data);
                }
                case 0x0004 -> packet.evt_0004 = processor_Evt_0004.process(data);
                case 0x0005 -> packet.evt_0005 = processor_Evt_0005.process(data);
                case 0x0006 -> packet.evt_0006 = processor_Evt_0006.process(data);
                case 0x0007 -> packet.evt_0007 = processor_Evt_0007.process(data);
                case 0x0008 -> packet.evt_0008 = processor_Evt_0008.process(data);
                case 0x0009 -> packet.evt_0009 = processor_Evt_0009.process(data);
                case 0x000A -> packet.evt_000A = processor_Evt_000A.process(data);
                case 0x0800 -> packet.evt_0800 = processor_Evt_0800.process(data);
                case 0x0801 -> packet.evt_0801 = processor_Evt_0801.process(data);
                case 0x0802 -> packet.evt_0802 = processor_Evt_0802.process(data);
                case 0x0803 -> packet.evt_0803 = processor_Evt_0803.process(data);
                case 0xD006 -> packet.evt_D006 = processor_Evt_D006.process(data);
                case 0xD008 -> packet.evt_D008 = processor_Evt_D008.process(data);
                case 0xD009 -> packet.evt_D009 = processor_Evt_D009.process(data);
                case 0xD00A -> packet.evt_D00A = processor_Evt_D00A.process(data);
                case 0xD00B -> packet.evt_D00B = Evt_D00B.read(data);
                case 0xD00C -> packet.evt_D00C = Evt_D00C.read(data);
                case 0xD00D -> packet.evt_D00D = Evt_D00D.read(data);
                case 0xD00E -> packet.evt_D00E = processor_Evt_D00E.process(data);
                case 0xD00F -> packet.evt_D00F = processor_Evt_D00F.process(data);
                case 0xD01D -> packet.evt_D01D = processor_Evt_D01D.process(data);
                case 0xFFFF -> packet.evt_FFFF = processor_Evt_FFFF.process(data);
                default -> {
                    if ((evtId >= 0x0001 && evtId <= 0x07FF)
                            || (evtId >= 0x0800 && evtId <= 0x0FFF)
                            || (evtId >= 0x1000 && evtId <= 0x2FFF)
                            || (evtId >= 0x3000 && evtId <= 0x4FFF)
                            || (evtId >= 0x5000 && evtId <= 0x5FFF)
                            || (evtId >= 0x6000 && evtId <= 0x6FFF)
                            || (evtId >= 0x7000 && evtId <= 0x8FFF)
                            || (evtId >= 0x9000 && evtId <= 0xAFFF)
                    ) {
                        evt_2_6_unknown.add(processor_Evt_2_6_unknown.process(data));
                    } else if (evtId >= 0xD000 && evtId <= 0xDFFF) {
                        evt_4_x_unknown.add(processor_Evt_4_x_unknown.process(data));
                    } else {
                        final String evtIdHex = ByteBufUtil.hexDump(new byte[]{(byte) (evtId >> 8), (byte) evtId});
                        throw BaseRuntimeException.getException("evtId[{}] not support", evtIdHex);
                    }
                }
            }
        }
        packet.evt_2_6_unknown = evt_2_6_unknown.toArray(new Evt_2_6_unknown[0]);
        packet.evt_4_x_unknown = evt_4_x_unknown.toArray(new Evt_4_x_unknown[0]);
        return packet;
    }

    public void write(ByteBuf data) {
        if (evt_0001 != null) {
            processor_Evt_0001.deProcess(data, evt_0001);
        }
        if (evt_0004 != null) {
            processor_Evt_0004.deProcess(data, evt_0004);
        }
        if (evt_0005 != null) {
            processor_Evt_0005.deProcess(data, evt_0005);
        }
        if (evt_0006 != null) {
            processor_Evt_0006.deProcess(data, evt_0006);
        }
        if (evt_0007 != null) {
            processor_Evt_0007.deProcess(data, evt_0007);
        }
        if (evt_0008 != null) {
            processor_Evt_0008.deProcess(data, evt_0008);
        }
        if (evt_0009 != null) {
            processor_Evt_0009.deProcess(data, evt_0009);
        }
        if (evt_000A != null) {
            processor_Evt_000A.deProcess(data, evt_000A);
        }
        if (evt_0800 != null) {
            processor_Evt_0800.deProcess(data, evt_0800);
        }
        if (evt_0801 != null) {
            processor_Evt_0801.deProcess(data, evt_0801);
        }
        if (evt_0802 != null) {
            processor_Evt_0802.deProcess(data, evt_0802);
        }
        if (evt_0803 != null) {
            processor_Evt_0803.deProcess(data, evt_0803);
        }
        if (evt_D006 != null) {
            processor_Evt_D006.deProcess(data, evt_D006);
        }
        if (evt_D008 != null) {
            processor_Evt_D008.deProcess(data, evt_D008);
        }
        if (evt_D009 != null) {
            processor_Evt_D009.deProcess(data, evt_D009);
        }
        if (evt_D00A != null) {
            processor_Evt_D00A.deProcess(data, evt_D00A);
        }
        if (evt_D00B != null) {
            evt_D00B.write(data);
        }
        if (evt_D00C != null) {
            evt_D00C.write(data);
        }
        if (evt_D00D != null) {
            evt_D00D.write(data);
        }
        if (evt_D00E != null) {
            processor_Evt_D00E.deProcess(data, evt_D00E);
        }
        if (evt_D00F != null) {
            processor_Evt_D00F.deProcess(data, evt_D00F);
        }
        if (evt_D01D != null) {
            processor_Evt_D01D.deProcess(data, evt_D01D);
        }
        if (evt_2_6_unknown != null) {
            for (Evt_2_6_unknown e : evt_2_6_unknown) {
                processor_Evt_2_6_unknown.deProcess(data, e);
            }
        }
        if (evt_4_x_unknown != null) {
            for (Evt_4_x_unknown e : evt_4_x_unknown) {
                processor_Evt_4_x_unknown.deProcess(data, e);
            }
        }
        if (evt_FFFF != null) {
            processor_Evt_FFFF.deProcess(data, evt_FFFF);
        }
    }

    public static Packet[] reads(ByteBuf data) {
        List<Packet> packets = new ArrayList<>();
        while (data.isReadable()) {
            packets.add(read(data));
        }
        return packets.toArray(new Packet[0]);
    }

    public static void writes(ByteBuf data, Packet[] packets) {
        for (Packet packet : packets) {
            packet.write(data);
        }
    }
}

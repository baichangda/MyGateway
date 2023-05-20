package com.bcd.base.support_parser.util;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBuf_reader {

    static Logger logger = LoggerFactory.getLogger(BitBuf_reader.class);

    private final ByteBuf byteBuf;
    private byte b;
    //当前readIndex的bit偏移量
    private int bitOffset = 0;

    public static BitBuf_reader newBitBuf(ByteBuf byteBuf) {
        return new BitBuf_reader(byteBuf);
    }

    private BitBuf_reader(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public static void main(String[] args) {
        final long t1 = System.currentTimeMillis();
        final byte[] source = {
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4,
                (byte) 0xF0, (byte) 0xe4
        };
        for (int i = 0; i < 1; i++) {
//            ByteBuf bb = Unpooled.wrappedBuffer(source);
//            BitBuf_reader bitBuf = BitBuf_reader.newBitBuf(bb);
//            final long bitVal1 = bitBuf.read(1);
//            final long bitVal2 = bitBuf.read(15);
//            final long bitVal3 = bitBuf.read(1);
//            final long bitVal4 = bitBuf.read(15);
//            final long bitVal5 = bitBuf.read(1);
//            final long bitVal6 = bitBuf.read(15);
//            final long bitVal7 = bitBuf.read(1);
//            final long bitVal8 = bitBuf.read(15);
//            final long bitVal9 = bitBuf.read(1);
//            final long bitVal10 = bitBuf.read(15);
//            final long bitVal11 = bitBuf.read(1);
//            final long bitVal12 = bitBuf.read(15);
//            final long bitVal13 = bitBuf.read(15);
//            System.out.println(bitVal1);
//            System.out.println(bitVal2);
//            System.out.println(bitVal3);
//            System.out.println(bitVal4);
//            System.out.println(bitVal9);
//            System.out.println(bitVal10);
//            System.out.println(bitVal11);
//            System.out.println(bitVal12);
//            System.out.println(bitVal13);


            final byte[] source2 = {
                    (byte) 0x81, (byte) 0x72, (byte) 0x40,
            };
            ByteBuf bb2 = Unpooled.wrappedBuffer(source2);
            BitBuf_reader bitBuf2 = BitBuf_reader.newBitBuf(bb2);
//            final long l1 = bitBuf2.read_log(3);
//            final long l2 = bitBuf2.read_log(3);
//            final long l3 = bitBuf2.read_log(9);
            final ReadLog res1 = bitBuf2.read_log(3);
            final ReadLog res2 = bitBuf2.read_log(3);
            final SkipLog skip1 = bitBuf2.skip_log(3);
            final ReadLog res3 = bitBuf2.read_log(9);
            res1.print();
            res2.print();
            skip1.print();
            res3.print();
//            System.out.println(l1);
//            System.out.println(l2);
//            System.out.println(l3);
        }
        System.out.println(System.currentTimeMillis() - t1);
    }

    public void finish() {
        b = 0;
        bitOffset = 0;
    }

    public static class Log {
        public final byte[] bytes;

        public final int bitStart;

        public final int bitEnd;

        public Log(int byteLen, int bitStart, int bitEnd) {
            this.bytes = new byte[byteLen];
            this.bitStart = bitStart;
            this.bitEnd = bitEnd;
        }

        public String getLogHex() {
            return ByteBufUtil.hexDump(bytes) + ((bitEnd & 7) == 7 ? "" : "?");
        }

        public String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }
    }

    public static class SkipLog extends Log {
        public SkipLog(int byteLen, int bitStart, int bitEnd) {
            super(byteLen, bitStart, bitEnd);
        }

        public void print() {
            logger.info("skip bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", getLogHex(), bitStart, bitEnd, getLogBit());
        }
    }

    public static class ReadLog extends Log {

        public long val;

        public ReadLog(int byteLen, int bitStart, int bitEnd) {
            super(byteLen, bitStart, bitEnd);
        }

        public void print() {
            logger.info("read bit_hex[{}] bit_pos[{}-{}] bit_binary[{}] bit_val[{}]", getLogHex(), bitStart, bitEnd, getLogBit(), val);
        }
    }



    public long read(int bit) {
        if (bitOffset == 0) {
            b = byteBuf.readByte();
        }
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);

        long c = (b & 0xffL) << ((byteLen - 1) * 8);
        for (int i = 1; i < byteLen; i++) {
            b = byteBuf.readByte();
            c |= (b & 0xffL) << ((byteLen - 1 - i) << 3);
        }
        final int right = byteLen * 8 - bitOffset - bit;

        bitOffset = temp & 7;

        return (c >>> right) & ((0x01L << bit) - 1);
    }

    public ReadLog read_log(int bit) {
        if (bitOffset == 0) {
            b = byteBuf.readByte();
        }
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);

        final ReadLog log = new ReadLog(byteLen, bitOffset, bitOffset + bit - 1);

        log.bytes[0] = b;

        long c = (b & 0xffL) << ((byteLen - 1) * 8);
        for (int i = 1; i < byteLen; i++) {
            b = byteBuf.readByte();
            log.bytes[i] = b;
            c |= (b & 0xffL) << ((byteLen - 1 - i) << 3);
        }
        final int right = byteLen * 8 - bitOffset - bit;

        bitOffset = temp & 7;

        log.val = (c >>> right) & ((0x01L << bit) - 1);

        return log;
    }


    public void skip(int bit) {
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        if (byteLen == 1) {
            if (bitOffset == 0) {
                b = byteBuf.readByte();
            }
        } else {
            if (bitOffset == 0) {
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen);
                } else {
                    byteBuf.skipBytes(byteLen - 1);
                    b = byteBuf.readByte();
                }
            } else {
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen - 1);
                } else {
                    byteBuf.skipBytes(byteLen - 2);
                    b = byteBuf.readByte();
                }
            }
        }
        bitOffset = temp & 7;
    }


    public SkipLog skip_log(int bit) {
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);

        final SkipLog log = new SkipLog(byteLen, bitOffset, bitOffset + bit - 1);

        if (byteLen == 1) {
            if (bitOffset == 0) {
                b = byteBuf.readByte();
            }
            log.bytes[0] = b;
        } else {
            if (bitOffset == 0) {
                byteBuf.getBytes(0, log.bytes);
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen);
                } else {
                    byteBuf.skipBytes(byteLen - 1);
                    b = byteBuf.readByte();
                }
            } else {
                log.bytes[0] = b;
                byteBuf.getBytes(byteBuf.readerIndex(), log.bytes, 1, byteLen - 1);
                if (newBitOffsetZero) {
                    byteBuf.skipBytes(byteLen - 1);
                } else {
                    byteBuf.skipBytes(byteLen - 2);
                    b = byteBuf.readByte();
                }
            }
        }
        bitOffset = temp & 7;

        return log;
    }

}

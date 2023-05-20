package com.bcd.base.support_parser.util;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBuf_writer {

    static Logger logger = LoggerFactory.getLogger(BitBuf_writer.class);
    private final ByteBuf byteBuf;

    private byte b;

    //当前写入字节bit偏移量
    private int bitOffset = 0;

    public static BitBuf_writer newBitBuf(ByteBuf byteBuf) {
        return new BitBuf_writer(byteBuf);
    }

    private BitBuf_writer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public static void main(String[] args) {
        final long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
//            final ByteBuf bb = Unpooled.buffer();
//            final BitBuf_writer bitBufWriter = BitBuf_writer.newBitBuf(bb);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(1L, 1);
//            bitBufWriter.write(28900L, 15);
//            bitBufWriter.write(28900L, 15);

//            System.out.println(ByteBufUtil.hexDump(bb));


            final ByteBuf bb = Unpooled.buffer();
            final BitBuf_writer bitBufWriter = BitBuf_writer.newBitBuf(bb);
            final LogRes res1 = bitBufWriter.write_log(4, 3);
            final LogRes res2 = bitBufWriter.write_log(0, 3);
            final LogRes res3 = bitBufWriter.write_log(185, 9);
            res1.print();
            res2.print();
            res3.print();
            bitBufWriter.finish();
//            System.out.println(ByteBufUtil.hexDump(bb));
        }
        System.out.println(System.currentTimeMillis() - t1);
    }

    public void finish() {
        if (bitOffset > 0) {
            byteBuf.writeByte(b);
        }
        b = 0;
        bitOffset = 0;
    }

    public static class LogRes {
        public final byte[] bytes;

        public final int bitStart;

        public final int bitEnd;

        public long val;

        public LogRes(int byteLen, int bitStart, int bitEnd, long val) {
            this.bytes = new byte[byteLen];
            this.bitStart = bitStart;
            this.bitEnd = bitEnd;
            this.val = val;
        }

        public String getLogHex() {
            return ByteBufUtil.hexDump(bytes) + ((bitEnd & 7) == 7 ? "" : "?");
        }

        public String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd);
        }

        public void print() {
            logger.info("write bit_val[{}] bit_binary[{}] bit_hex[{}] bit_pos[{}-{}]", val, getLogBit(), getLogHex(), bitStart, bitEnd);
        }
    }

    public final LogRes write_log(long l, int bit) {
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);

        final LogRes logRes = new LogRes(byteLen, bitOffset, bitOffset + bit - 1, l);

        final int left = 8 - (temp & 7);
        final long newL = l << left;
        if (bitOffset == 0) {
            b = (byte) (newL >> ((byteLen - 1) << 3));
        } else {
            b |= (byte) (newL >> ((byteLen - 1) << 3));
        }
        logRes.bytes[0] = b;
        for (int i = 1; i < byteLen; i++) {
            byteBuf.writeByte(b);
            b = (byte) (newL >> ((byteLen - i - 1) << 3));
            logRes.bytes[i] = b;
        }
        bitOffset = temp & 7;
        if (bitOffset == 0) {
            byteBuf.writeByte(b);
        }

        return logRes;
    }

    public final void write(long l, int bit) {
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);
        final int left = 8 - (temp & 7);
        final long newL = l << left;
        if (bitOffset == 0) {
            b = (byte) (newL >> ((byteLen - 1) << 3));
        } else {
            b |= (byte) (newL >> ((byteLen - 1) << 3));
        }
        for (int i = 1; i < byteLen; i++) {
            byteBuf.writeByte(b);
            b = (byte) (newL >> ((byteLen - i - 1) << 3));
        }
        bitOffset = temp & 7;
        if (bitOffset == 0) {
            byteBuf.writeByte(b);
        }
    }
}

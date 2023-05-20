package com.bcd.base.support_parser.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class BitBuf_writer {
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
        for (int i = 0; i < 100000000; i++) {
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
            bitBufWriter.write(4, 3);
            bitBufWriter.write(0, 3);
            bitBufWriter.write(114, 9);
            bitBufWriter.write(114, 9);
            bitBufWriter.write(114, 9);
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

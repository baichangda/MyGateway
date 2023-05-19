package com.bcd.base.support_parser.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class BitBuf_writer {
    private final ByteBuf byteBuf;

    private final ByteBuf cache = Unpooled.buffer();

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
            final ByteBuf bb = Unpooled.buffer();
            final BitBuf_writer bitBufWriter = BitBuf_writer.newBitBuf(bb);
            bitBufWriter.write(1L, 1);
            bitBufWriter.write(28900L, 15);
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
        }
        System.out.println(System.currentTimeMillis() - t1);

//        final ByteBuf bb = Unpooled.buffer();
//        final BitBuf_writer bitBufWriter = BitBuf_writer.newBitBuf(bb);
//        bitBufWriter.write(4, 3);
//        bitBufWriter.write(0, 3);
//        bitBufWriter.write(114, 9);
//        bitBufWriter.finish();
//        System.out.println(ByteBufUtil.hexDump(bb));
    }

    public void finish() {
        byteBuf.writeBytes(cache);
        cache.clear();
        bitOffset = 0;
    }

    public void write(long l, int bit) {
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);
        final int left = (byteLen << 3) - bitOffset - bit;
        final long newL = l << left;

        if (bitOffset == 0) {
            cache.writeByte((byte) (newL >> ((byteLen - 1) << 3)));
        } else {
            cache.setByte(cache.writerIndex(), (byte) (newL >> ((byteLen - 1) << 3)));
        }
        for (int i = 1; i < byteLen; i++) {
            cache.writeByte((byte) (newL >> ((byteLen - i - 1) << 3)));
        }

        bitOffset = temp & 7;

        checkBuf();
    }

    private void checkBuf() {
        if (bitOffset == 0) {
            byteBuf.writeBytes(cache);
        } else {
            byteBuf.writeBytes(cache, cache.readableBytes() - 1);
        }
    }
}

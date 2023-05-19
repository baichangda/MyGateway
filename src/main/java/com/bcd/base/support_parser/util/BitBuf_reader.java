package com.bcd.base.support_parser.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BitBuf_reader {
    private final ByteBuf byteBuf;
    private final ByteBuf cache = Unpooled.buffer();
    //当前readIndex的bit偏移量
    private int bitOffset = 0;

    public static BitBuf_reader newBitBuf(ByteBuf byteBuf) {
        return new BitBuf_reader(byteBuf);
    }

    private BitBuf_reader(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public static void main(String[] args) {
//        final long t1 = System.currentTimeMillis();
//        final byte[] source = {
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4,
//                (byte) 0xF0, (byte) 0xe4
//        };
//        for (int i = 0; i < 1; i++) {
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
//        }
//        System.out.println(System.currentTimeMillis() - t1);


        final byte[] source2 = {
                (byte) 0x80, (byte) 0xe4,
        };
        ByteBuf bb2 = Unpooled.wrappedBuffer(source2);
        BitBuf_reader bitBuf2 = BitBuf_reader.newBitBuf(bb2);
        final long l1 = bitBuf2.read(3);
        final long l2 = bitBuf2.read(3);
        final long l3 = bitBuf2.read(9);
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l3);
    }

    public void finish() {
        cache.clear();
        bitOffset = 0;
    }

    public long read(int bit) {
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);
        //检查buf是否足够、不够则从byteBuf中读取
        checkCache(byteLen);

        long c = (cache.getByte(cache.readerIndex()) & 0xffL) << ((byteLen - 1) * 8);
        for (int i = 1; i < byteLen; i++) {
            c |= (cache.getByte(cache.readerIndex() + i) & 0xffL) << ((byteLen - 1 - i) << 3);
        }
        final int right = byteLen * 8 - bitOffset - bit;

        if (temp >= 8) {
            cache.skipBytes(temp >> 3);
            bitOffset = temp & 7;
        } else {
            bitOffset = temp;
        }


        return (c >>> right) & ((0x01L << bit) - 1);
    }

    private void checkCache(int byteLen) {
        final int readableBytes = cache.readableBytes();
        if (byteLen > readableBytes) {
            cache.writeBytes(byteBuf, byteLen - readableBytes);
        }
    }
}

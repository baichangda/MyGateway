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
            final WriteLog res1 = bitBufWriter.write_log(-4, 3, false);
            final WriteLog res2 = bitBufWriter.write_log(0, 3, true);
            final SkipLog skip1 = bitBufWriter.skip_log(3);
            final WriteLog res3 = bitBufWriter.write_log(-55, 9, false);
            res1.print();
            res2.print();
            skip1.print();
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
            logger.info("skip bit_binary[{}] bit_hex[{}] bit_pos[{}-{}]", getLogBit(), getLogHex(), bitStart, bitEnd);
        }
    }

    public static class WriteLog extends Log {
        public long val;

        public final boolean unsigned;

        public WriteLog(int byteLen, int bitStart, int bitEnd, long val, boolean unsigned) {
            super(byteLen, bitStart, bitEnd);
            this.val = val;
            this.unsigned = unsigned;
        }

        public void print() {
            logger.info("write bit_val[{}] bit_binary[{},{}] bit_hex[{}] bit_pos[{}-{}]", val, unsigned ? "u" : "s", getLogBit(), getLogHex(), bitStart, bitEnd);
        }
    }


    public final void write(long l, int bit, boolean unsigned) {
        if (!unsigned && l < 0) {
            l = (-l) & ((0x01L << bit) - 1);
        }
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);
        final int left = (byteLen << 3) - temp;
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


    public final WriteLog write_log(long l, int bit, boolean unsigned) {
        if (!unsigned && l < 0) {
            l = (-l) & ((0x01L << bit) - 1);
        }
        final int temp = bit + bitOffset;
        final int byteLen = (temp >> 3) + ((temp & 7) == 0 ? 0 : 1);

        final WriteLog logRes = new WriteLog(byteLen, bitOffset, bitOffset + bit - 1, l, unsigned);
        final int left = (byteLen << 3) - temp;
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


    public final void skip(int bit) {
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        if (byteLen == 1) {
            if (newBitOffsetZero) {
                b = 0;
            }
        } else {
            if (bitOffset == 0) {
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen);
                } else {
                    byteBuf.writeZero(byteLen - 1);
                }

            } else {
                byteBuf.writeByte(b);
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen - 1);
                } else {
                    byteBuf.writeZero(byteLen - 2);
                }
            }
            b = 0;
        }
        bitOffset = temp & 7;
    }

    public final SkipLog skip_log(int bit) {
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        final SkipLog log = new SkipLog(byteLen, bitOffset, bitOffset + bit - 1);

        if (byteLen == 1) {
            if (newBitOffsetZero) {
                b = 0;
            }
        } else {
            if (bitOffset == 0) {
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen);
                } else {
                    byteBuf.writeZero(byteLen - 1);
                }

            } else {
                log.bytes[0] = b;
                byteBuf.writeByte(b);
                if (newBitOffsetZero) {
                    byteBuf.writeZero(byteLen - 1);
                } else {
                    byteBuf.writeZero(byteLen - 2);
                }
            }
            b = 0;
        }

        bitOffset = temp & 7;

        return log;
    }
}

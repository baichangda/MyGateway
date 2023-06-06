package com.bcd.base.support_parser.util;

import com.bcd.base.support_parser.Parser;
import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBuf_writer {

    public final static boolean default_bigEndian = true;
    public final static boolean default_unsigned = true;

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
            final WriteLog res1 = bitBufWriter.write_log(4, 3, true, true);
            final WriteLog res2 = bitBufWriter.write_log(0, 3, true, true);
            final SkipLog skip1 = bitBufWriter.skip_log(3);
            final WriteLog res3 = bitBufWriter.write_log(-217, 9, false, false);
            res1.print(null);
            res2.print(null);
            skip1.print(null);
            res3.print(null);
            bitBufWriter.finish();
//            System.out.println(ByteBufUtil.hexDump(bb));
        }
        System.out.println(System.currentTimeMillis() - t1);
    }

    public final void finish() {
        if (bitOffset > 0) {
            byteBuf.writeByte(b);
        }
        b = 0;
        bitOffset = 0;
    }

    public final FinishLog finish_log() {
        final FinishLog finishLog;
        if (bitOffset == 0) {
            finishLog = new FinishLog();
        } else {
            byteBuf.writeByte(b);
            finishLog = new FinishLog(1, bitOffset, 8 - bitOffset);
            finishLog.bytes[0] = b;
        }
        b = 0;
        bitOffset = 0;
        return finishLog;
    }

    public static abstract class Log {
        public final byte[] bytes;

        public final int bitStart;
        public final int bit;

        public final int bitEnd;

        public Log(int byteLen, int bitStart, int bit) {
            this.bytes = new byte[byteLen];
            this.bitStart = bitStart;
            this.bit = bit;
            this.bitEnd = bitStart + bit - 1;
        }


        public final String getLogHex() {
            return ByteBufUtil.hexDump(bytes) + ((bitEnd & 7) == 7 ? "" : "?");
        }

        public abstract void print(String logPrefix);

    }

    public static class FinishLog extends Log {

        public boolean write;

        public FinishLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
            this.write = true;
        }

        public FinishLog() {
            super(0, 0, 0);
            this.write = false;
        }

        public final String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }

        public final void print(String logPrefix) {
            if (write) {
                Parser.logger.info("{} finish write bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", logPrefix == null ? "" : logPrefix, getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
            } else {
                Parser.logger.info("{} finish no write", logPrefix == null ? "" : logPrefix);
            }
        }
    }

    public static class SkipLog extends Log {
        public SkipLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
        }

        public final void print(String logPrefix) {
            Parser.logger.info("{} skip bit_hex[{}] bit_pos[{}-{}]", logPrefix == null ? "" : logPrefix, getLogHex().toUpperCase(), bitStart, bitEnd);
        }
    }

    public static class WriteLog extends Log {
        public long val1;
        public boolean signed1;
        public long val2;
        public long val3;

        public final boolean bigEndian;
        public final boolean unsigned;

        public WriteLog(int byteLen, int bitStart, int bit, boolean bigEndian, boolean unsigned) {
            super(byteLen, bitStart, bit);
            this.bigEndian = bigEndian;
            this.unsigned = unsigned;
        }

        public final String getLogBit(long l, boolean signed) {
            if (signed) {
                return "-" + Strings.padStart(Long.toBinaryString(-l), bit, '0');
            } else {
                return Strings.padStart(Long.toBinaryString(l), bit, '0');
            }
        }

        public final void print(String logPrefix) {
            Parser.logger.info("{} write bit_unsigned[{}] bit_bigEndian[{}] bit_val[{}->{}->{}] bit_binary[{}->{}->{}] bit_hex[{}] bit_pos[{}-{}]",
                    logPrefix == null ? "" : logPrefix,
                    unsigned ? "yes" : "no",
                    bigEndian ? "yes" : "no",
                    val1, val2, val3,
                    getLogBit(val1, signed1), getLogBit(val2, false), getLogBit(val3, false),
                    getLogHex().toUpperCase(),
                    bitStart, bitEnd
            );
        }
    }

    public final void write(long l, int bit) {
        write(l, bit, default_bigEndian, default_unsigned);
    }

    public final WriteLog write_log(long l, int bit) {
        return write_log(l, bit, default_bigEndian, default_unsigned);
    }


    public final void write(long l, int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        int bitOffset = this.bitOffset;
        byte b = this.b;
        if (!unsigned && l < 0) {
            l = l & ((0x01L << bit) - 1);
        }
        if (!bigEndian) {
            l = Long.reverse(l) >>> (64 - bit);
        }
        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final long newL;
        final int byteLen;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
            newL = l;
        } else {
            byteLen = (temp >> 3) + 1;
            newL = l << (8 - finalBitOffset);
        }
        if (bitOffset == 0) {
            b = (byte) (newL >> ((byteLen - 1) << 3));
        } else {
            b |= (byte) (newL >> ((byteLen - 1) << 3));
        }
        for (int i = 1; i < byteLen; i++) {
            byteBuf.writeByte(b);
            b = (byte) (newL >> ((byteLen - i - 1) << 3));
        }
        bitOffset = finalBitOffset;
        if (bitOffset == 0) {
            byteBuf.writeByte(b);
        }

        this.bitOffset = bitOffset;
        this.b = b;
    }


    public final WriteLog write_log(long l, int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        int bitOffset = this.bitOffset;
        byte b = this.b;

        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final int byteLen;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
        } else {
            byteLen = (temp >> 3) + 1;
        }

        final WriteLog logRes = new WriteLog(byteLen, bitOffset, bit, bigEndian, unsigned);

        logRes.val1 = l;

        if (!unsigned && l < 0) {
            logRes.signed1 = true;
            l = l & ((0x01L << bit) - 1);
        }

        logRes.val2 = l;

        if (!bigEndian) {
            l = Long.reverse(l) >>> (64 - bit);
        }

        logRes.val3 = l;

        final long newL;
        if (finalBitOffset == 0) {
            newL = l;
        } else {
            newL = l << (8 - finalBitOffset);
        }
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

        this.bitOffset = bitOffset;
        this.b = b;

        return logRes;
    }


    public final void skip(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;
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
        this.bitOffset = temp & 7;
        this.b = b;
    }

    public final SkipLog skip_log(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;
        final int temp = bit + bitOffset;
        final boolean newBitOffsetZero = (temp & 7) == 0;
        final int byteLen = (temp >> 3) + (newBitOffsetZero ? 0 : 1);
        final SkipLog log = new SkipLog(byteLen, bitOffset, bit);

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

        this.bitOffset = temp & 7;
        this.b = b;

        return log;
    }
}

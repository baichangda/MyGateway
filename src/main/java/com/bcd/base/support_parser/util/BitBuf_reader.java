package com.bcd.base.support_parser.util;

import com.bcd.base.support_parser.Parser;
import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBuf_reader {

    public final static boolean default_bigEndian = true;
    public final static boolean default_unsigned = true;


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
//            final ReadLog res1 = bitBuf2.read_log(3, true, true);
//            final ReadLog res2 = bitBuf2.read_log(3, true, true);
//            final SkipLog skip1 = bitBuf2.skip_log(3);
//            final ReadLog res3 = bitBuf2.read_log(9, false, false);
            final long res1 = bitBuf2.read(3, true, true);
            final long res2 = bitBuf2.read(3, true, true);
            bitBuf2.skip(3);
            final long res3 = bitBuf2.read(9, false, false);
//            res1.print(null);
//            res2.print(null);
//            skip1.print(null);
//            res3.print(null);
            System.out.println(res1);
            System.out.println(res2);
            System.out.println(res3);
        }
        System.out.println(System.currentTimeMillis() - t1);

    }

    public final void finish() {
        b = 0;
        bitOffset = 0;
    }

    public final FinishLog finish_log() {
        final FinishLog finishLog;
        if (bitOffset == 0) {
            finishLog = new FinishLog();
        } else {
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

        public boolean skip;

        public FinishLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
            this.skip = true;
        }

        public FinishLog() {
            super(0, 0, 0);
            this.skip = false;
        }

        public final String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }

        public final void print(String logPrefix) {
            if (skip) {
                Parser.logger.info("{} finish skip bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", logPrefix==null?"":logPrefix, getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
            } else {
                Parser.logger.info("{} finish no skip", logPrefix==null?"":logPrefix);
            }
        }
    }

    public static class SkipLog extends Log {

        public SkipLog(int byteLen, int bitStart, int bit) {
            super(byteLen, bitStart, bit);
        }

        public final String getLogBit() {
            final StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Strings.padStart(Integer.toBinaryString(b & 0xff), 8, '0'));
            }
            return sb.substring(bitStart, bitEnd + 1);
        }

        public final void print(String logPrefix) {
            Parser.logger.info("{} skip bit_hex[{}] bit_pos[{}-{}] bit_binary[{}]", logPrefix==null?"":logPrefix, getLogHex().toUpperCase(), bitStart, bitEnd, getLogBit());
        }
    }

    public static class ReadLog extends Log {

        public final boolean unsigned;

        public final boolean bigEndian;
        //原始值
        public long val1;
        //处理大小端之后的值
        public long val2;
        //是否有符号
        public boolean signed3;
        //最终解析值
        public long val3;

        public ReadLog(int byteLen, int bitStart, int bit, boolean bigEndian, boolean unsigned) {
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
            Parser.logger.info("{} read bit_hex[{}] bit_pos[{}-{}] bit_bigEndian[{}] bit_unsigned[{}] bit_binary[{}->{}->{}] bit_val[{}->{}->{}]",
                    logPrefix==null?"":logPrefix,
                    getLogHex().toUpperCase(),
                    bitStart, bitEnd,
                    bigEndian ? "yes" : "no",
                    unsigned ? "yes" : "no",
                    getLogBit(val1, false), getLogBit(val2, false), getLogBit(val3, signed3),
                    val1, val2, val3
            );
        }
    }

    public final long read(int bit) {
        return read(bit, default_bigEndian, default_unsigned);
    }

    public final ReadLog read_log(int bit) {
        return read_log(bit, default_bigEndian, default_unsigned);
    }


    public final long read(int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b;
        if (bitOffset == 0) {
            b = byteBuf.readByte();
        } else {
            b = this.b;
        }

        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final int byteLen;
        long l;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
            l = (b & 0xffL) << (temp - 8);
        } else {
            byteLen = (temp >> 3) + 1;
            l = (b & 0xffL) << (temp - finalBitOffset);
        }
        for (int i = 1; i < byteLen; i++) {
            b = byteBuf.readByte();
            l |= ((b & 0xffL) << ((byteLen - 1 - i) << 3));
        }

        this.bitOffset = finalBitOffset;
        this.b = b;

        //如果是小端模式、则翻转bit
        final long cRight;
        if (bigEndian) {
            cRight = l >>> ((byteLen << 3) - bitOffset - bit);
        } else {
            cRight = Long.reverse(l) >>> (64 - (byteLen << 3) + bitOffset);
        }

        if (!unsigned && ((cRight >> (bit - 1)) & 0x01) == 1) {
            return cRight | (-1L << bit);
        } else {
            return cRight & ((0x01L << bit) - 1);
        }
    }

    public final ReadLog read_log(int bit, boolean bigEndian, boolean unsigned) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b;
        if (bitOffset == 0) {
            b = byteBuf.readByte();
        } else {
            b = this.b;
        }

        final int temp = bit + bitOffset;
        final int finalBitOffset = temp & 7;
        final int byteLen;
        long l;
        if (finalBitOffset == 0) {
            byteLen = temp >> 3;
            l = (b & 0xffL) << (temp - 8);
        } else {
            byteLen = (temp >> 3) + 1;
            l = (b & 0xffL) << (temp - finalBitOffset);
        }

        final ReadLog log = new ReadLog(byteLen, bitOffset, bit, bigEndian, unsigned);
        log.bytes[0] = b;

        for (int i = 1; i < byteLen; i++) {
            b = byteBuf.readByte();
            log.bytes[i] = b;
            l |= (b & 0xffL) << ((byteLen - 1 - i) << 3);
        }

        this.bitOffset = finalBitOffset;
        this.b = b;

        log.val1 = (l >>> (byteLen * 8 - bitOffset - bit)) & ((0x01L << bit) - 1);

        //如果是小端模式、则翻转bit
        final long cRight;
        if (bigEndian) {
            cRight = l >>> ((byteLen << 3) - bitOffset - bit);
        } else {
            cRight = Long.reverse(l) >>> (64 - (byteLen << 3) + bitOffset);
        }

        log.val2 = cRight & ((0x01L << bit) - 1);

        if (!unsigned && ((cRight >> (bit - 1)) & 0x01) == 1) {
            log.val3 = cRight | (-1L << bit);
            log.signed3 = true;
        } else {
            log.val3 = cRight & ((0x01L << bit) - 1);
        }
        return log;
    }


    public final void skip(int bit) {
        final ByteBuf byteBuf = this.byteBuf;
        final int bitOffset = this.bitOffset;
        byte b = this.b;

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

        this.bitOffset = temp & 7;
        this.b = b;

        return log;
    }

}

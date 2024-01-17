package com.bcd.share.support_parser.util;

public class BcdUtil {
    public static final char[] BCD_DUMP_TABLE = new char[308];

    static {
        final char[] DIGITS = "0123456789".toCharArray();
        for (char c1 : DIGITS) {
            int n1 = Character.getNumericValue(c1);
            for (char c2 : DIGITS) {
                int n2 = Character.getNumericValue(c2);
                int i = ((n1 << 4) | n2) << 1;
                BCD_DUMP_TABLE[i] = c1;
                BCD_DUMP_TABLE[i + 1] = c2;
            }
        }
    }

    public static String bytesToString(byte[] bytes) {
        char[] chars = new char[bytes.length << 1];
        for (int i = 0; i < bytes.length; i++) {
            System.arraycopy(BCD_DUMP_TABLE, (bytes[i]&0xff) << 1, chars, i << 1, 2);
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        for (int i = 0; i < BCD_DUMP_TABLE.length; i++) {
            System.out.println(i + " " + BCD_DUMP_TABLE[i]);
        }
        System.out.println(bytesToString(new byte[]{(byte) 133, (byte) 153}));
        System.out.println(bytesToString(new byte[]{(byte) 66, (byte) 87}));
        System.out.println(bytesToString(new byte[]{(byte) 71, (byte) 99}));
    }
}

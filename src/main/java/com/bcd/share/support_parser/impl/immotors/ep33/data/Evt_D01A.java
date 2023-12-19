package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01A extends Evt_4_x {
    @F_bit_num(len = 16)
    public int iEcuSts;
    @F_bit_num(len = 8)
    public short iIAMInterSts;
    @F_bit_num(len = 8)
    public short iMpuIPTableRuleSts;
    @F_bit_num(len = 8)
    public short iModemIPTableRuleSts;
    @F_bit_num(len = 8)
    public short iARPRuleSts;
    @F_bit_num(len = 16)
    public int iICC2PHYSGMIISts;
    @F_bit_num(len = 16)
    public int iMpuRGMIISts;
    @F_bit_num(len = 16)
    public int iModemRGMIISts;
    @F_bit_num(len = 16)
    public int iSwitchSGMIISts;
    @F_bit_num(len = 8)
    public short iUSBConnSts;
    @F_bit_num(len = 8)
    public short iIPASts;
    @F_bit_num(len = 8)
    public short iAPSts;
    @F_string(len = 28)
    public byte networkbackupinfo;
}

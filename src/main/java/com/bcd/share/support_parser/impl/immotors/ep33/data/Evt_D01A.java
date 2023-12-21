package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_string;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D01A extends Evt_4_x {
    @F_num(type = NumType.uint16)
    public int iEcuSts;
    @F_num(type = NumType.uint8)
    public short iIAMInterSts;
    @F_num(type = NumType.uint8)
    public short iMpuIPTableRuleSts;
    @F_num(type = NumType.uint8)
    public short iModemIPTableRuleSts;
    @F_num(type = NumType.uint8)
    public short iARPRuleSts;
    @F_num(type = NumType.uint16)
    public int iICC2PHYSGMIISts;
    @F_num(type = NumType.uint16)
    public int iMpuRGMIISts;
    @F_num(type = NumType.uint16)
    public int iModemRGMIISts;
    @F_num(type = NumType.uint16)
    public int iSwitchSGMIISts;
    @F_num(type = NumType.uint8)
    public short iUSBConnSts;
    @F_num(type = NumType.uint8)
    public short iIPASts;
    @F_num(type = NumType.uint8)
    public short iAPSts;
    @F_string(len = 28)
    public String networkbackupinfo;
}

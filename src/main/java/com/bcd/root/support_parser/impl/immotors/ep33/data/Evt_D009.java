package com.bcd.root.support_parser.impl.immotors.ep33.data;


import com.bcd.root.support_parser.anno.F_bit_num;
import com.bcd.root.support_parser.anno.F_bit_skip;
import com.bcd.root.support_parser.anno.NumType;
import com.bcd.root.support_parser.impl.immotors.Evt_4_x;

public class Evt_D009 extends Evt_4_x {
    @F_bit_num(len = 2, valType = NumType.uint8)
    public byte BMSCMUFlt;
    @F_bit_num(len = 2, valType = NumType.uint8)
    public byte BMSCellVoltFlt;
    @F_bit_num(len = 2, valType = NumType.uint8)
    public byte BMSPackTemFlt;
    @F_bit_num(len = 2, valType = NumType.uint8)
    public byte BMSPackVoltFlt;
    @F_bit_num(len = 6, valType = NumType.uint8)
    public byte BMSWrnngInfo;
    @F_bit_num(len = 6, valType = NumType.uint8)
    public byte BMSWrnngInfoPV;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte BMSWrnngInfoRC;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPreThrmFltInd;
    @F_bit_skip(len = 5)
    public byte skip1;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte BMSKeepSysAwkScene;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSTemOverDifAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSOverTemAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSOverPackVolAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSUnderPackVolAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSHVILAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSOverCellVolAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSUnderCellVolAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSLowSOCAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSJumpngSOCAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSHiSOCAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSPackVolMsmchAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSPoorCellCnstncyAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSCellOverChrgdAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSLowPtIsltnRstcAlrm;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short TMRtrTem;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte TMStrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte TMInvtrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte ISCStrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte ISCInvtrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte SAMStrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte SAMInvtrOvTempAlrm;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte EPTHVDCDCMdReq;
    @F_bit_num(len = 6, valType = NumType.uint8)
    public byte VCUSecyWrnngInfo;
    @F_bit_num(len = 6, valType = NumType.uint8)
    public byte VCUSecyWrnngInfoPV;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte VCUSecyWrnngInfoRC;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short VCUSecyWrnngInfoCRC;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSOnbdChrgSpRsn;
}

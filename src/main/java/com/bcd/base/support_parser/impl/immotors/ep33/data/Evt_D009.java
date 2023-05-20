package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_skip;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D009 extends Evt_4_x {
    @F_integer(bit = 2)
    public byte BMSCMUFlt;
    @F_integer(bit = 2)
    public byte BMSCellVoltFlt;
    @F_integer(bit = 2)
    public byte BMSPackTemFlt;
    @F_integer(bit = 2)
    public byte BMSPackVoltFlt;
    @F_integer(bit = 6)
    public byte BMSWrnngInfo;
    @F_integer(bit = 6)
    public byte BMSWrnngInfoPV;
    @F_integer(bit = 4)
    public byte BMSWrnngInfoRC;
    @F_integer(bit = 1)
    public byte BMSPreThrmFltInd;
    @F_skip(bit = 5)
    public byte skip1;
    @F_integer(bit = 4)
    public byte BMSKeepSysAwkScene;
    @F_integer(bit = 3)
    public byte BMSTemOverDifAlrm;
    @F_integer(bit = 3)
    public byte BMSOverTemAlrm;
    @F_integer(bit = 3)
    public byte BMSOverPackVolAlrm;
    @F_integer(bit = 3)
    public byte BMSUnderPackVolAlrm;
    @F_integer(bit = 3)
    public byte BMSHVILAlrm;
    @F_integer(bit = 3)
    public byte BMSOverCellVolAlrm;
    @F_integer(bit = 3)
    public byte BMSUnderCellVolAlrm;
    @F_integer(bit = 3)
    public byte BMSLowSOCAlrm;
    @F_integer(bit = 3)
    public byte BMSJumpngSOCAlrm;
    @F_integer(bit = 3)
    public byte BMSHiSOCAlrm;
    @F_integer(bit = 3)
    public byte BMSPackVolMsmchAlrm;
    @F_integer(bit = 3)
    public byte BMSPoorCellCnstncyAlrm;
    @F_integer(bit = 3)
    public byte BMSCellOverChrgdAlrm;
    @F_integer(bit = 3)
    public byte BMSLowPtIsltnRstcAlrm;
    @F_integer(bit = 8,valExpr = "x-40")
    public short TMRtrTem;
    @F_integer(bit = 3)
    public byte TMStrOvTempAlrm;
    @F_integer(bit = 3)
    public byte TMInvtrOvTempAlrm;
    @F_integer(bit = 3)
    public byte ISCStrOvTempAlrm;
    @F_integer(bit = 3)
    public byte ISCInvtrOvTempAlrm;
    @F_integer(bit = 3)
    public byte SAMStrOvTempAlrm;
    @F_integer(bit = 3)
    public byte SAMInvtrOvTempAlrm;
    @F_integer(bit = 3)
    public byte EPTHVDCDCMdReq;
    @F_integer(bit = 6)
    public byte VCUSecyWrnngInfo;
    @F_integer(bit = 6)
    public byte VCUSecyWrnngInfoPV;
    @F_integer(bit = 4)
    public byte VCUSecyWrnngInfoRC;
    @F_integer(bit = 8)
    public short VCUSecyWrnngInfoCRC;
    @F_integer(bit = 8)
    public short BMSOnbdChrgSpRsn;
}

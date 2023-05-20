package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_2_6;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D006 extends Evt_4_x {
    @F_integer(bit = 1)
    public byte EPTRdy;
    @F_integer(bit = 5)
    public byte BMSBscSta;
    @F_integer(bit = 16, valExpr = "x*0.05-1000")
    public float BMSPackCrnt;
    @F_integer(bit = 1)
    public byte BMSPackCrntV;
    @F_integer(bit = 10, valExpr = "x*0.1")
    public float BMSPackSOC;
    @F_integer(bit = 1)
    public byte BMSPackSOCV;
    @F_integer(bit = 10, valExpr = "x*0.1")
    public float BMSPackSOCDsp;
    @F_integer(bit = 1)
    public byte BMSPackSOCDspV;
    @F_integer(bit = 4)
    public byte ElecVehSysMd;
    @F_integer(bit = 12, valExpr = "x*0.25")
    public float BMSPackVol;
    @F_integer(bit = 1)
    public byte BMSPackVolV;
    @F_integer(bit = 3)
    public byte HVDCDCSta;
    @F_integer(bit = 12, valExpr = "x*0.5-848")
    public float EPTTrInptShaftToq;
    @F_integer(bit = 1)
    public byte EPTTrInptShaftToqV;
    @F_integer(bit = 12, valExpr = "x*2-3392")
    public short EPTTrOtptShaftToq;
    @F_integer(bit = 1)
    public byte EPTTrOtptShaftToqV;
    @F_integer(bit = 1)
    public byte EPTBrkPdlDscrtInptSts;
    @F_integer(bit = 1)
    public byte EPTBrkPdlDscrtInptStsV;
    @F_integer(bit = 1)
    public byte BrkSysBrkLghtsReqd;
    @F_integer(bit = 1)
    public byte EPBSysBrkLghtsReqd;
    @F_integer(bit = 1)
    public byte EPBSysBrkLghtsReqdA;
    @F_integer(bit = 14, valExpr = "x*0.5")
    public float BMSPtIsltnRstc;
    @F_integer(bit = 8, valExpr = "x*0.392157")
    public float EPTAccelActuPos;
    @F_integer(bit = 1)
    public byte EPTAccelActuPosV;
    @F_integer(bit = 1)
    public byte TMInvtrCrntV;
    @F_integer(bit = 11, valExpr = "x-1024")
    public short TMInvtrCrnt;
    @F_integer(bit = 1)
    public byte ISGInvtrCrntV;
    @F_integer(bit = 11, valExpr = "x-1024")
    public short ISGInvtrCrnt;
    @F_integer(bit = 11, valExpr = "x-1024")
    public short SAMInvtrCrnt;
    @F_integer(bit = 1)
    public byte SAMInvtrCrntV;
    @F_integer(bit = 4)
    public byte TMSta;
    @F_integer(bit = 4)
    public byte ISGSta;
    @F_integer(bit = 4)
    public byte SAMSta;
    @F_integer(bit = 8, valExpr = "x-40")
    public short TMInvtrTem;
    @F_integer(bit = 8, valExpr = "x-40")
    public short ISGInvtrTem;
    @F_integer(bit = 8, valExpr = "x-40")
    public short SAMInvtrTem;
    @F_integer(bit = 16, valExpr = "x-32768")
    public int TMSpd;
    @F_integer(bit = 1)
    public byte TMSpdV;
    @F_integer(bit = 16, valExpr = "x-32768")
    public int ISGSpd;
    @F_integer(bit = 1)
    public byte ISGSpdV;
    @F_integer(bit = 1)
    public byte SAMSpdV;
    @F_integer(bit = 16, valExpr = "x-32768")
    public int SAMSpd;
    @F_integer(bit = 11, valExpr = "x*0.5-512")
    public float TMActuToq;
    @F_integer(bit = 1)
    public byte TMActuToqV;
    @F_integer(bit = 11, valExpr = "x*0.5-512")
    public float ISGActuToq;
    @F_integer(bit = 1)
    public byte ISGActuToqV;
    @F_integer(bit = 1)
    public byte SAMActuToqV;
    @F_integer(bit = 11, valExpr = "x*0.5-512")
    public float SAMActuToq;
    @F_integer(bit = 8, valExpr = "x-40")
    public short TMSttrTem;
    @F_integer(bit = 8, valExpr = "x-40")
    public short ISGSttrTem;
    @F_integer(bit = 8, valExpr = "x-40")
    public short SAMSttrTem;
    @F_integer(bit = 10)
    public short HVDCDCHVSideVol;
    @F_integer(bit = 1)
    public byte HVDCDCHVSideVolV;
    @F_integer(bit = 8, valExpr = "x*0.1")
    public float AvgFuelCsump;
    @F_integer(bit = 1)
    public byte TMInvtrVolV;

    @F_integer(bit = 10)
    public short TMInvtrVol;
    @F_integer(bit = 1)
    public byte ISGInvtrVolV;
    @F_integer(bit = 10)
    public short ISGInvtrVol;
    @F_integer(bit = 1)
    public byte SAMInvtrVolV;
    @F_integer(bit = 10)
    public short SAMInvtrVol;
    @F_integer(bit = 8)
    public short BMSCellMaxTemIndx;
    @F_integer(bit = 8, valExpr = "x*0.5-40")
    public float BMSCellMaxTem;
    @F_integer(bit = 1)
    public byte BMSCellMaxTemV;
    @F_integer(bit = 8)
    public short BMSCellMinTemIndx;

    @F_integer(bit = 8, valExpr = "x*0.5-40")
    public float BMSCellMinTem;
    @F_integer(bit = 1)
    public byte BMSCellMinTemV;
    @F_integer(bit = 8)
    public short BMSCellMaxVolIndx;
    @F_integer(bit = 13, valExpr = "x*0.001")
    public float BMSCellMaxVol;
    @F_integer(bit = 1)
    public byte BMSCellMaxVolV;
    @F_integer(bit = 8)
    public short BMSCellMinVolIndx;
    @F_integer(bit = 13, valExpr = "x*0.001")
    public float BMSCellMinVol;
    @F_integer(bit = 1)
    public byte BMSCellMinVolV;
    @F_integer(bit = 1)
    public byte BMSPtIsltnRstcV;
    @F_integer(bit = 8, valExpr = "x-40")
    public short HVDCDCTem;
    @F_integer(bit = 1)
    public byte BrkFludLvlLow;
    @F_integer(bit = 1)
    public byte BrkSysRedBrkTlltReq;
    @F_integer(bit = 1)
    public byte ABSF;
    @F_integer(bit = 3)
    public byte VSESts;
    @F_integer(bit = 1)
    public byte IbstrWrnngIO;
    @F_integer(bit = 1)
    public byte BMSHVILClsd;
    @F_integer(bit = 12, valExpr = "x*0.5-848")
    public float EPTTrOtptShaftTotToq;
    @F_integer(bit = 1)
    public byte EPTTrOtptShaftTotToqV;
    @F_integer(bit = 1)
    public byte BrkFludLvlLowV;
    @F_integer(bit = 16, valExpr = "x*0.25")
    public float EnSpd;
    @F_integer(bit = 2)
    public byte EnSpdSts;
    @F_integer(bit = 12, valExpr = "x*16")
    public int FuelCsump;

}

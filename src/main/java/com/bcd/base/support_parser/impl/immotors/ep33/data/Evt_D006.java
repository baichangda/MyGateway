package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.NumType;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D006 extends Evt_4_x {
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTRdy;
    @F_bit_num(len = 5, valType = NumType.uint8)
    public byte BMSBscSta;
    @F_bit_num(len = 16, valType = NumType.float32, valExpr = "x*0.05-1000")
    public float BMSPackCrnt;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPackCrntV;
    @F_bit_num(len = 10, valType = NumType.float32, valExpr = "x*0.1")
    public float BMSPackSOC;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPackSOCV;
    @F_bit_num(len = 10, valType = NumType.float32, valExpr = "x*0.1")
    public float BMSPackSOCDsp;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPackSOCDspV;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte ElecVehSysMd;
    @F_bit_num(len = 12, valType = NumType.float32, valExpr = "x*0.25")
    public float BMSPackVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPackVolV;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte HVDCDCSta;
    @F_bit_num(len = 12, valType = NumType.float32, valExpr = "x*0.5-848")
    public float EPTTrInptShaftToq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTTrInptShaftToqV;
    @F_bit_num(len = 12, valType = NumType.int16, valExpr = "x*2-3392")
    public short EPTTrOtptShaftToq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTTrOtptShaftToqV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTBrkPdlDscrtInptSts;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTBrkPdlDscrtInptStsV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BrkSysBrkLghtsReqd;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPBSysBrkLghtsReqd;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPBSysBrkLghtsReqdA;
    @F_bit_num(len = 14, valType = NumType.float32, valExpr = "x*0.5")
    public float BMSPtIsltnRstc;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.392157")
    public float EPTAccelActuPos;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTAccelActuPosV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte TMInvtrCrntV;
    @F_bit_num(len = 11, valType = NumType.int16, valExpr = "x-1024")
    public short TMInvtrCrnt;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte ISGInvtrCrntV;
    @F_bit_num(len = 11, valType = NumType.int16, valExpr = "x-1024")
    public short ISGInvtrCrnt;
    @F_bit_num(len = 11, valType = NumType.int16, valExpr = "x-1024")
    public short SAMInvtrCrnt;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte SAMInvtrCrntV;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte TMSta;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte ISGSta;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte SAMSta;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short TMInvtrTem;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short ISGInvtrTem;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short SAMInvtrTem;
    @F_bit_num(len = 16, valType = NumType.int32, valExpr = "x-32768")
    public short TMSpd;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte TMSpdV;
    @F_bit_num(len = 16, valType = NumType.int32, valExpr = "x-32768")
    public short ISGSpd;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte ISGSpdV;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte SAMSpdV;
    @F_bit_num(len = 16, valType = NumType.int32, valExpr = "x-32768")
    public short SAMSpd;
    @F_bit_num(len = 11, valType = NumType.float32, valExpr = "x*0.5-512")
    public float TMActuToq;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte TMActuToqV;
    @F_bit_num(len = 11, valType = NumType.float32, valExpr = "x*0.5-512")
    public double ISGActuToq;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte ISGActuToqV;
    @F_bit_num(len = 1, valType = NumType.int8)
    public byte SAMActuToqV;
    @F_bit_num(len = 11, valType = NumType.float32, valExpr = "x*0.5-512")
    public double SAMActuToq;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short TMSttrTem;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short ISGSttrTem;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short SAMSttrTem;
    @F_bit_num(len = 10, valType = NumType.uint16)
    public short HVDCDCHVSideVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte HVDCDCHVSideVolV;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.1")
    public float AvgFuelCsump;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte TMInvtrVolV;

    @F_bit_num(len = 10, valType = NumType.uint16)
    public short TMInvtrVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte ISGInvtrVolV;
    @F_bit_num(len = 10, valType = NumType.uint16)
    public short ISGInvtrVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte SAMInvtrVolV;
    @F_bit_num(len = 10, valType = NumType.uint16)
    public short SAMInvtrVol;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSCellMaxTemIndx;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.5-40")
    public float BMSCellMaxTem;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSCellMaxTemV;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSCellMinTemIndx;

    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.5-40")
    public double BMSCellMinTem;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSCellMinTemV;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSCellMaxVolIndx;
    @F_bit_num(len = 13, valType = NumType.float32, valExpr = "x*0.001")
    public float BMSCellMaxVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSCellMaxVolV;
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSCellMinVolIndx;
    @F_bit_num(len = 13, valType = NumType.float32, valExpr = "x*0.001")
    public float BMSCellMinVol;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSCellMinVolV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPtIsltnRstcV;
    @F_bit_num(len = 8, valType = NumType.int16, valExpr = "x-40")
    public short HVDCDCTem;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BrkFludLvlLow;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BrkSysRedBrkTlltReq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte ABSF;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte VSESts;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte IbstrWrnngIO;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSHVILClsd;
    @F_bit_num(len = 12, valType = NumType.float32, valExpr = "x*0.5-848")
    public float EPTTrOtptShaftTotToq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte EPTTrOtptShaftTotToqV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BrkFludLvlLowV;
    @F_bit_num(len = 16, valType = NumType.float32, valExpr = "x*0.25")
    public float EnSpd;
    @F_bit_num(len = 2, valType = NumType.uint8)
    public byte EnSpdSts;
    @F_bit_num(len = 12, valExpr = "x*16", valType = NumType.uint16)
    public int FuelCsump;

}

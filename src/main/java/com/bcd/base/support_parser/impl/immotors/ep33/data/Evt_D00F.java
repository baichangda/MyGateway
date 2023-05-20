package com.bcd.base.support_parser.impl.immotors.ep33.data;

import com.bcd.base.support_parser.anno.F_float_integer;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00F extends Evt_4_x {
    @F_integer(bit = 8)
    public short BMSWrnngInfoCRC;
    @F_float_integer(bit = 8, valExpr = "x*0.5-40")
    public float BMSBusbarTempMax;
    @F_integer(bit = 1)
    public byte BMSPreThrmFltIndBkup;
    @F_integer(bit = 4)
    public byte BMSWrnngInfoRCBkup;
    @F_integer(bit = 3)
    public byte BMSBatPrsFlt;
    @F_integer(bit = 6)
    public byte BMSWrnngInfoBkup;
    @F_integer(bit = 1)
    public byte BMSBatPrsAlrm;
    @F_integer(bit = 1)
    public byte BMSBatPrsAlrmV;
    @F_integer(bit = 1)
    public byte BMSBatPrsSnsrV;
    @F_float_integer(bit = 15, valExpr = "x*0.05")
    public float BMSBatPrsSnsrValBkup;
    @F_integer(bit = 1)
    public byte BMSBatPrsSnsrVBkup;
    @F_float_integer(bit = 15, valExpr = "x*0.05")
    public float BMSBatPrsSnsrVal;
    @F_float_integer(bit = 8, valExpr = "x*0.4")
    public float BMSClntPumpPWMReq;
    @F_integer(bit = 1)
    public byte BMSPumpPwrOnReq;
    @F_integer(bit = 1)
    public byte BMSBatPrsAlrmVBkup;
    @F_integer(bit = 1)
    public byte BMSBatPrsAlrmBkup;
    @F_integer(bit = 4)
    public byte BMSWrnngInfoCRCBkup;
    @F_integer(bit = 1)
    public byte VCUBatPrsAlrm;
    @F_float_integer(bit = 8, valExpr = "x*0.5-40")
    public float OtsdAirTemCrVal;
    @F_integer(bit = 1)
    public byte VCUBatPrsAlrmV;
    @F_integer(bit = 1)
    public byte OtsdAirTemCrValV;
}

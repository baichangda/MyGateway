package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00F extends Evt_4_x {
    @F_bit_num(len = 8)
    public short BMSWrnngInfoCRC;
    @F_bit_num(len = 8, valExpr = "x*0.5-40")
    public double BMSBusbarTempMax;
    @F_bit_num(len = 1)
    public byte BMSPreThrmFltIndBkup;
    @F_bit_num(len = 4)
    public byte BMSWrnngInfoRCBkup;
    @F_bit_num(len = 3)
    public byte BMSBatPrsFlt;
    @F_bit_num(len = 6)
    public byte BMSWrnngInfoBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrm;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmV;
    @F_bit_num(len = 1)
    public byte BMSBatPrsSnsrV;
    @F_bit_num(len = 15, valExpr = "x*0.05")
    public double BMSBatPrsSnsrValBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsSnsrVBkup;
    @F_bit_num(len = 15, valExpr = "x*0.05")
    public double BMSBatPrsSnsrVal;
    @F_bit_num(len = 8, valExpr = "x*0.4")
    public double BMSClntPumpPWMReq;
    @F_bit_num(len = 1)
    public byte BMSPumpPwrOnReq;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmVBkup;
    @F_bit_num(len = 1)
    public byte BMSBatPrsAlrmBkup;
    @F_bit_num(len = 4)
    public byte BMSWrnngInfoCRCBkup;
    @F_bit_num(len = 1)
    public byte VCUBatPrsAlrm;
    @F_bit_num(len = 8, valExpr = "x*0.5-40")
    public double OtsdAirTemCrVal;
    @F_bit_num(len = 1)
    public byte VCUBatPrsAlrmV;
    @F_bit_num(len = 1)
    public byte OtsdAirTemCrValV;
}
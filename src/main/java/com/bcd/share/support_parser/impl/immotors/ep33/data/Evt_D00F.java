package com.bcd.share.support_parser.impl.immotors.ep33.data;


import com.bcd.share.support_parser.anno.F_bit_num;
import com.bcd.share.support_parser.anno.NumType;
import com.bcd.share.support_parser.impl.immotors.Evt_4_x;

public class Evt_D00F extends Evt_4_x {
    @F_bit_num(len = 8, valType = NumType.uint8)
    public short BMSWrnngInfoCRC;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.5-40")
    public float BMSBusbarTempMax;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPreThrmFltIndBkup;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte BMSWrnngInfoRCBkup;
    @F_bit_num(len = 3, valType = NumType.uint8)
    public byte BMSBatPrsFlt;
    @F_bit_num(len = 6, valType = NumType.uint8)
    public byte BMSWrnngInfoBkup;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsAlrm;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsAlrmV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsSnsrV;
    @F_bit_num(len = 15, valType = NumType.float32, valExpr = "x*0.05")
    public float BMSBatPrsSnsrValBkup;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsSnsrVBkup;
    @F_bit_num(len = 15, valType = NumType.float32, valExpr = "x*0.05")
    public float BMSBatPrsSnsrVal;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.4")
    public float BMSClntPumpPWMReq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSPumpPwrOnReq;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsAlrmVBkup;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte BMSBatPrsAlrmBkup;
    @F_bit_num(len = 4, valType = NumType.uint8)
    public byte BMSWrnngInfoCRCBkup;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte VCUBatPrsAlrm;
    @F_bit_num(len = 8, valType = NumType.float32, valExpr = "x*0.5-40")
    public float OtsdAirTemCrVal;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte VCUBatPrsAlrmV;
    @F_bit_num(len = 1, valType = NumType.uint8)
    public byte OtsdAirTemCrValV;
}

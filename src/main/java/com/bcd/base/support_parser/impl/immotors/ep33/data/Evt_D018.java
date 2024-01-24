package com.bcd.base.support_parser.impl.immotors.ep33.data;


import com.bcd.base.support_parser.anno.F_bit_num;
import com.bcd.base.support_parser.impl.immotors.Evt_4_x;

public class Evt_D018 extends Evt_4_x {
    @F_bit_num(len = 1)
    public byte APN1ConnSts;
    @F_bit_num(len = 1)
    public byte APN2ConnSts;
    @F_bit_num(len = 2)
    public byte ECallSts;
    @F_bit_num(len = 4)
    public byte MqttConnFailRsn;
    @F_bit_num(len = 4)
    public byte LocDRSts;
    @F_bit_num(len = 29,valExpr = "x*0.000001",unsigned = false)
    public double LongitudeDR;
    @F_bit_num(len = 28,valExpr = "x*0.000001",unsigned = false)
    public double LatitudeDR;
    @F_bit_num(len = 4)
    public byte LocGnns1Sts;
    @F_bit_num(len = 48)
    public long TBOXGPSTime;
    @F_bit_num(len = 4)
    public byte LocGnns2Sts;
    @F_bit_num(len = 14)
    public short LocRTKSts;
    @F_bit_num(len = 8)
    public short LocGnns1SatNum;
    @F_bit_num(len = 8)
    public short LocGnns2SatNum;
}

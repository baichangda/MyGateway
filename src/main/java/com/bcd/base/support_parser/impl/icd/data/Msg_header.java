package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.Date;

public class Msg_header {
    @F_num_array(len = 4, singleType = NumType.int8)
    public byte[] header;
    @F_num(type = NumType.uint16)
    public int header_len;
    @F_num(type = NumType.uint32)
    public long frame_len;
    @F_num(type = NumType.uint16)
    public FrameType frame_type;
    @F_num(type = NumType.uint32)
    public long version;
    @F_num(type = NumType.uint32)
    public long device_sn;
    @F_num(type = NumType.uint32)
    public long count;
    @F_date(mode = DateMode.float64_second)
    public Date timestamp;
    @F_num(type = NumType.uint16,valType = NumType.float32, valExpr = "x/10")
    public float fps;
    @F_num(type = NumType.uint32,valType = NumType.float64, valExpr = "x/10000000")
    public double dev_lon;
    @F_num(type = NumType.uint32,valType = NumType.float64, valExpr = "x/10000000")
    public double dev_lat;
    @F_num(type = NumType.uint32)
    public long dev_alt;
    @F_num(type = NumType.uint16,valType = NumType.float32, valExpr = "x/100")
    public float dev_azimuth;

    @F_skip(mode = SkipMode.reservedFromStart, len = 128)
    public byte[] reserved;
}

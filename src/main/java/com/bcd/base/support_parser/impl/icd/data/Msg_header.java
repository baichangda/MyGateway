package com.bcd.base.support_parser.impl.icd.data;

import com.bcd.base.support_parser.anno.*;

import java.util.Date;

public class Msg_header {
    @F_integer_array(len = 4, singleLen = 1)
    public byte[] header;
    @F_integer(len = 2)
    public int header_len;
    @F_integer(len = 4)
    public long frame_len;
    @F_integer(len = 2)
    public FrameType frame_type;
    @F_integer(len = 4)
    public long version;
    @F_integer(len = 4)
    public long device_sn;
    @F_integer(len = 4)
    public long count;
    @F_date(mode = DateMode.Float64_second)
    public Date timestamp;
    @F_float_integer(len = 2, valExpr = "x/10")
    public float fps;
    @F_float_integer(len = 4, valExpr = "x/10000000")
    public double dev_lon;
    @F_float_integer(len = 4, valExpr = "x/10000000")
    public double dev_lat;
    @F_integer(len = 4)
    public long dev_alt;
    @F_float_integer(len = 2, valExpr = "x/100")
    public float dev_azimuth;

    @F_skip(mode = SkipMode.ReservedFromStart, len = 128)
    public byte[] reserved;
}

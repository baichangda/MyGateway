package com.bcd.share.support_parser.impl.jtt808.data;

import com.bcd.share.support_parser.anno.*;

import java.util.Date;

public class DriverIdentityReport implements PacketBody {
    //状态
    @F_num(type = NumType.uint8)
    public byte status;
    //时间
    @F_date_bytes_6
    public Date time;
    //ic卡读取结果
    @F_num(type = NumType.uint8)
    public byte res;
    //驾驶员姓名长度
    @F_num(type = NumType.uint8, var = 'n')
    public short nameLen;
    //驾驶员姓名
    @F_string(lenExpr = "n")
    public String name;
    //从业资格证编码
    @F_string(len = 20, appendMode = StringAppendMode.highAddressAppend)
    public String code;
    //发证机构名称长度
    @F_num(type = NumType.uint8, var = 'm')
    public short orgLen;
    //发证机构
    @F_string(lenExpr = "m")
    public String org;
    //证件有效期
    @F_num_array(len = 4, singleType = NumType.uint8)
    public byte[] expired;
    //驾驶员身份证号
    @F_string(len = 20, appendMode = StringAppendMode.highAddressAppend)
    public String id;
}

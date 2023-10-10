package com.bcd.share.support_parser.impl.icd.data;


import com.bcd.share.support_parser.anno.F_bean_list;
import com.bcd.share.support_parser.anno.F_num;
import com.bcd.share.support_parser.anno.F_num_array;
import com.bcd.share.support_parser.anno.NumType;

import java.util.List;

public class Msg_body_system_runtime_info implements Msg_body {
    @F_num(type = NumType.uint8, var = 'n')
    public short cpu_num;
    @F_num_array(lenExpr = "n", singleType = NumType.uint8)
    public short[] cpu_usage;
    @F_num(type = NumType.uint32)
    public long mem_size;
    @F_num(type = NumType.uint8)
    public short mem_usage;
    @F_num(type = NumType.uint8, var = 'm')
    public short gpu_num;
    @F_num_array(lenExpr = "m", singleType = NumType.uint8)
    public short[] gpu_usage;
    @F_num(type = NumType.uint8, var = 'a')
    public short disk_num;
    @F_bean_list(listLenExpr = "a")
    public List<Disk_infos> disk_infos;
    @F_num(type = NumType.uint8, var = 'b')
    public short net_num;
    @F_bean_list(listLenExpr = "b")
    public List<Net_infos> net_infos;
    @F_num(type = NumType.uint8, var = 'c')
    public short temp_num;
    //1、cpu温度;2、gpu温度;3、设备内温度
    @F_num_array(lenExpr = "c", singleType = NumType.uint8)
    public short[] temp_val;
    @F_num(type = NumType.uint8, var = 'd')
    public short fans_num;
    @F_num_array(lenExpr = "d", singleType = NumType.uint8)
    public short[] fans_speed;
}

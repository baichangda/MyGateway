package com.bcd.base.support_parser.impl.icd.data;


import com.bcd.base.support_parser.anno.F_bean_list;
import com.bcd.base.support_parser.anno.F_integer;
import com.bcd.base.support_parser.anno.F_integer_array;

import java.util.List;

public class Msg_body_system_runtime_info implements Msg_body{
    @F_integer(len = 1, var = 'n')
    public short cpu_num;
    @F_integer_array(lenExpr = "n", singleLen = 1)
    public short[] cpu_usage;
    @F_integer(len = 4)
    public long mem_size;
    @F_integer(len = 1)
    public short mem_usage;
    @F_integer(len = 1, var = 'm')
    public short gpu_num;
    @F_integer_array(lenExpr = "m", singleLen = 1)
    public short[] gpu_usage;
    @F_integer(len = 1, var = 'a')
    public short disk_num;
    @F_bean_list(listLenExpr = "a")
    public List<Disk_infos> disk_infos;
    @F_integer(len = 1, var = 'b')
    public short net_num;
    @F_bean_list(listLenExpr = "b")
    public List<Net_infos> net_infos;
    @F_integer(len = 1, var = 'c')
    public short temp_num;
    //1、cpu温度;2、gpu温度;3、设备内温度
    @F_integer_array(lenExpr = "c", singleLen = 1)
    public short[] temp_val;
    @F_integer(len = 1, var = 'd')
    public short fans_num;
    @F_integer_array(lenExpr = "d", singleLen = 1)
    public short[] fans_speed;
}

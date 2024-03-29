package com.bcd.base.support_parser.impl.jtt808.data;

import com.bcd.base.support_parser.anno.F_num;
import com.bcd.base.support_parser.anno.NumType;

public class RecordingStartCmd implements PacketBody{
    //录音命令
    @F_num(type = NumType.uint8)
    public byte cmd;
    //录音时间
    @F_num(type = NumType.uint16)
    public int time;
    //保存标志
    @F_num(type = NumType.uint8)
    public byte flag;
    //音频采样率
    @F_num(type = NumType.uint8)
    public byte samplingRate;
}

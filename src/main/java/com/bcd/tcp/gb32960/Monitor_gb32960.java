package com.bcd.tcp.gb32960;

import com.bcd.share.util.StringUtil;
import com.bcd.tcp.Monitor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.atomic.LongAdder;

@Component
public class Monitor_gb32960 implements Monitor {
    public final static LongAdder receiveNum = new LongAdder();
    public final static LongAdder queueNum = new LongAdder();
    public final static LongAdder saveNum = new LongAdder();

    @Override
    public String log(Duration period) {
        return StringUtil.format("gb32960 clientNum[{}] receiveSpeed[{}/s] queueNum[{}] saveSpeed[{}/s]",
                Session_gb32960.getSessionMap().size(),
                receiveNum.sumThenReset() / period.toSeconds(),
                queueNum.sum(),
                saveNum.sumThenReset() / period.toSeconds());
    }
}

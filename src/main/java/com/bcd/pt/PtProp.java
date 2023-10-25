package com.bcd.pt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pt")
public class PtProp {
    public int period;
    public int startIndex;
    public int num;
    public String server;
}

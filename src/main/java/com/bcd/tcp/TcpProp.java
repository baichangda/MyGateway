package com.bcd.tcp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tcp")
public class TcpProp {
    public int port;
    public Duration maxBeforeOffline;
    public String sessionTopic;
}

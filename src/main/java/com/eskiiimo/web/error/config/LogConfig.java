package com.eskiiimo.web.error.config;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "log")
public class LogConfig {

    private Level errorLevel;

    private Slack slack;

    private Database database;

    @Getter
    @Setter
    public static class Slack {
        private boolean enabled;
        private String webHookUrl;
        private String channel;
    }

    @Getter
    @Setter
    public static class Database {
        private boolean enabled;
    }

}

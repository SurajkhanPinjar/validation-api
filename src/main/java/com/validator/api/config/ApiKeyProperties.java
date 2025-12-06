package com.validator.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "security.api")
public class ApiKeyProperties {

    private List<String> keys;

    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class RateLimit {
        private int perSecond;
        private int perMinute;
    }
}
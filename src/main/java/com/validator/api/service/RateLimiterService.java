package com.validator.api.service;

import com.validator.api.config.ApiKeyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final ApiKeyProperties properties;

    private final Map<String, Window> counter = new ConcurrentHashMap<>();

    public boolean allowRequest(String apiKey) {

        int perSecond = properties.getRateLimit().getPerSecond();
        int perMinute = properties.getRateLimit().getPerMinute();

        long now = System.currentTimeMillis();

        Window window = counter.computeIfAbsent(apiKey, k -> new Window());

        synchronized (window) {
            // Reset per-second window
            if (now - window.secondWindowStart >= 1000) {
                window.secondWindowStart = now;
                window.secondCount = 0;
            }

            // Reset per-minute window
            if (now - window.minuteWindowStart >= 60_000) {
                window.minuteWindowStart = now;
                window.minuteCount = 0;
            }

            if (window.secondCount >= perSecond || window.minuteCount >= perMinute) {
                return false;
            }

            window.secondCount++;
            window.minuteCount++;

            return true;
        }
    }

    private static class Window {
        long secondWindowStart = System.currentTimeMillis();
        long minuteWindowStart = System.currentTimeMillis();
        int secondCount = 0;
        int minuteCount = 0;
    }
}
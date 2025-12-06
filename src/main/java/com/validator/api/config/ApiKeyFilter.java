package com.validator.api.config;

import com.validator.api.exception.ApiKeyInvalidException;
import com.validator.api.exception.ApiKeyMissingException;
import com.validator.api.service.RateLimiterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter implements Filter {

    private final ApiKeyProperties apiKeyProperties;
    private final RateLimiterService rateLimiterService;

    private static final String HEADER_NAME = "X-API-KEY";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Allow Swagger, API docs, resources, errors
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars")
                || path.equals("/error")) {

            chain.doFilter(request, response);
            return;
        }

        // Extract API key
        String apiKey = req.getHeader(HEADER_NAME);

        // Validate API key presence
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiKeyMissingException("Missing API Key. Please provide X-API-KEY header.");
        }

        // Validate key exists in allowed list
        if (!apiKeyProperties.getKeys().contains(apiKey)) {
            throw new ApiKeyInvalidException("Invalid API Key.");
        }

        // 🔥 Apply rate limiting
        rateLimiterService.allowRequest(apiKey);

        // Continue request
        chain.doFilter(request, response);
    }
}
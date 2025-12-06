//package com.validator.api.security;
//
//import com.validator.api.exception.ApiKeyMissingException;
//import com.validator.api.exception.ApiKeyInvalidException;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//
//@Component
//public class ApiKeyFilter implements Filter {
//
//    private static final String HEADER_NAME = "X-API-KEY";
//
//    // For now static keys (later we expand to DB-based keys)
//    private static final String VALID_KEY = "SECRET_DEMO_KEY_123";
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//
//        // Allow Swagger UI to work without keys
//        String path = req.getRequestURI();
//        if (path.startsWith("/swagger") ||
//                path.startsWith("/v3/api-docs") ||
//                path.startsWith("/error")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String apiKey = req.getHeader(HEADER_NAME);
//
//        if (apiKey == null || apiKey.isBlank()) {
//            throw new ApiKeyMissingException("Missing API Key. Please provide X-API-KEY header.");
//        }
//
//        if (!apiKey.equals(VALID_KEY)) {
//            throw new ApiKeyInvalidException("Invalid API Key.");
//        }
//
//        chain.doFilter(request, response);
//    }
//}
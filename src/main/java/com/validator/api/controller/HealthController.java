package com.validator.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private Map<String, Object> success(Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "UP");
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("data", data);
        return body;
    }

    // 1️⃣ BASIC HEALTH CHECK
    @Operation(
            summary = "Basic Health Check",
            description = "Returns API status and timestamp.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "API is running",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Health Response",
                                    value = """
                                    {
                                      "status": "UP",
                                      "timestamp": "2025-12-06T14:30:45",
                                      "data": "API is healthy"
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(success("API is healthy"));
    }

    // 2️⃣ PING ENDPOINT
    @Operation(
            summary = "Ping Endpoint",
            description = "Used for lightweight uptime checks.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Ping success",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ping Response",
                                    value = """
                                    {
                                      "status": "UP",
                                      "timestamp": "2025-12-06T14:30:45",
                                      "data": "pong"
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        return ResponseEntity.ok(success("pong"));
    }

    // 3️⃣ DETAILED SERVICE INFO
    @Operation(
            summary = "Detailed Health Info",
            description = "Provides service version, name, uptime information.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Detailed system info",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Info Response",
                                    value = """
                                    {
                                      "status": "UP",
                                      "timestamp": "2025-12-06T14:30:45",
                                      "data": {
                                        "service": "Advanced Validation API",
                                        "version": "1.0.0",
                                        "author": "Suraj",
                                        "description": "Unified Email, Phone, IP, ZIP & Bulk Validation API"
                                      }
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {

        Map<String, Object> details = new HashMap<>();
        details.put("service", "Advanced Validation API");
        details.put("version", "1.0.0");
        details.put("author", "Suraj");
        details.put("description", "Unified Email, Phone, IP, ZIP & Bulk Validation API");

        return ResponseEntity.ok(success(details));
    }
}
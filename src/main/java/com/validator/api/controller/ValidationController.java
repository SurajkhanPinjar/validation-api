package com.validator.api.controller;

import com.validator.api.dto.request.BulkValidationRequest;
import com.validator.api.dto.request.ValidationRequest;
import com.validator.api.dto.response.FinalValidationResponse;
import com.validator.api.service.ValidationOrchestratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/validate")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationOrchestratorService orchestratorService;

    private Map<String, Object> buildSuccessResponse(Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("data", data);
        return body;
    }

    // ---------------- POST: Full validation ---------------- //

    @Operation(
            summary = "Validate Email, Phone, IP, and ZIP in one request",
            description = "Send any combination of email, phone, IP address, and zipcode. You'll get a unified validation response.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Validation successful",
                            content = @Content(schema = @Schema(implementation = FinalValidationResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> validateAll(@RequestBody ValidationRequest request) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.validateAll(request)));
    }

    // ---------------- GET: Quick email validation ---------------- //

    @Operation(
            summary = "Quick Email Validation",
            description = "Validate only an email address using MX, syntax, disposable check."
    )
    @GetMapping("/email")
    public ResponseEntity<Map<String, Object>> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeEmail(email)));
    }

    // ---------------- GET: Quick phone validation ---------------- //

    @Operation(
            summary = "Quick Phone Validation",
            description = "Validate phone number including country, line type, and formatting."
    )
    @GetMapping("/phone")
    public ResponseEntity<Map<String, Object>> validatePhone(
            @RequestParam String phone,
            @RequestParam(required = false) String country) {

        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safePhone(phone, country)));
    }

    // ---------------- GET: Quick IP validation ---------------- //

    @Operation(
            summary = "Quick IP Validation",
            description = "Validate IPv4/IPv6 and detect private/reserved ranges."
    )
    @GetMapping("/ip")
    public ResponseEntity<Map<String, Object>> validateIp(@RequestParam String ip) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeIp(ip)));
    }

    // ---------------- GET: Quick ZIP validation ---------------- //

    @Operation(
            summary = "Quick ZIP Code Validation",
            description = "Validate US ZIP code and detect corresponding state."
    )
    @GetMapping("/zipcode")
    public ResponseEntity<Map<String, Object>> validateZip(@RequestParam String zipcode) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeZip(zipcode)));
    }



    // ---------------- Bulk End Point  ---------------- //

    @PostMapping("/bulk")
    @Operation(
            summary = "Bulk validation for emails, phones, IPs, and zipcodes",
            description = "Send arrays of values and receive bulk validation results."
    )
    public ResponseEntity<Map<String, Object>> bulkValidate(
            @RequestBody BulkValidationRequest request) {

        return ResponseEntity.ok(
                buildSuccessResponse(orchestratorService.validateBulk(request))
        );
    }
}
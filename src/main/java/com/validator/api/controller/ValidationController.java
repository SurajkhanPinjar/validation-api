package com.validator.api.controller;

import com.validator.api.dto.request.BulkValidationRequest;
import com.validator.api.dto.request.ValidationRequest;
import com.validator.api.dto.response.FinalValidationResponse;
import com.validator.api.service.ValidationOrchestratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Sample Request",
                                    value = """
                                    {
                                      "email": "test@example.com",
                                      "phone": "+14155552671",
                                      "country": "US",
                                      "ip": "8.8.8.8",
                                      "zipcode": "94016"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Validation successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "email": { "syntaxValid": true, "hasMxRecord": true },
                                                "phone": { "valid": true, "country": "US" },
                                                "ip": { "valid": true, "type": "public" },
                                                "zipcode": { "valid": true, "state": "California" }
                                              }
                                            }
                                            """
                                    )
                            )
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
            description = "Validate only an email address using MX, syntax, disposable check.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Email validation result",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Email Response",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "syntaxValid": true,
                                        "hasMxRecord": true,
                                        "disposable": false
                                      }
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping("/email")
    public ResponseEntity<Map<String, Object>> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeEmail(email)));
    }

    // ---------------- GET: Quick phone validation ---------------- //

    @Operation(
            summary = "Quick Phone Validation",
            description = "Validate phone number including country, line type, and formatting.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Phone validation result",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Phone Response",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "valid": true,
                                        "country": "US",
                                        "type": "mobile"
                                      }
                                    }
                                    """
                            )
                    )
            )
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
            description = "Validate IPv4/IPv6 and detect private/reserved ranges.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "IP validation result",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "IP Response",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "valid": true,
                                        "type": "public",
                                        "version": "IPv4"
                                      }
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping("/ip")
    public ResponseEntity<Map<String, Object>> validateIp(@RequestParam String ip) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeIp(ip)));
    }

    // ---------------- GET: Quick ZIP validation ---------------- //

    @Operation(
            summary = "Quick ZIP Code Validation",
            description = "Validate US ZIP code and detect corresponding state.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "ZIP code validation result",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ZIP Response",
                                    value = """
                                    {
                                      "success": true,
                                      "data": {
                                        "valid": true,
                                        "state": "California"
                                      }
                                    }
                                    """
                            )
                    )
            )
    )
    @GetMapping("/zipcode")
    public ResponseEntity<Map<String, Object>> validateZip(@RequestParam String zipcode) {
        return ResponseEntity.ok(buildSuccessResponse(orchestratorService.safeZip(zipcode)));
    }



    // ---------------- Bulk End Point  ---------------- //

    @PostMapping("/bulk")
    @Operation(
            summary = "Bulk validation for emails, phones, IPs, and zipcodes",
            description = "Send arrays of values and receive bulk validation results.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Bulk Request",
                                    value = """
                                            {
                                              "emails": [
                                                "valid.user@gmail.com",
                                                "temp@mailinator.com",
                                                "wrong-email-format"
                                              ],
                                              "phones": [
                                                { "number": "+14155552671", "country": "US" },
                                                { "number": "9876543210", "country": "IN" },
                                                { "number": "12345", "country": "US" }
                                              ],
                                              "ips": [
                                                "8.8.8.8",
                                                "192.168.0.1",
                                                "999.999.999.999"
                                              ],
                                              "zipcodes": [
                                                "94016",
                                                "10001",
                                                "ABCDE"
                                              ]
                                            }
                                    """
                            )
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Bulk validation result",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Bulk Response",
                                    value = """
                                            {
                                              "success": true,
                                              "data": {
                                                "emails": [
                                                  {
                                                    "index": 0,
                                                    "input": "valid.user@gmail.com",
                                                    "result": {
                                                      "syntaxValid": true,
                                                      "hasMxRecord": true,
                                                      "smtpStatus": "unknown",
                                                      "disposable": false,
                                                      "roleBased": false,
                                                      "score": 100,
                                                      "domain": "gmail.com"
                                                    }
                                                  },
                                                  {
                                                    "index": 1,
                                                    "input": "temp@mailinator.com",
                                                    "result": {
                                                      "syntaxValid": true,
                                                      "hasMxRecord": true,
                                                      "smtpStatus": "unknown",
                                                      "disposable": true,
                                                      "roleBased": false,
                                                      "score": 40,
                                                      "domain": "mailinator.com"
                                                    }
                                                  },
                                                  {
                                                    "index": 2,
                                                    "input": "wrong-email-format",
                                                    "result": {
                                                      "syntaxValid": false,
                                                      "hasMxRecord": false,
                                                      "smtpStatus": "invalid",
                                                      "disposable": false,
                                                      "roleBased": false,
                                                      "score": 10,
                                                      "domain": null
                                                    }
                                                  }
                                                ],
                                                "phones": [
                                                  {
                                                    "index": 0,
                                                    "input": "+14155552671",
                                                    "country": "US",
                                                    "result": {
                                                      "valid": true,
                                                      "country": "US",
                                                      "internationalFormat": "+1 415-555-2671",
                                                      "nationalFormat": "(415) 555-2671",
                                                      "lineType": "mobile",
                                                      "carrier": "Unknown"
                                                    }
                                                  },
                                                  {
                                                    "index": 1,
                                                    "input": "9876543210",
                                                    "country": "IN",
                                                    "result": {
                                                      "valid": true,
                                                      "country": "IN",
                                                      "internationalFormat": "+91 98765 43210",
                                                      "nationalFormat": "98765 43210",
                                                      "lineType": "mobile",
                                                      "carrier": "Unknown"
                                                    }
                                                  },
                                                  {
                                                    "index": 2,
                                                    "input": "12345",
                                                    "country": "US",
                                                    "result": {
                                                      "valid": false,
                                                      "country": "US",
                                                      "internationalFormat": null,
                                                      "nationalFormat": null,
                                                      "lineType": "invalid",
                                                      "carrier": null
                                                    }
                                                  }
                                                ],
                                                "ips": [
                                                  {
                                                    "index": 0,
                                                    "input": "8.8.8.8",
                                                    "result": {
                                                      "valid": true,
                                                      "version": "IPv4",
                                                      "isPrivate": false,
                                                      "isReserved": false
                                                    }
                                                  },
                                                  {
                                                    "index": 1,
                                                    "input": "192.168.0.1",
                                                    "result": {
                                                      "valid": true,
                                                      "version": "IPv4",
                                                      "isPrivate": true,
                                                      "isReserved": false
                                                    }
                                                  },
                                                  {
                                                    "index": 2,
                                                    "input": "999.999.999.999",
                                                    "result": {
                                                      "valid": false,
                                                      "version": "unknown",
                                                      "isPrivate": false,
                                                      "isReserved": false
                                                    }
                                                  }
                                                ],
                                                "zipcodes": [
                                                  {
                                                    "index": 0,
                                                    "input": "94016",
                                                    "result": {
                                                      "valid": true,
                                                      "state": "California",
                                                      "city": null,
                                                      "country": "US"
                                                    }
                                                  },
                                                  {
                                                    "index": 1,
                                                    "input": "10001",
                                                    "result": {
                                                      "valid": true,
                                                      "state": "New York",
                                                      "city": null,
                                                      "country": "US"
                                                    }
                                                  },
                                                  {
                                                    "index": 2,
                                                    "input": "ABCDE",
                                                    "result": {
                                                      "valid": false,
                                                      "state": null,
                                                      "city": null,
                                                      "country": "US"
                                                    }
                                                  }
                                                ]
                                              }
                                            }
                                    """
                            )
                    )
            )
    )
    public ResponseEntity<Map<String, Object>> bulkValidate(
            @RequestBody BulkValidationRequest request) {

        return ResponseEntity.ok(
                buildSuccessResponse(orchestratorService.validateBulk(request))
        );
    }
}
package com.validator.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneValidationResponse {

    private boolean valid;
    private String country;
    private String internationalFormat;
    private String nationalFormat;

    private String lineType;     // mobile, landline, voip, unknown
    private String carrier;      // Airtel, Jio, Verizon, Twilio etc.

    private boolean voip;        // true = VOIP number
    private int riskScore;       // 0–100
    private String riskLevel;    // LOW / MEDIUM / HIGH / CRITICAL
}
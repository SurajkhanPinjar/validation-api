package com.validator.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailValidationResponse {

    private boolean syntaxValid;
    private boolean hasMxRecord;
    private String smtpStatus;      // valid / invalid / unknown
    private boolean disposable;
    private boolean roleBased;
    private Integer score;          // 0 - 100 deliverability score
    private String domain;

    private Integer reputationScore;     // 0-100, nullable if unknown
    private String reputationCategory;   // excellent|good|neutral|suspicious|bad

    private Integer riskScore;           // 0-100 (0 = low risk, 100 = critical)
    private String riskLevel;            // LOW | MEDIUM | HIGH | CRITICAL
}
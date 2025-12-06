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
}
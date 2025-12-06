package com.validator.api.validator;

import lombok.Data;

@Data
public class SmtpResult {
    private String email;
    private String status;  // valid | invalid | unknown | mx_not_found
}
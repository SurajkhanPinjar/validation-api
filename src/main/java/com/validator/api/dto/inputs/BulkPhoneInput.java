package com.validator.api.dto.inputs;

import lombok.Data;

@Data
public class BulkPhoneInput {
    private String number;
    private String country; // optional
}
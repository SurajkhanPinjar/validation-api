package com.validator.api.dto.result;

import com.validator.api.dto.response.EmailValidationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkEmailResult {
    private int index;
    private String input;
    private EmailValidationResponse result;
}
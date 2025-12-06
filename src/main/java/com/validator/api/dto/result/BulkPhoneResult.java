package com.validator.api.dto.result;

import com.validator.api.dto.response.PhoneValidationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkPhoneResult {
    private int index;
    private String input;
    private String country;
    private PhoneValidationResponse result;
}
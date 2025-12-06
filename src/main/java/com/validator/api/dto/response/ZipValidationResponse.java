package com.validator.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZipValidationResponse {

    private boolean valid;
    private String state;
    private String city;
    private String country; // Always "US" for now
}
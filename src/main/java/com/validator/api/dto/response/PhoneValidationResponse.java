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
    private String lineType;     // mobile / fixed / voip / unknown
    private String carrier;
}
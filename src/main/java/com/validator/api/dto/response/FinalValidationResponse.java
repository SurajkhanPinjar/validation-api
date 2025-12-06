package com.validator.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinalValidationResponse {

    private EmailValidationResponse email;
    private PhoneValidationResponse phone;
    private IpValidationResponse ip;
    private ZipValidationResponse zipcode;

    private String risk; // LOW / MEDIUM / HIGH
}
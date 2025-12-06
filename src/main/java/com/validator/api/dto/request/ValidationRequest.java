package com.validator.api.dto.request;

import lombok.Data;

@Data
public class ValidationRequest {

    private String email;
    private String phone;
    private String country;   // Used for phone validation
    private String ip;
    private String zipcode;
}
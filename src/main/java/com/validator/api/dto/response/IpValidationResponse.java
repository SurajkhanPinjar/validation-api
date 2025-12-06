package com.validator.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IpValidationResponse {

    private boolean valid;
    private String version;         // IPv4 / IPv6
    private boolean isPrivate;
    private boolean isReserved;
}
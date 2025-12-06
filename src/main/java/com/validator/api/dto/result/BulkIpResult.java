package com.validator.api.dto.result;

import com.validator.api.dto.response.IpValidationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkIpResult {
    private int index;
    private String input;
    private IpValidationResponse result;
}
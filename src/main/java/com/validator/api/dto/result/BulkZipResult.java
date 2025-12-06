package com.validator.api.dto.result;

import com.validator.api.dto.response.ZipValidationResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkZipResult {
    private int index;
    private String input;
    private ZipValidationResponse result;
}
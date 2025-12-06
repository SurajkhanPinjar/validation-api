package com.validator.api.dto.response;

import com.validator.api.dto.result.BulkEmailResult;
import com.validator.api.dto.result.BulkIpResult;
import com.validator.api.dto.result.BulkPhoneResult;
import com.validator.api.dto.result.BulkZipResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BulkValidationResponse {
    private List<BulkEmailResult> emails;
    private List<BulkPhoneResult> phones;
    private List<BulkIpResult> ips;
    private List<BulkZipResult> zipcodes;
}
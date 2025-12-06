package com.validator.api.dto.request;

import com.validator.api.dto.inputs.BulkPhoneInput;
import lombok.Data;
import java.util.List;

@Data
public class BulkValidationRequest {

    private List<String> emails;

    private List<BulkPhoneInput> phones;

    private List<String> ips;

    private List<String> zipcodes;

}
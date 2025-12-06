package com.validator.api.service;

import com.validator.api.dto.response.ZipValidationResponse;
import com.validator.api.exception.GlobalExceptionHandler.InvalidZipException;
import org.springframework.stereotype.Service;

@Service
public class ZipValidationService {

    public ZipValidationResponse validate(String zip) {
        if (zip == null || !zip.matches("\\d{5}")) {
            return ZipValidationResponse.builder()
                    .valid(false)
                    .state(null)
                    .city(null)
                    .country("US")
                    .build();
        }

        int zipCode = Integer.parseInt(zip);

        // 2 Efficient state lookup using indexed ranges (FAST)
        String state = resolveState(zipCode);

        if (state == null) {
            return ZipValidationResponse.builder()
                    .valid(false)
                    .state(null)
                    .city(null)
                    .country("US")
                    .build();
        }

        return ZipValidationResponse.builder()
                .valid(true)
                .state(state)
                .city(null)   // We skip city to keep API fast; can add JSON based DB later
                .country("US")
                .build();
    }

    /**
     * Ultra-fast range-based state resolution.
     * No Maps, no loops — pure O(1) checks.
     */
    private String resolveState(int zip) {

        if (zip >= 99500 && zip <= 99950) return "Alaska";
        if (zip >= 35000 && zip <= 36999) return "Alabama";
        if (zip >= 71600 && zip <= 72999) return "Arkansas";
        if (zip >= 85000 && zip <= 86999) return "Arizona";

        if (zip >= 90000 && zip <= 96699) return "California";
        if (zip >= 80000 && zip <= 81699) return "Colorado";
        if (zip >= 6000  && zip <= 6999)  return "Connecticut";

        if (zip >= 19700 && zip <= 19999) return "Delaware";
        if (zip >= 20000 && zip <= 20099) return "Washington D.C.";

        if (zip >= 32000 && zip <= 34999) return "Florida";
        if (zip >= 30000 && zip <= 31999) return "Georgia";

        if (zip >= 96700 && zip <= 96999) return "Hawaii";
        if (zip >= 83200 && zip <= 83999) return "Idaho";
        if (zip >= 60000 && zip <= 62999) return "Illinois";
        if (zip >= 46000 && zip <= 47999) return "Indiana";
        if (zip >= 50000 && zip <= 52999) return "Iowa";

        if (zip >= 66000 && zip <= 67999) return "Kansas";
        if (zip >= 40000 && zip <= 42999) return "Kentucky";
        if (zip >= 70000 && zip <= 71599) return "Louisiana";

        if (zip >= 3900  && zip <= 4999)  return "Maine";
        if (zip >= 20600 && zip <= 21999) return "Maryland";
        if (zip >= 1000  && zip <= 2799)  return "Massachusetts";

        if (zip >= 48000 && zip <= 49999) return "Michigan";
        if (zip >= 55000 && zip <= 56799) return "Minnesota";
        if (zip >= 38600 && zip <= 39999) return "Mississippi";
        if (zip >= 63000 && zip <= 65999) return "Missouri";

        if (zip >= 59000 && zip <= 59999) return "Montana";
        if (zip >= 68000 && zip <= 69999) return "Nebraska";
        if (zip >= 88900 && zip <= 89999) return "Nevada";

        if (zip >= 3000  && zip <= 3899)  return "New Hampshire";
        if (zip >= 7000  && zip <= 8999)  return "New Jersey";

        if (zip >= 87000 && zip <= 88499) return "New Mexico";
        if (zip >= 10000 && zip <= 14999) return "New York";

        if (zip >= 27000 && zip <= 28999) return "North Carolina";
        if (zip >= 58000 && zip <= 58999) return "North Dakota";

        if (zip >= 43000 && zip <= 45999) return "Ohio";
        if (zip >= 73000 && zip <= 74999) return "Oklahoma";
        if (zip >= 97000 && zip <= 97999) return "Oregon";

        if (zip >= 15000 && zip <= 19699) return "Pennsylvania";

        if (zip >= 2800  && zip <= 2999)  return "Rhode Island";
        if (zip >= 29000 && zip <= 29999) return "South Carolina";
        if (zip >= 57000 && zip <= 57999) return "South Dakota";

        if (zip >= 37000 && zip <= 38599) return "Tennessee";
        if (zip >= 75000 && zip <= 79999) return "Texas";
        if (zip >= 88500 && zip <= 88599) return "Texas";

        if (zip >= 84000 && zip <= 84999) return "Utah";

        if (zip >= 5000  && zip <= 5999)  return "Vermont";
        if (zip >= 22000 && zip <= 24699) return "Virginia";

        if (zip >= 98000 && zip <= 99499) return "Washington";

        if (zip >= 24700 && zip <= 26999) return "West Virginia";
        if (zip >= 53000 && zip <= 54999) return "Wisconsin";
        if (zip >= 82000 && zip <= 83199) return "Wyoming";

        return null;
    }
}
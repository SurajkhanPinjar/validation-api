package com.validator.api.service;

import com.google.i18n.phonenumbers.*;
import com.validator.api.dto.response.PhoneValidationResponse;
import com.validator.api.exception.GlobalExceptionHandler.InvalidPhoneException;
import org.springframework.stereotype.Service;

@Service
public class PhoneValidationService {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    public PhoneValidationResponse validate(String phone, String country) {

        // 1 Null/blank request handling
        if (phone == null || phone.isBlank()) {
            return PhoneValidationResponse.builder()
                    .valid(false)
                    .lineType("invalid")
                    .build();
        }

        try {
            // 2 If no country provided → attempt auto-detection
            if (country == null || country.isBlank()) {
                country = detectCountry(phone);
            }

            // 3 Parse using Google libphonenumber
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, country);

            boolean isValid = phoneUtil.isValidNumber(number);

            if (!isValid) {
                return PhoneValidationResponse.builder()
                        .valid(false)
                        .country(country)
                        .lineType("invalid")
                        .carrier(null)
                        .internationalFormat(null)
                        .nationalFormat(null)
                        .build();
            }

            // 4 Determine line type (MOBILE, FIXED_LINE, VOIP…)
            PhoneNumberUtil.PhoneNumberType type = phoneUtil.getNumberType(number);
            String lineType = type.name().toLowerCase();

            // 5 Generate formats
            String internationalFormat = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            String nationalFormat = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

            // 6 Carrier lookup placeholder
            String carrier = "Unknown";

            return PhoneValidationResponse.builder()
                    .valid(true)
                    .country(country)
                    .carrier(carrier)
                    .internationalFormat(internationalFormat)
                    .nationalFormat(nationalFormat)
                    .lineType(lineType)
                    .build();

        } catch (Exception ex) {
            return PhoneValidationResponse.builder()
                    .valid(false)
                    .country(country)
                    .lineType("invalid")
                    .carrier(null)
                    .internationalFormat(null)
                    .nationalFormat(null)
                    .build();
        }
    }

    // Auto-detect country from phone number
    private String detectCountry(String phone) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, null);
            int countryCode = number.getCountryCode();
            return phoneUtil.getRegionCodeForCountryCode(countryCode);
        } catch (Exception ignored) { }
        return "US"; // Default fallback
    }
}
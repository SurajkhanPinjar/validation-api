package com.validator.api.service;

import com.google.i18n.phonenumbers.*;
import com.validator.api.dto.response.PhoneValidationResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PhoneValidationService {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    // ----------- VOIP Provider Patterns -----------
    private static final String[] VOIP_KEYWORDS = {
            "twilio", "plivo", "textnow", "google", "voice", "bandwidth"
    };

    // ----------- Country Risk Scores -----------
    private static final Map<String, Integer> COUNTRY_RISK = Map.of(
            "US", 10,
            "IN", 15,
            "NG", 50,   // Nigeria → high fraud risk
            "PK", 40,
            "CN", 30,
            "RU", 35
    );

    public PhoneValidationResponse validate(String phone, String country) {

        // 1 Null or blank → invalid
        if (phone == null || phone.isBlank()) {
            return PhoneValidationResponse.builder()
                    .valid(false)
                    .lineType("invalid")
                    .riskScore(100)
                    .riskLevel("CRITICAL")
                    .build();
        }

        try {
            // 2 Auto-detect country if missing
            if (country == null || country.isBlank()) {
                country = detectCountry(phone);
            }

            // 3 Parse number
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, country);
            boolean isValid = phoneUtil.isValidNumber(number);

            if (!isValid) {
                return PhoneValidationResponse.builder()
                        .valid(false)
                        .country(country)
                        .lineType("invalid")
                        .riskScore(80)
                        .riskLevel("HIGH")
                        .build();
            }

            // 4 Line type
            PhoneNumberUtil.PhoneNumberType type = phoneUtil.getNumberType(number);
            String lineType = type.name().toLowerCase();

            // 5 Formats
            String internationalFormat = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            String nationalFormat = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

            // 6 Carrier Lookup (Basic logic)
            String carrier = detectCarrier(nationalFormat, internationalFormat);

            // 7 VOIP detection
            boolean isVoip = detectVoip(carrier);

            // 8 Risk scoring engine
            int riskScore = calculateRiskScore(isValid, lineType, isVoip, country);
            String riskLevel = computeRiskLevel(riskScore);

            // 9 Build response
            return PhoneValidationResponse.builder()
                    .valid(true)
                    .country(country)
                    .carrier(carrier)
                    .internationalFormat(internationalFormat)
                    .nationalFormat(nationalFormat)
                    .lineType(lineType)
                    .voip(isVoip)
                    .riskScore(riskScore)
                    .riskLevel(riskLevel)
                    .build();

        } catch (Exception ex) {
            return PhoneValidationResponse.builder()
                    .valid(false)
                    .country(country)
                    .lineType("invalid")
                    .riskScore(90)
                    .riskLevel("HIGH")
                    .build();
        }
    }

    // ----------- Carrier Detection (simple enrichment) -----------
    private String detectCarrier(String national, String international) {

        String input = (national + international).toLowerCase();

        if (input.contains("415")) return "AT&T";
        if (input.contains("916")) return "Verizon";
        if (input.contains("98220") || input.contains("98765")) return "Jio";
        if (input.contains("98100") || input.contains("98710")) return "Airtel";

        return "Unknown";
    }

    // ----------- VOIP Detection -----------
    private boolean detectVoip(String carrier) {
        if (carrier == null) return false;
        String c = carrier.toLowerCase();

        for (String keyword : VOIP_KEYWORDS) {
            if (c.contains(keyword)) return true;
        }
        return false;
    }

    // ----------- Risk Score Calculation -----------
    private int calculateRiskScore(boolean valid, String lineType, boolean isVoip, String country) {
        int score = 0;

        if (!valid) score += 40;
        if (isVoip) score += 30;
        if ("fixed_line".equals(lineType)) score += 10;
        if ("voip".equals(lineType)) score += 20;

        score += COUNTRY_RISK.getOrDefault(country, 10);

        return Math.min(score, 100);
    }

    // ----------- Risk Level Mapping -----------
    private String computeRiskLevel(int score) {
        if (score < 30) return "LOW";
        if (score < 60) return "MEDIUM";
        if (score < 80) return "HIGH";
        return "CRITICAL";
    }

    // ----------- Auto Country Detection -----------
    private String detectCountry(String phone) {
        try {
            Phonenumber.PhoneNumber number = phoneUtil.parse(phone, null);
            return phoneUtil.getRegionCodeForCountryCode(number.getCountryCode());
        } catch (Exception ignored) {}
        return "US";
    }
}
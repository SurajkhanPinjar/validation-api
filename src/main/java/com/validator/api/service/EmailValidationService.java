package com.validator.api.service;

import com.validator.api.dto.response.EmailValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;
import com.validator.api.exception.GlobalExceptionHandler.InvalidEmailException;

@Service
@Slf4j
public class EmailValidationService {

    public EmailValidationResponse validate(String email) {

        // 1 Null or empty email → no validation
        if (email == null || email.isBlank()) {
            return EmailValidationResponse.builder()
                    .syntaxValid(false)
                    .hasMxRecord(false)
                    .smtpStatus("unknown")
                    .disposable(false)
                    .roleBased(false)
                    .score(0)
                    .domain(null)
                    .build();
        }

        // 2 Syntax validation using Apache Commons Validator
        boolean syntaxValid = EmailValidator.getInstance().isValid(email);

        // 3 If syntax invalid → return invalid response
        if (!syntaxValid || !email.contains("@")) {
            return EmailValidationResponse.builder()
                    .syntaxValid(false)
                    .hasMxRecord(false)
                    .smtpStatus("invalid")
                    .disposable(false)
                    .roleBased(false)
                    .score(0)
                    .domain(null)
                    .build();
        }

        // 4 Extract domain safely
        String domain = extractDomain(email);

        // 5 Validate domain
        boolean hasMx = hasMxRecord(domain);
        boolean disposable = isDisposable(domain);
        boolean roleBased = isRoleBased(email);

        // 6 Email quality score (0–100)
        int score = calculateScore(syntaxValid, hasMx, disposable);

        return EmailValidationResponse.builder()
                .syntaxValid(syntaxValid)
                .hasMxRecord(hasMx)
                .smtpStatus("unknown")  // SMTP check can be added later
                .disposable(disposable)
                .roleBased(roleBased)
                .score(score)
                .domain(domain)
                .build();
    }

    // Extract domain safely
    private String extractDomain(String email) {
        if (!email.contains("@")) return null;
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }

    // MX record lookup
    private boolean hasMxRecord(String domain) {
        if (domain == null) return false;

        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            Record[] records = lookup.run();
            return records != null && records.length > 0;
        } catch (Exception e) {
            log.error("MX lookup failed for domain {}: {}", domain, e.getMessage());
            return false;
        }
    }

    // Disposable domain detection
    private boolean isDisposable(String domain) {
        if (domain == null) return false;

        String[] disposableProviders = {
                "tempmail", "10minutemail", "mailinator", "trashmail",
                "guerrillamail", "yopmail", "burnermail", "fakeinbox",
                "dispostable", "throwawaymail"
        };

        for (String d : disposableProviders) {
            if (domain.contains(d)) return true;
        }
        return false;
    }

    // Role-based email (low quality)
    private boolean isRoleBased(String email) {
        if (!email.contains("@")) return false;
        String localPart = email.split("@")[0];

        String[] roles = {
                "admin", "support", "info", "contact", "hello", "sales"
        };

        for (String r : roles) {
            if (localPart.equalsIgnoreCase(r)) return true;
        }
        return false;
    }

    // Email deliverability scoring
    private int calculateScore(boolean syntax, boolean mx, boolean disposable) {
        int score = 0;

        if (syntax) score += 40;
        if (mx) score += 40;
        if (!disposable) score += 20;

        return score;
    }
}
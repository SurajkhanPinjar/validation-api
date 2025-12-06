package com.validator.api.service;

import com.validator.api.dto.response.EmailValidationResponse;
import com.validator.api.validator.SMTPValidator;
import com.validator.api.validator.SmtpResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailValidationService {

    private final DomainReputationService reputationService;
    private final RiskScoringService riskScoringService;

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
                    .reputationScore(null)
                    .reputationCategory("unknown")
                    .riskScore(100)
                    .riskLevel("CRITICAL")
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
                    .reputationScore(null)
                    .reputationCategory("bad")
                    .riskScore(100)
                    .riskLevel("CRITICAL")
                    .build();
        }

        // 4 Extract domain safely
        String domain = extractDomain(email);

        // 5 Validate domain signals
        boolean hasMx = hasMxRecord(domain);
        boolean disposable = isDisposable(domain);
        boolean roleBased = isRoleBased(email);

        // 6 Email quality score (0–100)
        int score = calculateScore(syntaxValid, hasMx, disposable);

        // 7 SMTP check (Your working SMTPValidator)
        SmtpResult smtpResult = SMTPValidator.validate(email, domain);
        String smtpStatus = smtpResult.getStatus(); // valid | invalid | unknown

        // 8 DOMAIN REPUTATION
        DomainReputationService.DomainReputation rep = reputationService.evaluate(domain);
        Integer reputationScore = rep.getReputationScore();    // may be null
        String reputationCategory = rep.getCategory();

        // 9 RISK SCORING
        RiskScoringService.RiskResult risk = riskScoringService.computeRisk(
                syntaxValid,
                hasMx,
                disposable,
                roleBased,
                smtpStatus,
                reputationScore,
                score
        );

        // 10 Final API response
        return EmailValidationResponse.builder()
                .syntaxValid(syntaxValid)
                .hasMxRecord(hasMx)
                .smtpStatus(smtpStatus)
                .disposable(disposable)
                .roleBased(roleBased)
                .score(score)
                .domain(domain)
                .reputationScore(reputationScore)
                .reputationCategory(reputationCategory)
                .riskScore(risk.getRiskScore())
                .riskLevel(risk.getRiskLevel())
                .build();
    }


    // ===== HELPERS =====

    private String extractDomain(String email) {
        if (!email.contains("@")) return null;
        return email.substring(email.indexOf("@") + 1).toLowerCase();
    }

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

    // Simple quality scoring function
    private int calculateScore(boolean syntax, boolean mx, boolean disposable) {
        int score = 0;

        if (syntax) score += 40;
        if (mx) score += 40;
        if (!disposable) score += 20;

        return score;
    }
}
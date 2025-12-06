package com.validator.api.service;

import org.springframework.stereotype.Service;

@Service
public class RiskScoringService {

    /**
     * Compute risk score 0..100 and level
     *
     * Weights (example):
     *  - syntaxValid: 25
     *  - hasMx: 25
     *  - disposable: -40 (penalty)
     *  - roleBased: -10 (penalty)
     *  - smtpStatus: valid -> +20, unknown -> +0, invalid -> -30
     *  - reputationScore contribution: scale to 0..20
     */
    public RiskResult computeRisk(boolean syntaxValid,
                                  boolean hasMx,
                                  boolean disposable,
                                  boolean roleBased,
                                  String smtpStatus,
                                  Integer reputationScore,
                                  int baseQualityScore) {

        int score = 0;

        // syntax
        score += syntaxValid ? 25 : 0;

        // MX
        score += hasMx ? 20 : 0;

        // baseQualityScore (0..100) contributes scaled to 20%
        int qualityContrib = Math.round(baseQualityScore * 0.15f); // up to 15
        score += qualityContrib;

        // reputation (0..100 => 0..20)
        if (reputationScore != null) {
            score += Math.round(reputationScore * 0.2f); // 0..20
        }

        // smtp status
        if ("valid".equalsIgnoreCase(smtpStatus)) score += 20;
        else if ("invalid".equalsIgnoreCase(smtpStatus)) score -= 30;
        else score += 0; // unknown or limited => neutral

        // disposable & role-based penalties
        if (disposable) score -= 40;
        if (roleBased) score -= 10;

        // clamp
        int finalScore = Math.max(0, Math.min(100, score));

        String level = computeLevel(finalScore);

        RiskResult r = new RiskResult();
        r.setRiskScore(finalScore);
        r.setRiskLevel(level);
        return r;
    }

    private String computeLevel(int score) {
        if (score >= 75) return "LOW";        // low fraud risk
        if (score >= 50) return "MEDIUM";
        if (score >= 30) return "HIGH";
        return "CRITICAL";
    }

    public static class RiskResult {
        private int riskScore;
        private String riskLevel;
        public int getRiskScore() { return riskScore; }
        public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    }
}
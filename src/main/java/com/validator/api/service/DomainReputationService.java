package com.validator.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.InetAddress;
import java.util.Set;
import java.util.Arrays;

@Service
@Slf4j
public class DomainReputationService {

    // Expand both lists based on research / feed inputs
    private static final Set<String> FREE_EMAIL_PROVIDERS = Set.of(
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com",
            "protonmail.com", "icloud.com", "mailinator.com", "yopmail.com"
    );

    private static final Set<String> KNOWN_BAD_DOMAINS = Set.of(
            // add any domains you want treated as definitely bad
            "disposable-mail.com", "trashmail.com"
    );

    /**
     * Compute a reputation score (0..100) for a domain.
     * This is heuristic-based and uses MX / SOA / provider lists.
     */
    public DomainReputation evaluate(String domain) {
        DomainReputation rep = new DomainReputation();
        rep.setDomain(domain);

        if (domain == null || domain.isBlank()) {
            rep.setReputationScore(null);
            rep.setCategory("unknown");
            return rep;
        }

        try {
            // 1) MX existence & priority
            Record[] mxRecords = new Lookup(domain, Type.MX).run();
            boolean hasMx = mxRecords != null && mxRecords.length > 0;
            rep.setHasMx(hasMx);

            int mxScore = 0;
            if (hasMx) {
                MXRecord[] mxs = Arrays.stream(mxRecords)
                        .map(r -> (MXRecord) r)
                        .toArray(MXRecord[]::new);

                // lower priority value = better
                int bestPriority = Arrays.stream(mxs).mapToInt(MXRecord::getPriority).min().orElse(100);
                if (bestPriority <= 10) mxScore = 30;
                else if (bestPriority <= 50) mxScore = 24;
                else if (bestPriority <= 100) mxScore = 18;
                else mxScore = 10;

                // if MX host resolves to multiple IPs, slightly better
                String target = mxs[0].getTarget().toString(true);
                try {
                    InetAddress[] ips = InetAddress.getAllByName(target);
                    if (ips != null && ips.length > 1) mxScore += 6;
                } catch (Exception ignored) {}
            } else {
                mxScore = 0;
            }

            // 2) Free provider penalty
            boolean isFreeProvider = FREE_EMAIL_PROVIDERS.contains(domain.toLowerCase());
            int freePenalty = isFreeProvider ? -20 : 0;

            // 3) Known disposable / bad domain
            boolean knownBad = KNOWN_BAD_DOMAINS.contains(domain.toLowerCase());
            int badPenalty = knownBad ? -40 : 0;

            // 4) SOA check (proxy for domain maturity / proper setup)
            int soaScore = 0;
            try {
                Record[] soaRecords = new Lookup(domain, Type.SOA).run();
                if (soaRecords != null && soaRecords.length > 0) {
                    SOARecord soa = (SOARecord) soaRecords[0];
                    long serial = soa.getSerial();
                    // we can't use age reliably; but presence → slightly positive
                    soaScore = 10;
                } else {
                    soaScore = 0;
                }
            } catch (Exception e) {
                soaScore = 0;
            }

            // 5) Heuristic: domain length & hyphens (shorter & clean domains are better)
            int nameScore = 0;
            String dn = domain.split(":")[0];
            if (dn.length() < 15) nameScore = 6;
            else if (dn.length() < 25) nameScore = 3;
            else nameScore = 0;
            if (dn.contains("-")) nameScore -= 2;

            int raw = Math.max(0, mxScore + soaScore + nameScore + (isFreeProvider ? 0 : 20) + freePenalty + badPenalty);
            // cap 0..100
            int finalScore = Math.min(100, Math.max(0, raw));

            rep.setReputationScore(finalScore);

            // compute category
            String category;
            if (finalScore >= 85) category = "excellent";
            else if (finalScore >= 65) category = "good";
            else if (finalScore >= 45) category = "neutral";
            else if (finalScore >= 25) category = "suspicious";
            else category = "bad";

            rep.setCategory(category);

        } catch (TextParseException tpe) {
            log.debug("Reputation lookup failed for domain {}: {}", domain, tpe.getMessage());
            rep.setReputationScore(null);
            rep.setCategory("unknown");
        } catch (Exception e) {
            log.error("Reputation evaluation error for {}: {}", domain, e.getMessage());
            rep.setReputationScore(null);
            rep.setCategory("unknown");
        }

        return rep;
    }

    public static class DomainReputation {
        private String domain;
        private Boolean hasMx;
        private Integer reputationScore;
        private String category;

        // getters/setters
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
        public Boolean getHasMx() { return hasMx; }
        public void setHasMx(Boolean hasMx) { this.hasMx = hasMx; }
        public Integer getReputationScore() { return reputationScore; }
        public void setReputationScore(Integer reputationScore) { this.reputationScore = reputationScore; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
}
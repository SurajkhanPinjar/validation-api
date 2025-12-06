package com.validator.api.service;

import com.validator.api.dto.inputs.BulkPhoneInput;
import com.validator.api.dto.request.BulkValidationRequest;
import com.validator.api.dto.request.ValidationRequest;
import com.validator.api.dto.response.*;
import com.validator.api.dto.result.BulkEmailResult;
import com.validator.api.dto.result.BulkIpResult;
import com.validator.api.dto.result.BulkPhoneResult;
import com.validator.api.dto.result.BulkZipResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ValidationOrchestratorService {

    private final EmailValidationService emailService;
    private final PhoneValidationService phoneService;
    private final IpValidationService ipService;
    private final ZipValidationService zipService;

    public FinalValidationResponse validateAll(ValidationRequest request) {

        // Safe: always returns a response object, no null
        EmailValidationResponse email = safeEmail(request.getEmail());
        PhoneValidationResponse phone = safePhone(request.getPhone(), request.getCountry());
        IpValidationResponse ip = safeIp(request.getIp());
        ZipValidationResponse zip = safeZip(request.getZipcode());

        String risk = calculateRisk(email, phone, ip, zip);

        return FinalValidationResponse.builder()
                .email(email)
                .phone(phone)
                .ip(ip)
                .zipcode(zip)
                .risk(risk)
                .build();
    }

    // ---------------- SAFE WRAPPERS ---------------- //

    public EmailValidationResponse safeEmail(String email) {
        EmailValidationResponse res = emailService.validate(email);
        return res != null ? res : EmailValidationResponse.builder()
                .syntaxValid(false)
                .hasMxRecord(false)
                .smtpStatus("unknown")
                .disposable(false)
                .roleBased(false)
                .score(0)
                .domain(null)
                .build();
    }

    public PhoneValidationResponse safePhone(String phone, String country) {
        PhoneValidationResponse res = phoneService.validate(phone, country);
        return res != null ? res : PhoneValidationResponse.builder()
                .valid(false)
                .country(country)
                .lineType("invalid")
                .build();
    }

    public IpValidationResponse safeIp(String ip) {
        IpValidationResponse res = ipService.validate(ip);
        return res != null ? res : IpValidationResponse.builder()
                .valid(false)
                .version("unknown")
                .isPrivate(false)
                .isReserved(false)
                .build();
    }

    public ZipValidationResponse safeZip(String zipcode) {
        ZipValidationResponse res = zipService.validate(zipcode);
        return res != null ? res : ZipValidationResponse.builder()
                .valid(false)
                .state(null)
                .city(null)
                .country("US")
                .build();
    }

    // ---------------- RISK LOGIC (ADVANCED) ---------------- //

    private String calculateRisk(
            EmailValidationResponse email,
            PhoneValidationResponse phone,
            IpValidationResponse ip,
            ZipValidationResponse zip) {

        // If email disposable → highest risk
        if (email.isDisposable()) return "HIGH";

        // Invalid phone indicates potential fraud
        if (!phone.isValid()) return "MEDIUM";

        // IP is private OR reserved → normal
        if (ip.isPrivate() || ip.isReserved()) return "LOW";

        // ZIP invalid → small risk
        if (!zip.isValid()) return "MEDIUM";

        // Default
        return "LOW";
    }

    public BulkValidationResponse validateBulk(BulkValidationRequest request) {

        // ---------- 1) EMAIL BULK VALIDATION ----------
        List<String> emails = safeList(request.getEmails());
        List<BulkEmailResult> emailResults =
                IntStream.range(0, emails.size())
                        .mapToObj(i -> {
                            String email = emails.get(i);
                            return BulkEmailResult.builder()
                                    .index(i)
                                    .input(email)
                                    .result(safeEmail(email))
                                    .build();
                        })
                        .toList();

        // ---------- 2) PHONE BULK VALIDATION ----------
        List<BulkPhoneInput> phoneInputs = safeList(request.getPhones());
        List<BulkPhoneResult> phoneResults =
                IntStream.range(0, phoneInputs.size())
                        .mapToObj(i -> {
                            BulkPhoneInput p = phoneInputs.get(i);
                            return BulkPhoneResult.builder()
                                    .index(i)
                                    .input(p.getNumber())
                                    .country(p.getCountry())
                                    .result(safePhone(p.getNumber(), p.getCountry()))
                                    .build();
                        })
                        .toList();

        // ---------- 3) IP BULK VALIDATION ----------
        List<BulkIpResult> ipResults =
                IntStream.range(0, safeList(request.getIps()).size())
                        .mapToObj(i -> {
                            String ip = request.getIps().get(i);
                            return BulkIpResult.builder()
                                    .index(i)
                                    .input(ip)
                                    .result(safeIp(ip))
                                    .build();
                        })
                        .toList();

        // ---------- 4) ZIP BULK VALIDATION ----------
        List<BulkZipResult> zipResults =
                IntStream.range(0, safeList(request.getZipcodes()).size())
                        .mapToObj(i -> {
                            String zip = request.getZipcodes().get(i);
                            return BulkZipResult.builder()
                                    .index(i)
                                    .input(zip)
                                    .result(safeZip(zip))
                                    .build();
                        })
                        .toList();

        // ---------- FINAL RESPONSE ----------
        return BulkValidationResponse.builder()
                .emails(emailResults)
                .phones(phoneResults)
                .ips(ipResults)
                .zipcodes(zipResults)
                .build();
    }

    private <T> List<T> safeList(List<T> input) {
        return (input == null) ? Collections.emptyList() : input;
    }
}
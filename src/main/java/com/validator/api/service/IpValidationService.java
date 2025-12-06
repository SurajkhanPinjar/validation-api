package com.validator.api.service;

import com.validator.api.dto.response.IpValidationResponse;
import com.validator.api.exception.GlobalExceptionHandler.InvalidIpException;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class IpValidationService {

    public IpValidationResponse validate(String ip) {

        // 1 Null / empty request = invalid
        if (ip == null || ip.isBlank()) {
            return IpValidationResponse.builder()
                    .valid(false)
                    .version("unknown")
                    .isPrivate(false)
                    .isReserved(false)
                    .build();
        }

        try {
            InetAddress address = InetAddress.getByName(ip);

            boolean isPrivate = address.isSiteLocalAddress()
                    || address.isLinkLocalAddress()
                    || address.isLoopbackAddress();

            boolean isReserved = address.isAnyLocalAddress()
                    || address.isMulticastAddress()
                    || address.isLoopbackAddress();

            String version = isIPv6(ip) ? "IPv6" : "IPv4";

            return IpValidationResponse.builder()
                    .valid(true)
                    .version(version)
                    .isPrivate(isPrivate)
                    .isReserved(isReserved)
                    .build();

        } catch (UnknownHostException e) {
            return IpValidationResponse.builder()
                    .valid(false)
                    .version("unknown")
                    .isPrivate(false)
                    .isReserved(false)
                    .build();
        }
    }

    // Detect IPv6 more accurately
    private boolean isIPv6(String ip) {
        return ip.contains(":");
    }
}
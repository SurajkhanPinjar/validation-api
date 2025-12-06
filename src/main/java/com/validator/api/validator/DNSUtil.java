package com.validator.api.validator;

import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DNSUtil {

    public static List<String> getMxRecords(String domain) {
        List<String> mxHosts = new ArrayList<>();

        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            Record[] records = lookup.run();

            if (records != null) {
                for (Record record : records) {
                    String mx = record.rdataToString().split(" ")[1];
                    mxHosts.add(mx);
                }
            }
        } catch (Exception e) {
            log.error("DNS MX Lookup failed for domain {}: {}", domain, e.getMessage());
        }

        return mxHosts;
    }
}
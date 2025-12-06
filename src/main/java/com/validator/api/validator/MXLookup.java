package com.validator.api.validator;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.Arrays;

public class MXLookup {

    public static String findBestMxRecord(String domain) {
        try {
            Record[] records = new Lookup(domain, Type.MX).run();

            if (records == null) return null;

            return Arrays.stream(records)
                    .map(r -> (MXRecord) r)
                    .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
                    .findFirst()
                    .map(MXRecord::getTarget)
                    .map(Name::toString)
                    .orElse(null);

        } catch (Exception e) {
            return null;
        }
    }
}
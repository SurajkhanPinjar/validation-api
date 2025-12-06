package com.validator.api.validator;

import com.validator.api.validator.SmtpResult;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Slf4j
public class SMTPValidator {

    public static SmtpResult validate(String email, String domain) {

        SmtpResult result = new SmtpResult();
        result.setEmail(email);

        // 1️⃣ Skip SMTP for disposable domains (always invalid)
        if (domain.contains("mailinator") || domain.contains("tempmail")
                || domain.contains("yopmail") || domain.contains("guerrillamail")) {

            result.setStatus("invalid");
            return result;
        }

        try {
            // 2️⃣ Resolve MX records
            List<String> mxHosts = DNSUtil.getMxRecords(domain);

            if (mxHosts.isEmpty()) {
                result.setStatus("invalid");
                return result;
            }

            String mxHost = mxHosts.get(0);
            log.debug("SMTP connecting to: {}", mxHost);

            // 3️⃣ CLOUD-FRIENDLY timeout (Railway / Render safe)
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(mxHost, 25), 2000); // 2s timeout

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            reader.readLine(); // server greeting

            writer.println("HELO validator.com");
            reader.readLine();

            writer.println("MAIL FROM:<check@validator.com>");
            reader.readLine();

            writer.println("RCPT TO:<" + email + ">");
            String rcptResponse = reader.readLine();

            log.debug("RCPT Response: {}", rcptResponse);

            writer.println("QUIT");
            socket.close();

            // 4️⃣ Interpret SMTP response
            if (rcptResponse != null && rcptResponse.startsWith("250")) {
                result.setStatus("valid");
            } else if (rcptResponse != null && rcptResponse.startsWith("550")) {
                result.setStatus("invalid");
            } else {
                result.setStatus("unknown"); // greylisted / throttled / blocked
            }

        } catch (Exception e) {
            log.error("SMTP Error: {}", e.getMessage());
            result.setStatus("unknown"); // Cloud fallback
        }

        return result;
    }
}
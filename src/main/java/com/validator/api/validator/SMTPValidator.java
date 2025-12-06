package com.validator.api.validator;

import jakarta.mail.Session;
import jakarta.mail.Store;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

@Slf4j
public class SMTPValidator {

    public static SmtpResult validate(String email, String domain) {
        SmtpResult result = new SmtpResult();
        result.setEmail(email);

        try {
            String mxHost = MXLookup.findBestMxRecord(domain);
            if (mxHost == null) {
                result.setStatus("mx_not_found");
                return result;
            }

            log.debug("SMTP connecting to: {}", mxHost);

            Socket socket = new Socket(mxHost, 25);
            socket.setSoTimeout(5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String response = reader.readLine();
            log.debug("SMTP banner: {}", response);

            writer.println("HELO validator.com");
            reader.readLine();

            writer.println("MAIL FROM:<check@validator.com>");
            reader.readLine();

            writer.println("RCPT TO:<" + email + ">");
            String rcptResponse = reader.readLine();
            log.debug("RCPT Response: {}", rcptResponse);

            writer.println("QUIT");

            if (rcptResponse.startsWith("250")) {
                result.setStatus("valid");
            } else if (rcptResponse.startsWith("550")) {
                result.setStatus("invalid");
            } else {
                result.setStatus("unknown");
            }

            socket.close();
        } catch (Exception e) {
            log.error("SMTP Error: {}", e.getMessage());
            result.setStatus("unknown");
        }

        return result;
    }
}
package com.reitzel.invoiceApproval.whatsappservice;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class WhatsAppService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.whatsappFrom}")
    private String whatsappFrom;

    // Initialize Twilio
    private void initialize() {
        Twilio.init(accountSid, authToken);
    }

    // Send text message
    public String sendTextMessage(String toNumber, String messageBody) {
        initialize();

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + toNumber),
                new PhoneNumber(whatsappFrom),
                messageBody)
            .create();

        return message.getSid();
    }

 // Send media message using Google Drive file ID
    public String sendMediaMessage(String toNumber, String caption, String driveFileId) {
        initialize();

        try {
            // Build direct download link
            String mediaUrl = "https://drive.google.com/uc?export=download&id=" + driveFileId;

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toNumber),
                    new PhoneNumber(whatsappFrom),
                    caption)
                .setMediaUrl(List.of(URI.create(mediaUrl)))
                .create();

            return message.getSid();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send media message: " + e.getMessage());
        }
    }
}

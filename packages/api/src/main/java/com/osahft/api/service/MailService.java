package com.osahft.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService implements MailServiceIF {

    @Autowired
    private JavaMailSender emailSender;

    private SimpleMailMessage createSimpleMail(String to, String from, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        return message;
    }

    @Override
    public void sendVerificationCode(
            String to, long verificationCode) {
        SimpleMailMessage message = createSimpleMail(to, "verification@osahft.de", "Your OSAHFT verification code");
        message.setText("Code to verify your identity to OSAHFT " + verificationCode);
        emailSender.send(message);
    }

    @Override
    public void sendDownloadLink(String to, String from, String subject, String text, String downloadLink) {
        SimpleMailMessage message = createSimpleMail(to, from, subject);
        message.setText(text + " " + downloadLink);
        emailSender.send(message);
    }
}

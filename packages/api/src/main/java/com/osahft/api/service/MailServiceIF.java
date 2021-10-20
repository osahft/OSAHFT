package com.osahft.api.service;

public interface MailServiceIF {

    void sendVerificationCode(String to, long verificationCode);

    void sendDownloadLink(String to, String from, String subject, String text, String downloadLink);
}

package com.osahft.api.service;

import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.repository.MailTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class MailService implements MailServiceIF {

    @Value("${spring.mail.username}")
    private String mailSender;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    private SimpleMailMessage createSimpleMail(String to, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender);
        message.setTo(to);
        message.setSubject(subject);
        return message;
    }

    private MailTransfer findMailTransferById(String transferId) throws MailTransferRepositoryException {
        return mailTransferRepository
                .findById(transferId)
                .orElseThrow(() -> new MailTransferRepositoryException("Could not find MailTransfer with id:" + transferId));
    }

    @Override
    public void sendAuthenticationCode(String transferId) throws MailTransferRepositoryException {
        MailTransfer mailTransfer = findMailTransferById(transferId);

        SimpleMailMessage message = createSimpleMail(mailTransfer.getMailSender(), "Your OSAHFT verification code");
        message.setText("Your code to verify your identity to OSAHFT: " + mailTransfer.getAuthenticationCode());
        emailSender.send(message);
    }

    @Override
    public void sendDownloadLink(String transferId) throws MailTransferRepositoryException {
        MailTransfer mailTransfer = findMailTransferById(transferId);
        for (MailReceiverDownloadLinkMapping mapping : mailTransfer.getMailReceiverDownloadLinkMapping()) {
            SimpleMailMessage message = createSimpleMail(mapping.getMailReceiver(), mailTransfer.getMailSender() + " has sent you " + mailTransfer.getTitle() + " via OSAHFT");
            message.setText(mailTransfer.getMessage() + "\nDownload link: " + mapping.getDownloadLink());
            emailSender.send(message);
        }
    }

    @Override
    public void sendSuccessMessage(String transferId) throws MailTransferRepositoryException {
        MailTransfer mailTransfer = findMailTransferById(transferId);
        String receivers = mailTransfer.getMailReceiverDownloadLinkMapping()
                .stream()
                .map(MailReceiverDownloadLinkMapping::getMailReceiver)
                .collect(Collectors.joining(","));
        SimpleMailMessage message = createSimpleMail(mailTransfer.getMailSender(), mailTransfer.getTitle() + " sent successfully to " + receivers);
        message.setText("Thanks for using OSAHFT, the download link has been successfully sent to: " + receivers);
        emailSender.send(message);
    }

    @Override
    public void sendErrorMessage(String transferId) throws MailTransferRepositoryException {
        MailTransfer mailTransfer = findMailTransferById(transferId);
        SimpleMailMessage message = createSimpleMail(mailTransfer.getMailSender(), "Your OSAHFT file transfer " + mailTransfer.getTitle() + " failed.");
        message.setText("We are sorry to tell you that your OSAHFT file transfer " + mailTransfer.getTitle() + " failed. Please try again later.");
        emailSender.send(message);
    }
}

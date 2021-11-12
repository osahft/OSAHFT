package com.osahft.api.internal.helper;

import com.osahft.api.document.MailTransfer;
import com.osahft.api.internal.exception.TestCaseFailedException;
import com.osahft.api.repository.MailTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class TestHelper {

    @Autowired
    private MailTransferRepository mailTransferRepository;

    public String generateRandomEmail() {
        return generateRandomString() + "@" + generateRandomString() + ".com";
    }

    public String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    public MultipartFile generateRandomFile() {
        return new MockMultipartFile(generateRandomString(), generateRandomString().getBytes(StandardCharsets.UTF_8));
    }

    public MailTransfer getMailTransfer(String mailTransferId) throws TestCaseFailedException {
        return mailTransferRepository.findById(mailTransferId)
                .orElseThrow(TestCaseFailedException::new);
    }

}

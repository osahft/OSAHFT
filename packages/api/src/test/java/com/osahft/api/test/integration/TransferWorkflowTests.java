package com.osahft.api.test.integration;

import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.FileServiceClientServiceException;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.internal.helper.TestHelper;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import com.osahft.api.service.FileServiceClientServiceIF;
import com.osahft.api.service.LocalFileStorageServiceIF;
import com.osahft.api.service.MailServiceIF;
import com.osahft.api.service.TransferServiceIF;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.osahft.api.document.MailTransfer.State.AUTHORIZED;
import static com.osahft.api.document.MailTransfer.State.FINISHED;
import static com.osahft.api.internal.assertion.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class TransferWorkflowTests {

    @Autowired
    private TestHelper testHelper;

    @MockBean
    private MailServiceIF mailService;

    @MockBean
    private LocalFileStorageServiceIF localFileStorageService;

    @MockBean
    @Qualifier("FileServiceClientServiceIF")
    private FileServiceClientServiceIF fileServiceClientService;

    @Autowired
    private TransferServiceIF transferService;

    @Test
    void testCreateNewMailTransferOneReceiver() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();
        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        // check created MailTransfer in database
        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());
        assertThat(mailTransfer)
                .hasMailSender(request.getMailSender())
                .hasMailReceiverDownloadLinkMapping(new MailReceiverDownloadLinkMapping(null, null, request.getMailReceivers().get(0)))
                .hasMessage(request.getMessage())
                .hasTitle(request.getTitle());
    }

    @Test
    void testCreateNewMailTransferMultipleReceivers() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Arrays.asList(testHelper.generateRandomEmail(), testHelper.generateRandomEmail(), testHelper.generateRandomEmail(), testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        // create mappings for check
        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());
        List<MailReceiverDownloadLinkMapping> mappings = request.getMailReceivers()
                .stream()
                .map(receiver -> new MailReceiverDownloadLinkMapping(null, null, receiver))
                .collect(Collectors.toList());

        // check created MailTransfer in database
        assertThat(mailTransfer)
                .hasMailSender(request.getMailSender())
                .hasMailReceiverDownloadLinkMapping(mappings)
                .hasMessage(request.getMessage())
                .hasTitle(request.getTitle());
    }


    @Test
    void testAuthenticateMailTransfer() throws MailTransferRepositoryException, TransferServiceException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

        // authorize user for MailTransfer
        transferService.authorizeUser(response.getMailTransferId(), mailTransfer.getAuthenticationCode());

        mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

        // check state is authorized
        assertThat(mailTransfer).hasState(AUTHORIZED);
    }

    @Test
    void testUploadFile() throws MailTransferRepositoryException, TransferServiceException, LocalFileStorageServiceException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

        // authorize user for MailTransfer
        transferService.authorizeUser(response.getMailTransferId(), mailTransfer.getAuthenticationCode());

        // upload files
        List<MultipartFile> files = Collections.singletonList(testHelper.generateRandomFile());
        transferService.uploadFiles(response.getMailTransferId(), files);

        // verify
        Mockito.verify(localFileStorageService).storeFiles(response.getMailTransferId(), files);
    }

    @Test
    void testCompleteMailTransfer() throws MailTransferRepositoryException, TransferServiceException, LocalFileStorageServiceException, FileServiceClientServiceException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

        // authorize user for MailTransfer
        transferService.authorizeUser(response.getMailTransferId(), mailTransfer.getAuthenticationCode());

        // upload files
        List<MultipartFile> files = Collections.singletonList(testHelper.generateRandomFile());
        transferService.uploadFiles(response.getMailTransferId(), files);

        // mock readFiles
        Mockito.when(localFileStorageService.readFiles(response.getMailTransferId()))
                .thenReturn(Collections.singletonList(new File(testHelper.generateRandomString())));

        // complete mailTransfer
        transferService.completeMailTransfer(response.getMailTransferId());

        // wait until state is finished but max 5s
        long millis = TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS) + System.currentTimeMillis();
        MailTransfer.State mailTransferState;
        do {
            mailTransferState = testHelper.getMailTransfer(response.getMailTransferId())
                    .getState();
            if (mailTransferState.equals(FINISHED))
                break;
        }
        while (millis > System.currentTimeMillis());

        // check state
        assertThat(mailTransferState)
                .isEqualTo(FINISHED);
    }

}

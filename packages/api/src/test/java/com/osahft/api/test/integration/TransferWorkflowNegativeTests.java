package com.osahft.api.test.integration;

import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.helper.ErrorHelper;
import com.osahft.api.internal.helper.TestHelper;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import com.osahft.api.repository.MailTransferRepository;
import com.osahft.api.service.LocalFileStorageServiceIF;
import com.osahft.api.service.MailServiceIF;
import com.osahft.api.service.TransferServiceIF;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.osahft.api.document.MailTransfer.State.*;
import static com.osahft.api.internal.assertion.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class TransferWorkflowNegativeTests {

    @Autowired
    private TestHelper testHelper;

    @MockBean
    private MailServiceIF mailService;

    @MockBean
    private LocalFileStorageServiceIF localFileStorageService;

    @Autowired
    private TransferServiceIF transferService;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    @Test
    void testAuthenticateMailTransferInvalidCode() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> transferService.authorizeUser(response.getMailTransferId(), -1));

        MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

        // check state not authorized
        assertThat(mailTransfer).hasState(STARTED);

        // check ErrorResponse
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("Could not authorize user due to invalid authenticationCode: -1."));
    }

    @Test
    void testAuthenticateMailTransferInvalidCodeLocked() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        for (int i = 0; i < 6; i++) {
            // authorize user for MailTransfer
            TransferServiceException e = assertThrows(TransferServiceException.class,
                    () -> transferService.authorizeUser(response.getMailTransferId(), -1));

            MailTransfer mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());

            if (i < 3) {
                // check state not authorized
                assertThat(mailTransfer)
                        .hasState(STARTED);

                // check ErrorResponse
                assertThat(e.getError())
                        .isEqualTo(ErrorHelper.getBAD_REQUEST("Could not authorize user due to invalid authenticationCode: -1."));
            } else {
                assertThat(mailTransfer)
                        .hasState(LOCKED);
                // check ErrorResponse
                assertThat(e.getError())
                        .isEqualTo(ErrorHelper.getFORBIDDEN("User for MailTransfer " + mailTransfer.getId() + " is locked because of too many failed authentication attempts."));
            }
        }
    }

    @Test
    void testUploadFileUnauthorized() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // upload files
                    List<MultipartFile> files = Collections.singletonList(testHelper.generateRandomFile());
                    transferService.uploadFiles(response.getMailTransferId(), files);
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getUNAUTHORIZED());
    }

    @Test
    void testUploadFilesNull() throws MailTransferRepositoryException, TransferServiceException {
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

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // upload files
                    transferService.uploadFiles(response.getMailTransferId(), null);
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("Files array must not be empty or null."));
    }

    @Test
    void testUploadFilesEmpty() throws MailTransferRepositoryException, TransferServiceException {
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

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // upload files
                    transferService.uploadFiles(response.getMailTransferId(), Collections.emptyList());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("Files array must not be empty or null."));
    }

    @Test
    void testUploadFileDuplicateFile() throws MailTransferRepositoryException, TransferServiceException {
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

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // upload files
                    MultipartFile file = testHelper.generateRandomFile();
                    List<MultipartFile> files = Arrays.asList(file, file);
                    transferService.uploadFiles(response.getMailTransferId(), files);
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("Duplicate file names are currently not supported."));
    }


    @Test
    void testCompleteMailTransferUnauthorized() throws MailTransferRepositoryException {
        // create MailTransfer
        CreateMailTransferRequest request = CreateMailTransferRequest.builder()
                .mailSender(testHelper.generateRandomEmail())
                .mailReceivers(Collections.singletonList(testHelper.generateRandomEmail()))
                .message(testHelper.generateRandomString())
                .title(testHelper.generateRandomString())
                .build();

        CreateMailTransferResponse response = transferService.createNewMailTransfer(request);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // complete mailTransfer
                    transferService.completeMailTransfer(response.getMailTransferId());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getUNAUTHORIZED());

    }

    @Test
    void testCompleteMailTransferEmptyFiles() throws MailTransferRepositoryException, TransferServiceException {
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

        // mock readFiles
        Mockito.when(localFileStorageService.readFiles(response.getMailTransferId()))
                .thenReturn(Collections.emptyList());

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // complete mailTransfer
                    transferService.completeMailTransfer(response.getMailTransferId());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("No files for MailTransfer " + response.getMailTransferId() + " found."));
    }

    @Test
    void testCompleteMailTransferNullFiles() throws MailTransferRepositoryException, TransferServiceException {
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

        // mock readFiles
        Mockito.when(localFileStorageService.readFiles(response.getMailTransferId()))
                .thenReturn(null);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // complete mailTransfer
                    transferService.completeMailTransfer(response.getMailTransferId());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("No files for MailTransfer " + response.getMailTransferId() + " found."));
    }

    @Test
    void testCompleteMailTransferAlreadyTriggered() throws MailTransferRepositoryException, TransferServiceException {
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

        // mock readFiles
        Mockito.when(localFileStorageService.readFiles(response.getMailTransferId()))
                .thenReturn(Collections.singletonList(new File(testHelper.generateRandomString())));

        // set state to triggered
        mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());
        mailTransfer.setState(TRIGGERED);
        mailTransferRepository.save(mailTransfer);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // complete mailTransfer
                    transferService.completeMailTransfer(response.getMailTransferId());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("MailTransfer is already triggered."));
    }

    @Test
    void testCompleteMailTransferAlreadyFinished() throws MailTransferRepositoryException, TransferServiceException {
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

        // mock readFiles
        Mockito.when(localFileStorageService.readFiles(response.getMailTransferId()))
                .thenReturn(Collections.singletonList(new File(testHelper.generateRandomString())));

        // set state to triggered
        mailTransfer = testHelper.getMailTransfer(response.getMailTransferId());
        mailTransfer.setState(FINISHED);
        mailTransferRepository.save(mailTransfer);

        TransferServiceException e = assertThrows(TransferServiceException.class,
                () -> {
                    // complete mailTransfer
                    transferService.completeMailTransfer(response.getMailTransferId());
                });
        assertThat(e.getError())
                .isEqualTo(ErrorHelper.getBAD_REQUEST("MailTransfer is already finished."));
    }

}

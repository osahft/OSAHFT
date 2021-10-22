package com.osahft.api.service;

import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import com.osahft.api.repository.MailTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferService implements TransferServiceIF {

    @Autowired
    private LocalFileStorageServiceIF localFileStorageService;

    @Autowired
    @Qualifier("FileServiceClientServiceIF")
    private FileServiceClientServiceIF fileServiceClientService;

    @Autowired
    private MailServiceIF mailService;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    @Autowired
    @Qualifier("TaskExecutor")
    private TaskExecutor taskExecutor;

    private MailTransfer getMailTransfer(String mailTransferId) throws MailTransferRepositoryException {
        return mailTransferRepository.findById(mailTransferId)
                .orElseThrow(() -> new MailTransferRepositoryException("Could not find MailTransfer with id:" + mailTransferId));
    }

    private void checkAuthentication(String mailTransferId) throws TransferServiceException, MailTransferRepositoryException {
        if (!getMailTransfer(mailTransferId).getIsAuthenticated())
            throw new TransferServiceException("User is not authenticated. Please authenticate user first.");
    }

    @Override
    public CreateMailTransferResponse createNewMailTransfer(CreateMailTransferRequest createMailTransferRequest) throws MailTransferRepositoryException {

        // create and store MailTransfer
        MailTransfer mailTransfer = MailTransfer.builder()
                .mailSender(createMailTransferRequest.getMailSender())
                .mailReceiverDownloadLinkMapping(createMailTransferRequest
                        .getMailReceivers()
                        .stream()
                        .map(receiver -> new MailReceiverDownloadLinkMapping(null, null, receiver))
                        .collect(Collectors.toList()))
                .title(createMailTransferRequest.getTitle())
                .message(createMailTransferRequest.getMessage())
                .build();
        mailTransferRepository.save(mailTransfer);

        // send authentication code via email
        mailService.sendAuthenticationCode(mailTransfer.getId());

        // create dataDir
        localFileStorageService.createStorage(mailTransfer.getId());

        return CreateMailTransferResponse.builder()
                .mailTransferId(mailTransfer.getId())
                .build();
    }

    @Override
    public void uploadFiles(String mailTransferId, List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException, TransferServiceException {
        checkAuthentication(mailTransferId);
        localFileStorageService.storeFiles(mailTransferId, files);
    }

    @Override
    public void completeMailTransfer(String mailTransferId) throws MailTransferRepositoryException, TransferServiceException {
        checkAuthentication(mailTransferId);

        taskExecutor.execute(() -> {
            try {
                // set MailTransfer.State.TRIGGERED
                MailTransfer mailTransfer = getMailTransfer(mailTransferId);
                mailTransfer.setState(MailTransfer.State.TRIGGERED);
                mailTransferRepository.save(mailTransfer);

                fileServiceClientService.uploadFiles(mailTransferId);
                fileServiceClientService.createDownloadLinks(mailTransferId);
                mailService.sendDownloadLink(mailTransferId);
                mailService.sendSuccessMessage(mailTransferId);

                // set MailTransfer.State.FINISHED
                mailTransfer = getMailTransfer(mailTransferId);
                mailTransfer.setState(MailTransfer.State.TRIGGERED);
                mailTransfer.setState(MailTransfer.State.FINISHED);

            } catch (Exception e) {
                try {
                    log.error("Could not complete mail transfer");
                    mailService.sendErrorMessage(mailTransferId);
                } catch (MailTransferRepositoryException ex) {
                    log.error("Could not send error message");
                }
            }
        });
    }

    @Override
    public void authenticateUser(String mailTransferId, Integer authenticationCode) throws MailTransferRepositoryException, TransferServiceException {
        MailTransfer mailTransfer = getMailTransfer(mailTransferId);
        if (mailTransfer.getAuthenticationCode().equals(authenticationCode)) {
            mailTransfer.setIsAuthenticated(true);
            mailTransferRepository.save(mailTransfer);
        } else
            throw new TransferServiceException("Could not authenticate user due to invalid authenticationCode: " + authenticationCode);
    }


}

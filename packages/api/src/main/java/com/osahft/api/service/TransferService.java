package com.osahft.api.service;

import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.helper.ErrorHelper;
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

import static com.osahft.api.document.MailTransfer.State.*;

@Service
@Slf4j
public class TransferService implements TransferServiceIF {

    private static final int NUMBER_OF_AUTHENTICATION_ATTEMPTS = 5;

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
                .orElseThrow(() -> new MailTransferRepositoryException(mailTransferId));
    }

    private void checkAuthorization(String mailTransferId) throws TransferServiceException, MailTransferRepositoryException {
        if (!getMailTransfer(mailTransferId).isAuthorized())
            throw new TransferServiceException(ErrorHelper.getUNAUTHORIZED());
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

        return new CreateMailTransferResponse(mailTransfer.getId());
    }

    @Override
    public void uploadFiles(String mailTransferId, List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException, TransferServiceException {
        checkAuthorization(mailTransferId);

        // check for duplicate file names
        long distinctFilesNames = files.stream().map(MultipartFile::getName).distinct().count();
        if ((long) files.size() != distinctFilesNames)
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("Duplicate file names are currently not supported."));

        localFileStorageService.storeFiles(mailTransferId, files);
    }

    @Override
    public void completeMailTransfer(String mailTransferId) throws MailTransferRepositoryException, TransferServiceException {
        // check MailTransfer is not finished yet
        if (getMailTransfer(mailTransferId).getState().equals(TRIGGERED)) {
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("MailTransfer is already triggered."));
        }
        // check MailTransfer is not finished yet
        if (getMailTransfer(mailTransferId).getState().equals(FINISHED)) {
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("MailTransfer is already finished."));
        }

        // check authorization
        checkAuthorization(mailTransferId);

        // check that files exist
        if (localFileStorageService.readFiles(mailTransferId).isEmpty())
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("No files for MailTransfer " + mailTransferId + " found."));

        taskExecutor.execute(() -> {
            try {
                // set TRIGGERED
                MailTransfer mailTransfer = getMailTransfer(mailTransferId);
                mailTransfer.setState(TRIGGERED);
                mailTransferRepository.save(mailTransfer);

                fileServiceClientService.uploadFiles(mailTransferId);
                fileServiceClientService.createDownloadLinks(mailTransferId);
                mailService.sendDownloadLink(mailTransferId);
                mailService.sendSuccessMessage(mailTransferId);

                // set FINISHED
                mailTransfer = getMailTransfer(mailTransferId);
                mailTransfer.setState(TRIGGERED);
                mailTransfer.setState(FINISHED);

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
    public void authorizeUser(String mailTransferId, Integer authenticationCode) throws MailTransferRepositoryException, TransferServiceException {
        MailTransfer mailTransfer = getMailTransfer(mailTransferId);

        // check user hasn't exceeded authentication attempts
        if (mailTransfer.getAuthenticationAttempts() >= NUMBER_OF_AUTHENTICATION_ATTEMPTS && !mailTransfer.getState().equals(LOCKED)) {
            mailTransfer.setState(LOCKED);
            mailTransferRepository.save(mailTransfer);
        }
        // check user isn't locked
        if (mailTransfer.getState().equals(LOCKED)) {
            throw new TransferServiceException(ErrorHelper.getFORBIDDEN("User for MailTransfer " + mailTransferId + " is locked because of to many failed authentication attempts."));
        }

        // check user isn't authorized yet
        if (mailTransfer.isAuthorized())
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("User for MailTransfer " + mailTransferId + " is already authorized."));

        if (mailTransfer.getAuthenticationCode().equals(authenticationCode)) {
            mailTransfer.setState(AUTHORIZED);
            mailTransferRepository.save(mailTransfer);
        } else {
            mailTransfer.incrementAuthenticationAttempts();
            mailTransferRepository.save(mailTransfer);
            throw new TransferServiceException(ErrorHelper.getBAD_REQUEST("Could not authorize user due to invalid authenticationCode: " + authenticationCode + "."));
        }
    }

}

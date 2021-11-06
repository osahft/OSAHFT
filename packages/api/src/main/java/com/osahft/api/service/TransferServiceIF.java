package com.osahft.api.service;

import com.osahft.api.exception.FileServiceClientServiceException;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransferServiceIF {
    CreateMailTransferResponse createNewMailTransfer(CreateMailTransferRequest createMailTransferRequest) throws MailTransferRepositoryException;

    void uploadFiles(String mailTransferId, List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException, TransferServiceException;

    void completeMailTransfer(String mailTransferId) throws FileServiceClientServiceException, MailTransferRepositoryException, TransferServiceException;

    void authorizeUser(String mailTransferId, Integer authenticationCode) throws MailTransferRepositoryException, TransferServiceException;
}

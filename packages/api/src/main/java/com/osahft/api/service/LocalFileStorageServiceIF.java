package com.osahft.api.service;

import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface LocalFileStorageServiceIF {
    void createStorage(String transferId) throws MailTransferRepositoryException;

    void storeFiles(String transferId, List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException;

    List<File> readFiles(String transferId) throws MailTransferRepositoryException, LocalFileStorageServiceException;

    void deleteStorage(String transferId) throws MailTransferRepositoryException, LocalFileStorageServiceException;
}

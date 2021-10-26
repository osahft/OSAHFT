package com.osahft.api.service;

import com.osahft.api.exception.MailTransferRepositoryException;

public interface MailServiceIF {
    void sendAuthenticationCode(String transferId) throws MailTransferRepositoryException;

    void sendDownloadLink(String transferId) throws MailTransferRepositoryException;

    void sendSuccessMessage(String transferId) throws MailTransferRepositoryException;

    void sendErrorMessage(String transferId) throws MailTransferRepositoryException;
}

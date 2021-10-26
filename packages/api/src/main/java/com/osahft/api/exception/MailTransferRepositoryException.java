package com.osahft.api.exception;

public class MailTransferRepositoryException extends OSAHFTApiException {
    public MailTransferRepositoryException(String transferId) {
        super("Could not find MailTransfer with id:" + transferId);
    }
}

package com.osahft.api.exception;

import com.osahft.api.helper.ErrorHelper;

public class MailTransferRepositoryException extends OSAHFTApiException {
    public MailTransferRepositoryException(String transferId) {
        super(ErrorHelper.getNOT_FOUND(transferId));
    }
}

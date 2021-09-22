package com.osahft.api.service;

import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransferServiceIF {
    CreateMailTransferResponse createNewMailTransfer(CreateMailTransferRequest createMailTransferRequest);

    void uploadFiles(Long mailTransferId, List<MultipartFile> files);

    void completeMailTransfer(Long mailTransferId);
}

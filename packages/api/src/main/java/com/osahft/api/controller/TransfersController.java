package com.osahft.api.controller;

import com.osahft.api.constant.ApiConstants;
import com.osahft.api.exception.FileServiceClientServiceException;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.exception.TransferServiceException;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import com.osahft.api.service.TransferServiceIF;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@Tag(name = "transfers", description = "Transfer operations")
@RequestMapping(ApiConstants.API_VERSION + "/transfers")
public class TransfersController {

    @Autowired
    private TransferServiceIF transferService;

    @Operation(summary = "Create new mail transfer", description = "transfers/postTransfersMails.md")
    @PostMapping(value = "/mails", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMailTransferResponse createNewMailTransfer(@RequestBody CreateMailTransferRequest createMailTransferRequest) throws MailTransferRepositoryException {
        return transferService.createNewMailTransfer(createMailTransferRequest);
    }

    @Operation(summary = "Upload files for mail transfer", description = "transfers/postTransfersMailsMailTransferIdUploads.md")
    @PostMapping(value = "/mails/{mail_transfer_id}/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadFiles(@PathVariable("mail_transfer_id") String mailTransferId, @RequestBody List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException, TransferServiceException {
        transferService.uploadFiles(mailTransferId, files);
    }

    @Operation(summary = "Complete mail transfer", description = "transfers/putMailsMailTransferId.md")
    @PutMapping(value = "/mails/{mail_transfer_id}")
    @ResponseStatus(HttpStatus.OK)
    public void completeMailTransfer(@PathVariable("mail_transfer_id") String mailTransferId) throws MailTransferRepositoryException, FileServiceClientServiceException, TransferServiceException {
        transferService.completeMailTransfer(mailTransferId);
    }

    @Operation(summary = "Authenticate user for mail transfer", description = "transfers/postTransfersMailsMailTransferIdAuthAuthenticationCode.md")
    @PostMapping(value = "/mails/{mail_transfer_id}/auth/{authentication_code}")
    @ResponseStatus(HttpStatus.OK)
    public void authenticateUser(@PathVariable("mail_transfer_id") String mailTransferId, @PathVariable("authentication_code") Integer authenticationCode) throws MailTransferRepositoryException, TransferServiceException {
        transferService.authenticateUser(mailTransferId, authenticationCode);
    }

}

package com.osahft.api.controller;

import com.osahft.api.constant.ApiConstants;
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

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Create new mail transfer", description = "transfers/postTransfersMails.md")
    @PostMapping(value = "/mails", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMailTransferResponse createNewMailTransfer(@RequestBody CreateMailTransferRequest createMailTransferRequest) {
        return transferService.createNewMailTransfer(createMailTransferRequest);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Upload files for mail transfer", description = "transfers/postTransfersMailsMailTransferIdUploads.md")
    @PostMapping(value = "/mails/{mail_transfer_id}/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadFiles(@PathVariable("mail_transfer_id") Long mailTransferId, @RequestBody List<MultipartFile> files) {
        transferService.uploadFiles(mailTransferId, files);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Complete mail transfer", description = "transfers/putMailsMailTransferId.md")
    @PutMapping(value = "/mails/{mail_transfer_id}")
    @ResponseStatus(HttpStatus.OK)
    public void completeMailTransfer(@PathVariable("mail_transfer_id") Long mailTransferId) {
        transferService.completeMailTransfer(mailTransferId);
    }

}

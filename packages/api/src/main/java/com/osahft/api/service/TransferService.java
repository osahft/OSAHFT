package com.osahft.api.service;

import com.osahft.api.entity.MailTransfer;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransferService implements TransferServiceIF {

    // TODO implement this service, this is just an dummy implementation
    // TODO REMOVE WHEN IMPLEMENTING DB
    private final List<MailTransfer> createdTransfers = new LinkedList<>();
    private long availableId = 0;


    @Override
    public CreateMailTransferResponse createNewMailTransfer(CreateMailTransferRequest createMailTransferRequest) {
        MailTransfer mailTransfer = MailTransfer.builder()
                .id(availableId++)
                .mailSender(createMailTransferRequest.getMailSender())
                .mailReceivers(createMailTransferRequest.getMailReceivers())
                .title(createMailTransferRequest.getTitle())
                .message(createMailTransferRequest.getMessage())
                .build();

        createdTransfers.add(mailTransfer);

        return CreateMailTransferResponse.builder()
                .mailTransferId(mailTransfer.getId())
                .build();
    }

    @Override
    public void uploadFiles(Long mailTransferId, List<MultipartFile> files) {
        MailTransfer mailTransfer = createdTransfers.stream().filter(transfer -> transfer.getId().equals(mailTransferId)).findFirst().orElseThrow(RuntimeException::new);
        log.info("Files could be matched to" + mailTransfer);
        LongSummaryStatistics collect = files.stream().map(MultipartFile::getSize).collect(Collectors.summarizingLong(Long::longValue));
        log.info("Total files size in byte:" + collect.getSum());
    }

    @Override
    public void completeMailTransfer(Long mailTransferId) {
        MailTransfer mailTransfer = createdTransfers.stream().filter(transfer -> transfer.getId().equals(mailTransferId)).findFirst().orElseThrow(RuntimeException::new);
        mailTransfer.setTriggered(true);
        log.info("Set triggered of" + mailTransfer + "to true");
    }
}

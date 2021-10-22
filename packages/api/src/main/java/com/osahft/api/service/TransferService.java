package com.osahft.api.service;

import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.model.CreateMailTransferRequest;
import com.osahft.api.model.CreateMailTransferResponse;
import com.osahft.api.repository.MailTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MailService mailService;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    @Override
    public CreateMailTransferResponse createNewMailTransfer(CreateMailTransferRequest createMailTransferRequest) {

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

        MailTransfer get = mailTransferRepository.findById(mailTransfer.getId()).get();


        try {
            mailService.sendVerificationCode(mailTransfer.getId());
        } catch (MailTransferRepositoryException e) {
            e.printStackTrace();
        }

        createdTransfers.add(mailTransfer);
        return CreateMailTransferResponse.builder()
                .mailTransferId(mailTransfer.getId())
                .build();
    }

    @Override
    public void uploadFiles(Long mailTransferId, List<MultipartFile> files) {
        MailTransfer mailTransfer = createdTransfers.stream().filter(transfer -> transfer.getId().equals(mailTransferId)).findFirst().orElseThrow(RuntimeException::new);
        log.info("Files could be matched to " + mailTransfer);
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

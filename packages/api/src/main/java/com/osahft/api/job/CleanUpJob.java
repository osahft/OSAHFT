package com.osahft.api.job;

import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.FileServiceClientServiceException;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.repository.MailTransferRepository;
import com.osahft.api.service.FileServiceClientServiceIF;
import com.osahft.api.service.LocalFileStorageServiceIF;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class CleanUpJob {

    @Autowired
    private MailTransferRepository mailTransferRepository;

    @Autowired
    private LocalFileStorageServiceIF localFileStorageService;

    @Autowired
    @Qualifier("FileServiceClientServiceIF")
    private FileServiceClientServiceIF fileServiceClientService;

    // run once a hour
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void cleanUpLocalFileStorageService() throws LocalFileStorageServiceException, MailTransferRepositoryException {
        // clean up local stored file for finished MailTransfers
        log.info("Cleaning up local file storage");
        for (MailTransfer mailTransfer : mailTransferRepository.findAll()) {
            if (mailTransfer.getState().equals(MailTransfer.State.FINISHED)) {
                localFileStorageService.deleteStorage(mailTransfer.getId());
                mailTransfer.setDataDir(null);
                mailTransferRepository.save(mailTransfer);
            }
        }

    }

    // run once a day
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanUpEverything() throws LocalFileStorageServiceException, MailTransferRepositoryException, FileServiceClientServiceException {
        log.info("Cleaning up local file storage, file service and database");
        Date fourteenDaysAgo = new Date(System.currentTimeMillis() - 14 * 24 * 60 * 60 * 1000);
        for (MailTransfer mailTransfer : mailTransferRepository.findAll()) {
            if (mailTransfer.getCreatedAt().before(fourteenDaysAgo)) {
                localFileStorageService.deleteStorage(mailTransfer.getId());
                fileServiceClientService.deleteFiles(mailTransfer.getId());
                mailTransferRepository.deleteById(mailTransfer.getId());
            }
        }
    }

    // run once every second day
    @Scheduled(fixedRate = 2 * 24 * 60 * 60 * 1000)
    public void cleanUpLockedMailTransfers() throws LocalFileStorageServiceException, MailTransferRepositoryException {
        log.info("Cleaning up locked MailTransfers from database");
        Date oneDayAgo = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        for (MailTransfer mailTransfer : mailTransferRepository.findAll()) {
            if (mailTransfer.getState().equals(MailTransfer.State.LOCKED) && mailTransfer.getCreatedAt().before(oneDayAgo)) {
                localFileStorageService.deleteStorage(mailTransfer.getId());
                mailTransferRepository.deleteById(mailTransfer.getId());
            }
        }
    }

}

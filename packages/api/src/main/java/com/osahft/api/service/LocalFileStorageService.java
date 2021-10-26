package com.osahft.api.service;

import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.LocalFileStorageServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.repository.MailTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LocalFileStorageService implements LocalFileStorageServiceIF {

    @Value("${data.dir}")
    private String rootDataDir;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    private Path getDataDir(String transferId) throws MailTransferRepositoryException {
        return Paths.get(mailTransferRepository
                .findById(transferId).orElseThrow(() -> new MailTransferRepositoryException(transferId)).getDataDir());
    }

    @Override
    public void createStorage(String transferId) throws MailTransferRepositoryException {
        File dataDir = new File(this.rootDataDir, transferId);
        if (!dataDir.exists()) {
            log.info("data.dir {} does not exist. Creating data.dir", dataDir.getAbsolutePath());
            if (!dataDir.mkdirs())
                log.warn("data.dir {} could not be created", dataDir.getAbsolutePath());
            else {
                // store files directory
                MailTransfer mailTransfer = mailTransferRepository.findById(transferId)
                        .orElseThrow(() -> new MailTransferRepositoryException(transferId));
                mailTransfer.setDataDir(dataDir.getAbsolutePath());
                mailTransferRepository.save(mailTransfer);
            }
        }
    }

    @Override
    public void storeFiles(String transferId, List<MultipartFile> files) throws LocalFileStorageServiceException, MailTransferRepositoryException {
        Path dataDir = getDataDir(transferId);
        for (MultipartFile file : files) {
            try {
                Files.copy(file.getInputStream(), dataDir.resolve(Objects.requireNonNull(file.getOriginalFilename())));
            } catch (Exception e) {
                throw new LocalFileStorageServiceException("File " + file.getOriginalFilename() + " could not be stored.", e);
            }
        }
    }

    @Override
    public List<File> readFiles(String transferId) throws MailTransferRepositoryException {
        File dataDir = getDataDir(transferId).toFile();
        File[] files = dataDir.listFiles();
        if (files == null || files.length == 0) {
            log.warn("Could not find files in {}" + dataDir.getAbsolutePath());
            return new LinkedList<>();
        } else {
            return Arrays.asList(files);
        }
    }

    @Override
    public void deleteStorage(String transferId) throws MailTransferRepositoryException, LocalFileStorageServiceException {
        MailTransfer mailTransfer = mailTransferRepository
                .findById(transferId).orElseThrow(() -> new MailTransferRepositoryException(transferId));
        if (mailTransfer.getDataDir() != null) {
            Path dataDir = Paths.get(mailTransfer.getDataDir());
            try {
                FileSystemUtils.deleteRecursively(dataDir);
                mailTransfer.setDataDir(null);
                mailTransferRepository.save(mailTransfer);
            } catch (IOException e) {
                throw new LocalFileStorageServiceException("Could not delete files in " + dataDir.toFile().getAbsolutePath(), e);
            }

        }
    }
}

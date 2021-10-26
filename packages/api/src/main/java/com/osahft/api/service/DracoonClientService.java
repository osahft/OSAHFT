package com.osahft.api.service;


import com.dracoon.sdk.DracoonAuth;
import com.dracoon.sdk.DracoonClient;
import com.dracoon.sdk.error.DracoonException;
import com.dracoon.sdk.model.*;
import com.osahft.api.document.MailReceiverDownloadLinkMapping;
import com.osahft.api.document.MailTransfer;
import com.osahft.api.exception.FileServiceClientServiceException;
import com.osahft.api.exception.MailTransferRepositoryException;
import com.osahft.api.helper.ErrorHelper;
import com.osahft.api.repository.MailTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.Collections;


@Service
@Slf4j
public class DracoonClientService implements FileServiceClientServiceIF {

    private DracoonClient client;

    @Value("${file.service.url}")
    private String url;

    @Value("${file.service.oauth.access-token}")
    private String accessToken;

    @Autowired
    private MailTransferRepository mailTransferRepository;

    @Autowired
    private LocalFileStorageServiceIF localFileStorageService;

    private MailTransfer getMailTransfer(String transferId) throws MailTransferRepositoryException {
        return mailTransferRepository
                .findById(transferId).orElseThrow(() -> new MailTransferRepositoryException(transferId));
    }

    @PostConstruct
    public void init() throws FileServiceClientServiceException {
        try {
            DracoonAuth auth = new DracoonAuth(accessToken);
            client = new DracoonClient.Builder(new URL(url))
                    .auth(auth)
                    .build();
        } catch (Exception e) {
            throw new FileServiceClientServiceException("Couldn't connect to DRACOON-API. ", e);
        }
    }

    @Override
    public void uploadFiles(String transferId) throws FileServiceClientServiceException {
        try {
            // create container
            // room name == transferId
            CreateRoomRequest createRoomRequest = new CreateRoomRequest.Builder(transferId)
                    .adminUserIds(Collections.singletonList(client.account().getUserAccount().getId()))
                    .build();
            Node room = client.nodes().createRoom(createRoomRequest);
            MailTransfer mailTransfer = getMailTransfer(transferId);

            // store containerId
            mailTransfer.setContainerId(room.getId());
            mailTransferRepository.save(mailTransfer);
            log.info("Created room with Node.name={} and Node.Id={}", room.getName(), room.getId());

            // upload files
            for (File fileToUpload : localFileStorageService.readFiles(transferId)) {
                FileUploadRequest request = new FileUploadRequest.Builder(room.getId(), fileToUpload.getName())
                        .build();
                FileUploadCallback callback = new FileUploadCallback() {
                    @Override
                    public void onStarted(String id) {
                        // do nothing
                    }

                    @Override
                    public void onRunning(String id, long bytesSend, long bytesTotal) {
                        // do nothing
                    }

                    @Override
                    public void onFinished(String id, Node node) {
                        // do nothing
                    }

                    @Override
                    public void onCanceled(String id) {
                        // inform user via mail that upload failed
                    }

                    @Override
                    public void onFailed(String id, DracoonException e) {
                        // inform user via mail that upload failed
                    }
                };
                client.nodes().uploadFile(fileToUpload.getName(), request, fileToUpload, callback);
            }

        } catch (Exception e) {
            throw new FileServiceClientServiceException(ErrorHelper.getSERVICE_UNAVAILABLE("Couldn't create room or upload files to DRACOON-API."), e);
        }
    }

    @Override
    public void createDownloadLinks(String transferId) throws FileServiceClientServiceException {
        try {
            // create an individual download link for every receiver
            MailTransfer mailTransfer = getMailTransfer(transferId);
            for (MailReceiverDownloadLinkMapping mapping : mailTransfer.getMailReceiverDownloadLinkMapping()) {
                CreateDownloadShareRequest request = new CreateDownloadShareRequest.Builder(mailTransfer.getContainerId()).
                        build();
                DownloadShare downloadShare = client.shares().createDownloadShare(request);
                mapping.setDownloadLinkId(downloadShare.getId());
                mapping.setDownloadLink(url + "/public/download-shares/" + downloadShare.getAccessKey());
            }
            mailTransferRepository.save(mailTransfer);
        } catch (Exception e) {
            throw new FileServiceClientServiceException("Couldn't create DownloadShares to files at DRACOON-API.", e);
        }
    }

    @Override
    public void deleteFiles(String transferId) throws FileServiceClientServiceException {
        try {
            // delete room to delete files in it as well
            MailTransfer mailTransfer = getMailTransfer(transferId);
            if (mailTransfer.getContainerId() != null) {
                client.nodes().deleteNode(mailTransfer.getContainerId());
                mailTransfer.setContainerId(null);
                mailTransferRepository.save(mailTransfer);
            }
        } catch (Exception e) {
            throw new FileServiceClientServiceException("Couldn't delete room at DRACOON-API.", e);
        }
    }
}

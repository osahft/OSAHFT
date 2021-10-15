package com.osahft.api.service;


import com.dracoon.sdk.DracoonAuth;
import com.dracoon.sdk.DracoonClient;
import com.dracoon.sdk.error.DracoonException;
import com.dracoon.sdk.model.*;
import com.osahft.api.exception.FileServiceClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


@Service
@Slf4j
public class DracoonClientService implements FileServiceClientServiceIF {

    private DracoonClient client;

    @Value("${file.service.url}")
    private String url;

    @Value("${file.service.oauth.access-token}")
    private String accessToken;

    // TODO autowire database

    @PostConstruct
    public void init() throws FileServiceClientException {
        try {
            DracoonAuth auth = new DracoonAuth(accessToken);
            client = new DracoonClient.Builder(new URL(url))
                    .auth(auth)
                    .build();
        } catch (Exception e) {
            throw new FileServiceClientException("Couldn't connect to DRACOON-API. ", e);
        }
    }

    @Override
    public void uploadFiles(Long transferId) throws FileServiceClientException {
        try {
            // create container
            // room name == transferId
            CreateRoomRequest createRoomRequest = new CreateRoomRequest.Builder(transferId.toString())
                    .adminUserIds(Collections.singletonList(client.account().getUserAccount().getId()))
                    .build();
            Node room = client.nodes().createRoom(createRoomRequest);
            // TODO store room id to database containerId
            log.debug("Created room with Node.name={} and Node.Id={}", room.getName(), room.getId());

            // upload files
            // TODO get files from DB
            List<String> filePaths = Collections.singletonList("./README.md");
            for (String filePath : filePaths) {
                File file = new File(filePath);
                FileUploadRequest request = new FileUploadRequest.Builder(room.getId(), file.getName())
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
                client.nodes().uploadFile(file.getName(), request, file, callback);
            }

        } catch (Exception e) {
            throw new FileServiceClientException("Couldn't create room or upload files to DRACOON-API.", e);
        }
    }

    @Override
    public List<String> createDownloadLinks(Long transferId) throws FileServiceClientException {
        try {
            // TODO get receivers from DB and create for every one a share link
            // TODO use containerId as nodeId
            List<String> downloadLinks = new LinkedList<>();
            for (int i = 0; i < 5; i++) {
                CreateDownloadShareRequest request = new CreateDownloadShareRequest.Builder(9L).
                        build();
                DownloadShare downloadShare = client.shares().createDownloadShare(request);
                downloadLinks.add(url + "/public/download-shares/" + downloadShare.getAccessKey());
            }
            return downloadLinks;
        } catch (Exception e) {
            throw new FileServiceClientException("Couldn't create DownloadShares to files at DRACOON-API.", e);
        }
    }

    @Override
    public void deleteFiles(Long transferId) throws FileServiceClientException {
        try {
            // TODO get roomId == containerId from DB
            // delete room to delete files in it as well
            client.nodes().deleteNode(45L);
        } catch (Exception e) {
            throw new FileServiceClientException("Couldn't delete room at DRACOON-API.", e);
        }
    }
}
